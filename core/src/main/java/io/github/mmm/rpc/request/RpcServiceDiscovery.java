/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.request;

import io.github.mmm.marshall.StructuredFormatProvider;

/**
 * Interface to {@link #getUrl(RpcRequest, String) discover service URLs} for a {@link RpcRequest}.
 *
 * @since 1.0.0
 */
public interface RpcServiceDiscovery {

  /**
   * @param request the {@link RpcRequest}.
   * @param format the {@link StructuredFormatProvider#getId() format name} used for marshalling and unmarshaling the
   *        data (request and response).
   * @return the URL of the service handling the given {@link RpcRequest}.
   */
  String getUrl(RpcRequest<?> request, String format);

  /**
   * @return the instance of {@link RpcServiceDiscovery}.
   */
  static RpcServiceDiscovery get() {

    return io.github.mmm.rpc.impl.RpcServiceDiscoveryProvider.DISCOVERY;
  }

}
