/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides RPC client based on TeaVM using {@link org.teavm.jso.ajax.XMLHttpRequest}.
 *
 * @provides io.github.mmm.rpc.client.RpcClient
 */
module io.github.mmm.rpc.client.tvm {

  requires transitive io.github.mmm.rpc;

  requires transitive org.teavm.jso;

  requires transitive org.teavm.jso.apis;

  provides io.github.mmm.rpc.client.RpcClient with //
      io.github.mmm.rpc.client.tvm.RpcClientTvm;

}
