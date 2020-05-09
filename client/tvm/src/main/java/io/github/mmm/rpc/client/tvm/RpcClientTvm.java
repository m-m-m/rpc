/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.tvm;

import io.github.mmm.rpc.client.AbstractRpcClient;
import io.github.mmm.rpc.client.RpcClient;
import io.github.mmm.rpc.client.RpcInvocation;
import io.github.mmm.rpc.request.RpcRequest;

/**
 * Implementation of {@link RpcClient} for TeaVM.
 *
 * @since 1.0.0
 */
public class RpcClientTvm extends AbstractRpcClient {

  @Override
  public <R> RpcInvocation<R> call(RpcRequest<R> request) {

    return new RpcInvocationTvm<>(request, getServiceDiscovery(), getDefaultFormat(), getDefaultErrorHandler(),
        createDefaultHeaders());
  }
}
