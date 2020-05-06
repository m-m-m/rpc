/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.java;

import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.marshall.StructuredFormatFactory;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.rpc.client.RpcClient;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.request.RpcServiceDiscovery;

/**
 * Implementation of {@link RpcClient} for regular JVM based on {@link HttpClient}.
 *
 * @since 1.0.0
 */
public class RpcClientImplJava implements RpcClient {

  private static final String HEADER_ACCEPT = "Accept";

  private static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";

  private static final String HEADER_CONTENT_TYPE = "Content-Type";

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
      String format, Map<String, String> headers) {

    StructuredFormat structuredFormat = StructuredFormatFactory.get().getProvider(format).create();
    String url = this.serviceDiscovery.getUrl(request, format);
    // TODO redesign marshalling for full reactive support?
    StringWriter stringWriter = new StringWriter(1024);
    StructuredWriter writer = structuredFormat.writer(stringWriter);
    request.getRequestMarshalling().write(writer);
    String payload = stringWriter.toString();
    BodyPublisher body = BodyPublishers.ofString(payload);
    Builder builder = HttpRequest.newBuilder(URI.create(url));
    builder = addHeaders(format, headers, builder);
    HttpRequest httpRequest = builder.method(request.getMethod(), body).build();
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
        assert (result == null) || result.isEmpty();
      }
      successConsumer.accept(response);
    };
    this.httpClient.sendAsync(httpRequest, BodyHandlers.ofString()).thenApply(HttpResponse::body)
        .thenAccept(responseFunction).exceptionally(errorFunction);
  }

  private Builder addHeaders(String format, Map<String, String> headers, Builder builder) {

    if (headers.containsKey(HEADER_CONTENT_TYPE)) {
      builder = builder.header(HEADER_CONTENT_TYPE, format + "; charset=utf-8");
    }
    if (headers.containsKey(HEADER_ACCEPT)) {
      builder = builder.header(HEADER_ACCEPT, format);
    }
    if (headers.containsKey(HEADER_ACCEPT_CHARSET)) {
      builder = builder.header(HEADER_ACCEPT_CHARSET, "utf-8");
    }
    for (Entry<String, String> entry : headers.entrySet()) {
      builder = builder.header(entry.getKey(), entry.getValue());
    }
    return builder;
  }

}
