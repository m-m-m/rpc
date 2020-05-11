/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.tvm;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.marshall.Marshalling;
import io.github.mmm.property.number.longs.LongProperty;
import io.github.mmm.rpc.request.RpcRequestBean;

/**
 *
 */
public class TestRequest extends RpcRequestBean<TestResult> {

  public final LongProperty Id;

  /**
   * The constructor.
   */
  public TestRequest() {

    this(null, false);
  }

  /**
   * The constructor.
   *
   * @param writable
   * @param dynamic
   */
  public TestRequest(AbstractBean writable, boolean dynamic) {

    super(writable, dynamic);
    this.Id = add().newLong().withValidator().mandatory().and().build("Id");
  }

  @Override
  public String getPath() {

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
