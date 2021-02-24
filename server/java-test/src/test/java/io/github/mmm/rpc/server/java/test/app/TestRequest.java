/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java.test.app;

import io.github.mmm.marshall.Marshalling;
import io.github.mmm.property.number.longs.LongProperty;
import io.github.mmm.rpc.request.RpcRequestBean;

/**
 * {@link RpcRequestBean} for testing.
 */
public class TestRequest extends RpcRequestBean<TestResult> {

  /** Unique identifier. */
  public final LongProperty Id;

  /**
   * The constructor.
   */
  public TestRequest() {

    super();
    this.Id = add().newLong().withValidator().mandatory().and().build("Id");
  }

  @Override
  public String getPathPattern() {

    return "test/path";
  }

  @Override
  public String getPermission() {

    return null;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Marshalling<TestResult> getResponseMarshalling() {

    return (Marshalling) new TestResult();
  }

}
