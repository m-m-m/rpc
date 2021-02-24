/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server;

import io.github.mmm.rpc.request.RpcRequest;

/**
 * This is the interface for the handler of a specific {@link RpcRequest}. Its implementation defines how to
 * {@link #handle(RpcRequest) handle} the command on the server-side.
 *
 * @param <D> type of the result of the {@link #handle(RpcRequest) invocation}.
 * @param <R> type of the specific {@link RpcRequest}.
 * @since 1.0.0
 */
public interface RpcHandler<D, R extends RpcRequest<D>> {

  /**
   * @return a new instance of the managed {@link RpcRequest}. E.g. used for unmarshalling the data.
   */
  R createRequest();

  /**
   * This method invokes the given <code>command</code> and returns the result of the invocation.
   *
   * @param request is the {@link RpcRequest} to handle.
   * @return the {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data}.
   */
  D handle(R request);

}
