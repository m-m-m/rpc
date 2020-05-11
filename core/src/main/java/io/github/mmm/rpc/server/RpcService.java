/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server;

import java.io.Reader;
import java.io.Writer;

import io.github.mmm.marshall.StructuredFormatProvider;
import io.github.mmm.rpc.request.RpcRequest;

/**
 * Interface for a generic RPC service that aggregates all {@link RpcHandler}s to handle any RPC.
 *
 * @since 1.0.0
 */
public interface RpcService {

  /**
   * @param <R> type of the response.
   * @param request the {@link RpcRequest} to handle.
   * @return the response for the given {@code request}.
   */
  <R> R handle(RpcRequest<R> request);

  /**
   * @param method the HTTP method.
   * @param path the {@link RpcRequest#getPath() request path}.
   * @param format the {@link StructuredFormatProvider#getId() format name}.
   * @param requestReader the {@link Reader} to read the request from.
   * @param responseWriter the {@link Writer} to write the response to.
   * @param statusSender the reciver of the HTTP status.
   * @return the HTTP status code. It has also been {@link StatusSender#sendStatus(int) send} via the given
   *         {@link StatusSender}.
   */
  int handle(String method, String path, String format, Reader requestReader, Writer responseWriter,
      StatusSender statusSender);

  /**
   * Interface to send the status (abstracting {@code HttpResponse}).
   */
  @FunctionalInterface
  interface StatusSender {

    /**
     * @param status the HTTP status to send.
     */
    default void sendStatus(int status) {

      sendStatus(status, null);
    }

    /**
     * @param status the HTTP status to send.
     * @param statusText the optional status text.
     */
    void sendStatus(int status, String statusText);
  }

}
