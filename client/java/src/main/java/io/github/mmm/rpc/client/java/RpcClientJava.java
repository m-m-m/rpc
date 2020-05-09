/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.java;

import java.net.http.HttpClient;

import io.github.mmm.rpc.client.AbstractRpcClient;
import io.github.mmm.rpc.client.RpcClient;
import io.github.mmm.rpc.client.RpcInvocation;
import io.github.mmm.rpc.request.RpcRequest;

/**
 * Implementation of {@link RpcClient} for regular JVM based on {@link HttpClient}.
 *
 * @since 1.0.0
 */
public class RpcClientJava extends AbstractRpcClient {

  private final HttpClient httpClient;

  /**
   * The constructor.
   */
  public RpcClientJava() {

    this(HttpClient.newHttpClient());
  }

  /**
   * The constructor.
   *
   * @param httpClient the preconfigured {@link HttpClient} to use.
   */
  @SuppressWarnings("exports")
  public RpcClientJava(HttpClient httpClient) {

    super();
    this.httpClient = httpClient;
  }

  @Override
  public <R> RpcInvocation<R> call(RpcRequest<R> request) {

    return new RpcInvocationJava<>(request, getServiceDiscovery(), getDefaultFormat(), getDefaultErrorHandler(),
        createDefaultHeaders(), this.httpClient);
  }

}
