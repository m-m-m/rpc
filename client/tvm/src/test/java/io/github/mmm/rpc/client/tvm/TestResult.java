/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.tvm;

import io.github.mmm.bean.Bean;
import io.github.mmm.property.string.StringProperty;
import io.github.mmm.property.temporal.localdate.LocalDateProperty;

/**
 * RPC result {@link Bean} for testing.
 */
public class TestResult extends Bean {

  /** Full name of the person. */
  public final StringProperty Name;

  /** Birthday of the person. */
  public final LocalDateProperty Birthday;

  /**
   * The constructor.
   */
  public TestResult() {

    super();
    this.Name = add().newString().withValidator().mandatory().and().build("Name");
    this.Birthday = add().newLocalDate().withValidator().past().and().build("Birthday");
  }

}
