/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.impl;

import java.util.ServiceLoader;

import io.github.mmm.base.config.ServiceHelper;
import io.github.mmm.rpc.client.RpcClient;

/**
 * Provider of {@link RpcClient}.
 *
 * @since 1.0.0
 */
public class RpcClientProvider {

  /** Instance of {@link RpcClient}. */
  public static final RpcClient CLIENT = ServiceHelper.singleton(ServiceLoader.load(RpcClient.class));

}
