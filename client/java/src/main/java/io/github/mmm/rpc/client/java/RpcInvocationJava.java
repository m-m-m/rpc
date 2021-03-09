/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.java;

import java.io.ByteArrayOutputStream;
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
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import io.github.mmm.base.i18n.Localizable;
import io.github.mmm.marshall.MarshallingObject;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.marshall.StructuredTextFormat;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.rpc.client.AbstractRpcInvocation;
import io.github.mmm.rpc.client.RpcInvocation;
import io.github.mmm.rpc.discovery.RpcServiceDiscovery;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.response.RpcDataResponse;
import io.github.mmm.rpc.response.RpcException;
import io.github.mmm.rpc.response.RpcNetworkException;

/**
 * Implementation of {@link RpcInvocation} for Java.
 *
 * @param <R> type of the {@link RpcDataResponse#getData() response data}.
 * @since 1.0.0
 */
public class RpcInvocationJava<R> extends AbstractRpcInvocation<R> {

  private final HttpClient httpClient;

  private HttpRequest httpRequest;

  /**
   * The constructor.
   *
   * @param request the {@link RpcRequest} to send.
   * @param serviceDiscovery the {@link RpcServiceDiscovery} to create the URL.
   * @param format the default {@link StructuredFormat}.
   * @param errorHandler the default {@link #errorHandler(Consumer) error handler}.
   * @param headers the default {@link #headers(Map) headers}.
   * @param httpClient the {@link HttpClient} instance.
   */
  @SuppressWarnings("exports")
  public RpcInvocationJava(RpcRequest<R> request, RpcServiceDiscovery serviceDiscovery, StructuredFormat format,
      Consumer<RpcException> errorHandler, Map<String, String> headers, HttpClient httpClient) {

    super(request, serviceDiscovery, format, errorHandler, headers);
    this.httpClient = httpClient;
  }

  @Override
  protected void prepareSend() {

    super.prepareSend();
    Builder builder = HttpRequest.newBuilder(URI.create(this.url));
    for (Entry<String, String> entry : this.headers.entrySet()) {
      builder = builder.header(entry.getKey(), entry.getValue());
    }
    BodyPublisher body = createBody();
    this.httpRequest = builder.method(this.request.getMethod(), body).build();
  }

  private BodyPublisher createBody() {

    MarshallingObject requestMarshalling = this.request.getRequestMarshalling();
    if (requestMarshalling == null) {
      return BodyPublishers.noBody();
    }
    if (this.format.isText()) {
      String payload = ((StructuredTextFormat) this.format).write(requestMarshalling);
      return BodyPublishers.ofString(payload);
    } else {
      assert (this.format.isBinary());
      // HTTP Client streaming API to create BodyPublisher as out stream?
      ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
      StructuredWriter writer = this.format.writer(baos);
      requestMarshalling.write(writer);
      writer.close();
      byte[] payload = baos.toByteArray();
      return BodyPublishers.ofByteArray(payload);
    }
  }

  @Override
  public void sendAsync(Consumer<RpcDataResponse<R>> responseHandler) {

    prepareSend();
    CompletableFuture<HttpResponse<String>> future = this.httpClient.sendAsync(this.httpRequest,
        BodyHandlers.ofString());
    future.thenAccept(response -> {
      ResponseHeaders responseHeaders = new ResponseHeaders(response.headers());
      createResponse(response.statusCode(), null, response.body(), responseHeaders, true, responseHandler);
    }).exceptionally(error -> {
      // TODO
      RpcNetworkException rpcException = new RpcNetworkException(Localizable.ofStatic(error.getLocalizedMessage()),
          error);
      this.errorHandler.accept(rpcException);
      return null;
    });
  }

  @Override
  public RpcDataResponse<R> sendSync() {

    prepareSend();
    try {
      HttpResponse<String> response = this.httpClient.send(this.httpRequest, BodyHandlers.ofString());
      ResponseHeaders responseHeaders = new ResponseHeaders(response.headers());
      return createResponse(response.statusCode(), null, response.body(), responseHeaders, false, null);
    } catch (RpcException error) {
      throw error;
    } catch (Exception error) {
      // TODO
      throw new RpcNetworkException(Localizable.ofStatic(error.getLocalizedMessage()), error);
    }
  }

}
