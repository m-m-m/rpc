/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
module io.github.mmm.rpc.client.java {

  requires java.net.http;

  requires transitive io.github.mmm.rpc;

  provides io.github.mmm.rpc.client.RpcClient with //
      io.github.mmm.rpc.client.java.RpcClientJava;

  exports io.github.mmm.rpc.client.java;

}
