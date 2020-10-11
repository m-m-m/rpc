/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.discovery;

import io.github.mmm.rpc.request.RpcRequest;

/**
 * Static implementation of {@link RpcServiceDiscovery}.
 *
 * @since 1.0.0
 */
public class StaticServiceDiscovery extends AbstractRpcServiceDiscovery {

  private final String baseUrl;

  /**
   * The constructor.
   *
   * @param baseUrl the base URL.
   */
  public StaticServiceDiscovery(String baseUrl) {

    super();
    this.baseUrl = baseUrl;
  }

  @Override
  protected String getBaseUrl(RpcRequest<?> request, String format) {

    return this.baseUrl;
  }

}
