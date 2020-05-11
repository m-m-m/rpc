/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java.test;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import io.github.mmm.rpc.server.RpcHandler;

/**
 *
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
      result.Birthday.set(LocalDate.of(1999, 12, 31));
      return result;
    }
    return null;
  }

}
