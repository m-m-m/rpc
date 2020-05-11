/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.mmm.rpc.server.RpcHandler;
import io.github.mmm.rpc.server.java.AbstractRpcService;

/**
 * Implementation of {@link AbstractRpcService} using spring {@link Autowired}.
 */
@Component
public class TestRpcService extends AbstractRpcService {

  @Autowired
  public void setHandlers(List<RpcHandler<?, ?>> handlers) {

    for (RpcHandler<?, ?> handler : handlers) {
      register(handler);
    }
  }

}
