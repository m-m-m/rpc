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
        cause.printStackTrace();
      }
      String message = errorData.getMessage();
      if ((message == null) || message.isEmpty()) {
        message = statusText;
        if (statusText == null) {
          message = statusText(status);
        }
        message = status + ":" + message;
      }
      error = new RpcResponseException(Localizable.ofStatic(message), status, responseHeaders, errorData.isTechnical(),
          errorData.getCode(), errorData.getUuid());
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
   * @param status
   * @return
   */
  private String statusText(int status) {

    switch (status) {
      case 400:
        return "Bad Request";
      case 401:
        return "Unauthorized";
      case 402:
        return "Payment Required";
      case 403:
        return "Forbidden";
      case 404:
        return "Not Found";
      case 405:
        return "Method Not Allowed";
      case 406:
        return "Not Acceptable";
      case 407:
        return "Proxy Authentication Required";
      case 408:
        return "Request Timeout";
      case 409:
        return "Conflict";
      case 410:
        return "Gone";
      case 411:
        return "Length Required";
      case 412:
        return "Precondition Failed";
      case 413:
        return "Request Entity Too Large";
      case 414:
        return "URI Too Long";
      case 415:
        return "Unsupported Media Type";
      case 416:
        return "Requested range not satisfiable";
      case 417:
        return "Expectation Failed";
      case 420:
        return "Policy Not Fulfilled";
      case 421:
        return "Misdirected Request";
      case 422:
        return "Unprocessable Entity";
      case 423:
        return "Locked";
      case 424:
        return "Failed Dependency";
      case 426:
        return "Upgrade Required";
      case 428:
        return "Precondition Required";
      case 429:
        return "Too Many Requests";
      case 431:
        return "Request Header Fields Too Large";
      case 451:
        return "Unavailable For Legal Reasons";
      case 500:
        return "Internal Server Error";
      case 501:
        return "Not Implemented";
      case 502:
        return "Bad Gateway";
      case 503:
        return "Service Unavailable";
      case 504:
        return "Gateway Timeout";
      case 505:
        return "HTTP Version not supported";
      case 506:
        return "Variant Also Negotiates";
      case 507:
        return "Insufficient Storage";
      case 508:
        return "Loop Detected";
      case 509:
        return "Bandwidth Limit Exceeded";
      case 510:
        return "Not Extended";
      case 511:
        return "Network Authentication Required";
      default:
        return "unknown status code";
    }
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
