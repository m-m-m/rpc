/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.java;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.marshall.StructuredFormatFactory;
import io.github.mmm.rpc.client.RpcClient;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.request.RpcServiceDiscovery;

/**
 * Implementation of {@link RpcClient} for regular JVM based on {@link HttpClient}.
 *
 * @since 1.0.0
 */
public class RpcClientImplJava implements RpcClient {

  private final HttpClient httpClient;

  private final RpcServiceDiscovery serviceDiscovery;

  private Consumer<Throwable> defaultFailureConsumer;

  /**
   * The constructor.
   */
  public RpcClientImplJava() {

    this(HttpClient.newHttpClient(), RpcServiceDiscovery.get());
  }

  /**
   * The constructor.
   *
   * @param httpClient the preconfigured {@link HttpClient} to use.
   * @param serviceDiscovery the {@link RpcServiceDiscovery} to use.
   */
  @SuppressWarnings("exports")
  public RpcClientImplJava(HttpClient httpClient, RpcServiceDiscovery serviceDiscovery) {

    super();
    this.httpClient = httpClient;
    this.serviceDiscovery = serviceDiscovery;
    this.defaultFailureConsumer = t -> t.printStackTrace(); // TODO use logger
  }

  @Override
  public Consumer<Throwable> getDefaultFailureConsumer() {

    return this.defaultFailureConsumer;
  }

  @Override
  public void setDefaultFailureConsumer(Consumer<Throwable> defaultFailureConsumer) {

    Objects.requireNonNull(defaultFailureConsumer);
    this.defaultFailureConsumer = defaultFailureConsumer;
  }

  @Override
  public <R> void call(RpcRequest<R> request, Consumer<R> successConsumer, Consumer<Throwable> failureConsumer,
      String format) {

    StructuredFormat structuredFormat = StructuredFormatFactory.get().getProvider(format).create();
    String url = this.serviceDiscovery.getUrl(request, format);
    // TODO redesign marshalling for full reactive support?
    String payload = structuredFormat.write(request);
    BodyPublisher body = BodyPublishers.ofString(payload);
    HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url)).method(request.getMethod(), body).build();
    Function<Throwable, Void> errorFunction = error -> {
      failureConsumer.accept(error);
      return null;
    };
    Consumer<String> responseFunction = result -> {
      if (successConsumer == null) {
        return;
      }
      R response = null;
      Marshalling<R> marshalling = request.getResponseMarshalling();
      if (marshalling != null) {
        response = marshalling.readObject(structuredFormat.reader(result));
      } else {
        assert result.isEmpty();
      }
      successConsumer.accept(response);
    };
    this.httpClient.sendAsync(httpRequest, BodyHandlers.ofString()).thenApply(HttpResponse::body)
        .thenAccept(responseFunction).exceptionally(errorFunction);
  }

}
