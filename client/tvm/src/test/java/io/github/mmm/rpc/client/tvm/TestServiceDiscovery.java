/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.tvm;

import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.request.RpcServiceDiscovery;

/**
 *
 */
public class TestServiceDiscovery implements RpcServiceDiscovery {

  private final String baseUrl;

  /**
   * The constructor.
   *
   * @param baseUrl the base URL.
   */
  public TestServiceDiscovery(String baseUrl) {

    super();
    this.baseUrl = baseUrl;
  }

  @Override
  public String getUrl(RpcRequest<?> request, String format) {

    return this.baseUrl + request.getPath();
  }

}
