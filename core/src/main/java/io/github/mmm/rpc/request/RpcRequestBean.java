/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.request;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.Bean;
import io.github.mmm.marshall.MarshallingObject;

/**
 * Implementation of {@link RpcRequest} as {@link Bean}.
 *
 * @param <R> type of the result of the operation represented by this command.
 * @since 1.0.0
 */
public abstract class RpcRequestBean<R> extends Bean implements RpcRequest<R> {

  /**
   * The constructor.
   */
  public RpcRequestBean() {

    super();
  }

  /**
   * The constructor.
   *
   * @param writable the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on or {@code null} to
   *        create a regular mutable {@link Bean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public RpcRequestBean(AbstractBean writable, boolean dynamic) {

    super(writable, dynamic);
  }

  @Override
  public MarshallingObject getRequestMarshalling() {

    return this;
  }

}
