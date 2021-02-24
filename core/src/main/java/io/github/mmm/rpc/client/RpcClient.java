/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client;

import io.github.mmm.rpc.request.RpcRequest;

/**
 * Interface to call a {@link RpcRequest} from the client easily. To avoid boilerplate coding and overhead for CRUD
 * operations use {@link RpcCrudClient}.
 *
 * @since 1.0.0
 */
public interface RpcClient {

  /**
   * Prepares an {@link RpcInvocation} that can be completed by any {@code send*} method such as
   * {@link RpcInvocation#sendAsync(java.util.function.Consumer)}.
   *
   * @param <D> type of the {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data}.
   * @param request the {@link RpcRequest}.
   * @return the {@link RpcInvocation} to configure additional options and finally
   *         {@link RpcInvocation#sendAsync(java.util.function.Consumer) send} the request.
   */
  <D> RpcInvocation<D> call(RpcRequest<D> request);

  /**
   * Immediately invokes the given {@link RpcRequest} ignoring the response.
   *
   * @param request the {@link RpcRequest} with {@link Void} response data.
   */
  default void sendAsync(RpcRequest<Void> request) {

    call(request).sendAsync(null);
  }

  /**
   * @return the instance of this {@link RpcClient}.
   */
  static RpcClient get() {

    return io.github.mmm.rpc.impl.RpcClientProvider.CLIENT;
  }

}
