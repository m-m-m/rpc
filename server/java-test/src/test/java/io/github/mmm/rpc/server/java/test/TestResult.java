/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java.test;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.Bean;
import io.github.mmm.property.string.StringProperty;
import io.github.mmm.property.temporal.localdate.LocalDateProperty;

/**
 *
 */
public class TestResult extends Bean {

  public final StringProperty Name;

  public final LocalDateProperty Birthday;

  /**
   * The constructor.
   */
  public TestResult() {

    this(null, false);
  }

  /**
   * The constructor.
   *
   * @param writable
   * @param dynamic
   */
  public TestResult(AbstractBean writable, boolean dynamic) {

    super(writable, dynamic);
    this.Name = add().newString().withValidator().mandatory().and().build("Name");
    this.Birthday = add().newLocalDate().withValidator().past().and().build("Birthday");
  }

}
