/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java.test.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.mmm.rpc.server.RpcService;
import io.github.mmm.rpc.server.java.RpcServlet;

/**
 * Extends {@link RpcServlet} for spring dependency-injection.
 */
@Component
public class TestRpcServlet extends RpcServlet {

  /**
   * The constructor.
   *
   * @param rpcService the {@link RpcService} to inject.
   */
  @Autowired
  public TestRpcServlet(RpcService rpcService) {

    super(rpcService);

  }

}
