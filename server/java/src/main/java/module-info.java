/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides RPC client based on pure Java using {@link javax.servlet.http.HttpServlet}.
 */
open module io.github.mmm.rpc.server.java {

  requires transitive io.github.mmm.rpc;

  // requires java.servlet;

  requires javax.servlet.api;

  exports io.github.mmm.rpc.server.java;

}
