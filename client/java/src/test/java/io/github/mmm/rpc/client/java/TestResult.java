/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.java;

import io.github.mmm.bean.Bean;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * RPC result {@link Bean} for testing.
 */
public class TestResult extends Bean {

  /** Full name of the person. */
  public final StringProperty Name;

  /** Age of the person. */
  public final IntegerProperty Age;

  /**
   * The constructor.
   */
  public TestResult() {

    super();
    this.Name = add().newString().withValidator().mandatory().and().build("Name");
    this.Age = add().newInteger("Age");
  }

}
