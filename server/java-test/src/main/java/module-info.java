/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Internal module only for integrative testing of RPC server for Java.
 */
open module io.github.mmm.rpc.server.java.test {

  requires transitive io.github.mmm.rpc.server.java;

  requires transitive io.github.mmm.rpc.client.java;

}
