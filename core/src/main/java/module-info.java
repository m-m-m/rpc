/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides the API for remote procedure calls (RPC). It is an abstraction of REST that simplifies the programming
 * model. Providing an {@link io.github.mmm.rpc.request.RpcRequest} is all you need to define your service API. To
 * provide the service on the back-end you only need to implement a corresponding
 * {@link io.github.mmm.rpc.server.RpcHandler}. To invoke your service on the front-end you only need to use
 * {@link RpcClient} that supports asynchronous service invocation out of the box.
 *
 * @uses io.github.mmm.rpc.discovery.RpcServiceDiscovery
 * @uses io.github.mmm.rpc.client.RpcClient
 */
module io.github.mmm.rpc {

  requires transitive io.github.mmm.bean;

  uses io.github.mmm.rpc.client.RpcClient;

  uses io.github.mmm.rpc.discovery.RpcServiceDiscovery;

  exports io.github.mmm.rpc.client;

  exports io.github.mmm.rpc.discovery;

  exports io.github.mmm.rpc.request;

  exports io.github.mmm.rpc.response;

  exports io.github.mmm.rpc.server;

}
