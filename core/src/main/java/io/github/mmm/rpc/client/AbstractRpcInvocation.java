/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import io.github.mmm.base.i18n.Localizable;
import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.rpc.impl.RpcErrorData;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.request.RpcServiceDiscovery;
import io.github.mmm.rpc.response.AttributeReadHttpHeader;
import io.github.mmm.rpc.response.RpcDataResponse;
import io.github.mmm.rpc.response.RpcException;
import io.github.mmm.rpc.response.RpcParseException;
import io.github.mmm.rpc.response.RpcResponse;
import io.github.mmm.rpc.response.RpcResponseException;

/**
 * Abstract base implementation of {@link RpcInvocation}.
 *
 * @param <R> type of the {@link RpcDataResponse#getData() response data}.
 * @since 1.0.0
 */
public abstract class AbstractRpcInvocation<R> implements RpcInvocation<R> {

  private static final String DEFAULT_ENCODING = "utf-8";

  /** The {@link RpcRequest} to send. */
  protected final RpcRequest<R> request;

  /** The {@link RpcServiceDiscovery} to create the URL. */
  protected final RpcServiceDiscovery serviceDiscovery;

  /** The HTTP {@link #headers(Map) headers}. */
  protected final Map<String, String> headers;

  /** The {@link StructuredFormat} to use for marshalling and unmarshalling. */
  protected StructuredFormat format;

  /** The {@link #errorHandler(Consumer) error handler}. */
  protected Consumer<RpcException> errorHandler;

  /** The URL to invoke that is created in {@link #prepareSend()}. */
  protected String url;

  /**
   * The constructor.
   *
   * @param request the {@link RpcRequest} to send.
   * @param serviceDiscovery the {@link RpcServiceDiscovery} to create the URL.
   * @param format the default {@link StructuredFormat}.
   * @param errorHandler the default {@link #errorHandler(Consumer) error handler}.
   * @param headers the default {@link #headers(Map) headers}.
   */
  public AbstractRpcInvocation(RpcRequest<R> request, RpcServiceDiscovery serviceDiscovery, StructuredFormat format,
      Consumer<RpcException> errorHandler, Map<String, String> headers) {

    super();
    this.request = request;
    this.serviceDiscovery = serviceDiscovery;
    this.format = format;
    this.errorHandler = errorHandler;
    this.headers = headers;
  }

  @Override
  public RpcInvocation<R> format(StructuredFormat newFormat) {

    Objects.requireNonNull(newFormat, "format");
    this.format = newFormat;
    return this;
  }

  @Override
  public RpcInvocation<R> errorHandler(Consumer<RpcException> newErrorHandler) {

    Objects.requireNonNull(newErrorHandler, "errorHandler");
    this.errorHandler = newErrorHandler;
    return this;
  }

  @Override
  public RpcInvocation<R> header(String key, String value) {

    this.headers.put(key, value);
    return this;
  }

  @Override
  public RpcInvocation<R> headers(Map<String, String> httpHeaders) {

    this.headers.putAll(httpHeaders);
    return this;
  }

  /**
   * Called by {@code send*} methods to prepare headers and other aspects.
   */
  protected void prepareSend() {

    String formatType = this.format.getId();
    this.url = this.serviceDiscovery.getUrl(this.request, this.format.getId());
    this.headers.putIfAbsent(HEADER_CONTENT_TYPE, formatType + "; charset=" + DEFAULT_ENCODING);
    this.headers.putIfAbsent(HEADER_ACCEPT, formatType);
    this.headers.putIfAbsent(HEADER_ACCEPT_CHARSET, DEFAULT_ENCODING);
  }

  /**
   * @param status the {@link RpcDataResponse#getStatus() status code}.
   * @param statusText the status text message.
   * @param payload the result data typically as {@link String}.
   * @param responseHeaders the HTTP response headers.
   * @param async - {@code true} on {@link #sendAsync(Consumer) asynchronous sending}, {@code false} otherwise (on
   *        {@link #sendSync() synchronous sending}).
   * @param responseHandler the {@link Consumer} {@link Consumer#accept(Object) receiving} the {@link RpcDataResponse}
   *        after successful transmission. Shall be {@code null} if {@code async} is {@code false}.
   * @return the {@link RpcResponse}.
   * @throws RpcException in case of an error.
   */
  protected RpcDataResponse<R> createResponse(int status, String statusText, Object payload,
      AttributeReadHttpHeader responseHeaders, boolean async, Consumer<RpcDataResponse<R>> responseHandler) {

    RpcException error = null;
    RpcDataResponse<R> response = null;
    if (isSuccess(status)) {
      R data = null;
      Marshalling<R> marshalling = this.request.getResponseMarshalling();
      if (marshalling != null) {
        if ((payload != null) && (!async || (responseHandler != null))) {
          try {
            data = marshalling.readObject(this.format.reader(payload));
          } catch (RuntimeException cause) {
            error = new RpcParseException(cause, status, responseHeaders);
          }
        }
      } else {
        assert (payload == null);
      }
      response = new RpcDataResponse<>(status, responseHeaders, data);
    } else {
      RpcErrorData errorData = new RpcErrorData();
      try {
        if (payload != null) {
          errorData.read(this.format.reader(payload));
        }
      } catch (RuntimeException cause) {
        // TODO log or attach as cause
      }
      error = new RpcResponseException(Localizable.ofStatic(statusText), status, responseHeaders,
          errorData.isTechnical(), errorData.getCode(), errorData.getUuid());
    }
    if (error != null) {
      if (async) {
        this.errorHandler.accept(error);
        return null;
      } else {
        throw error;
      }
    }
    if (responseHandler != null) {
      responseHandler.accept(response);
    }
    return response;
  }

  /**
   * @param status the HTTP status code.
   * @return {@code true} if success, {@code false} otherwise (on error).
   */
  protected boolean isSuccess(int status) {

    return (status >= 200) && (status < 300);
  }

  @Override
  public String toString() {

    return this.request + "@" + this.format.getId();
  }

}
