/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server;

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
   * @param request the {@link HttpRequestReader} to read the request.
   * @param response the {@link HttpResponseWriter} to write the response.
   */
  void handle(HttpRequestReader request, HttpResponseWriter response);

}
