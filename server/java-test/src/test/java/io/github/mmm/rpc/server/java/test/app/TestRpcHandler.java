/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java.test.app;

import org.springframework.stereotype.Component;

import io.github.mmm.rpc.server.RpcHandler;

/**
 * Implementation of {@link RpcHandler} of {@link TestRequest}.
 */
@Component
public class TestRpcHandler implements RpcHandler<TestResult, TestRequest> {

  @Override
  public TestRequest createRequest() {

    return new TestRequest();
  }

  @Override
  public TestResult handle(TestRequest command) {

    if (command.Id.getValue() == 4711) {
      TestResult result = new TestResult();
      result.Name.set("John Doe");
      result.Age.setValue(42);
      return result;
    }
    return null;
  }

}
