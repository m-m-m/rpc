/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.impl;

import java.util.ServiceLoader;

import io.github.mmm.base.service.ServiceHelper;
import io.github.mmm.rpc.discovery.RpcServiceDiscovery;

/**
 * Provider for {@link RpcServiceDiscovery}.
 *
 * @since 1.0.0
 */
public class RpcServiceDiscoveryProvider {

  /** The instance of {@link RpcServiceDiscovery}. */
  public static final RpcServiceDiscovery DISCOVERY = ServiceHelper
      .singleton(ServiceLoader.load(RpcServiceDiscovery.class));

}
