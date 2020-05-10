/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.marshall.StructuredFormatFactory;
import io.github.mmm.rpc.response.RpcDataResponse;
import io.github.mmm.rpc.response.RpcException;
import io.github.mmm.rpc.response.RpcResponse;

/**
 * Interface for fluent API calls from {@link RpcClient}.
 *
 * @param <R> type of the {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data}.
 * @since 1.0.0
 */
public interface RpcInvocation<R> {

  /** HTTP {@link #header(String, String) header} {@value}. */
  String HEADER_ACCEPT = "Accept";

  /** HTTP {@link #header(String, String) header} {@value}. */
  String HEADER_ACCEPT_CHARSET = "Accept-Charset";

  /** HTTP {@link #header(String, String) header} {@value}. */
  String HEADER_CONTENT_TYPE = "Content-Type";

  /** HTTP {@link #header(String, String) header} {@value}. */
  String HEADER_AUTHORIZATION = "Authorization";

  /**
   * @param format the {@link io.github.mmm.marshall.StructuredFormat#getId() format} used for marshalling and
   *        unmarshaling the data (request and response). E.g. {@link io.github.mmm.marshall.StructuredFormat#ID_JSON}
   *        or {@link io.github.mmm.marshall.StructuredFormat#ID_XML}.
   * @return this object for fluent API calls.
   * @see AbstractRpcClient#getDefaultFormat()
   */
  default RpcInvocation<R> format(String format) {

    return format(StructuredFormatFactory.get().create(format));
  }

  /**
   * @param format the {@link io.github.mmm.marshall.StructuredFormat#getId() format} used for marshalling and
   *        unmarshaling the data (request and response). E.g. {@link io.github.mmm.marshall.StructuredFormat#ID_JSON}
   *        or {@link io.github.mmm.marshall.StructuredFormat#ID_XML}.
   * @return this object for fluent API calls.
   * @see AbstractRpcClient#getDefaultFormat()
   */
  RpcInvocation<R> format(StructuredFormat format);

  /**
   * Configures an explicit HTTP header to send with the request (e.g. for authentication).
   *
   * @param key the name of the HTTP header.
   * @param value the value of the HTTP header.
   * @return this object for fluent API calls.
   * @see #headers(Map)
   * @see AbstractRpcClient#setDefaultHeader(String, String)
   */
  RpcInvocation<R> header(String key, String value);

  /**
   * Configures explicit HTTP headers to send with the request (e.g. for authentication).
   *
   * @param headers the {@link Map} with the HTTP headers.
   * @return this object for fluent API calls.
   * @see #header(String, String)
   */
  default RpcInvocation<R> headers(Map<String, String> headers) {

    for (Entry<String, String> entry : headers.entrySet()) {
      header(entry.getKey(), entry.getValue());
    }
    return this;
  }

  /**
   * @param errorHandler the explicit {@link Consumer} to {@link Consumer#accept(Object) handle} errors of
   *        {@link #sendAsync(Consumer) asynchronous invocations}.
   * @return this object for fluent API calls.
   * @see AbstractRpcClient#getDefaultErrorHandler()
   */
  RpcInvocation<R> errorHandler(Consumer<RpcException> errorHandler);

  /**
   * Terminating operation to send this invocation asynchronously to the server.
   *
   * @param responseHandler the {@link Consumer} {@link Consumer#accept(Object) receiving} the {@link RpcDataResponse}
   *        after successful transmission. In case of an error the {@link #errorHandler(Consumer) error handler} is
   *        called instead.
   */
  void sendAsync(Consumer<RpcDataResponse<R>> responseHandler);

  /**
   * Terminating operation to send this invocation asynchronously to the server. Use {@link #sendAsync(Consumer)}
   * instead to get access to {@link RpcDataResponse} with {@link RpcDataResponse#getStatus() status code} and
   * {@link RpcDataResponse#getHeader(String) response headers}.
   *
   * @param resultHandler the {@link Consumer} {@link Consumer#accept(Object) receiving} the
   *        {@link RpcDataResponse#getData() response data} after successful transmission. In case of an error the
   *        {@link #errorHandler(Consumer) error handler} is called instead.
   */
  default void sendAsyncData(Consumer<R> resultHandler) {

    Consumer<RpcDataResponse<R>> successConsumer = r -> r.getData();
    sendAsync(successConsumer);
  }

  /**
   * Terminating operation to send this invocation synchronously to the server.<br>
   * <b>Attention</b>: A synchronous call is blocking the current thread and typically causing more resource overhead.
   * This is only supported for convenience. If possible always prefer {@link #sendAsync(Consumer)}.
   *
   * @return the successful {@link RpcResponse} with the {@link RpcDataResponse#getData() result data}.
   * @throws io.github.mmm.rpc.response.RpcException if the service failed on the server-side or the service-side could
   *         not be reached due to network issues.
   */
  RpcDataResponse<R> sendSync();
}
