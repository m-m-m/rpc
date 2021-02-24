/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.request;

import io.github.mmm.bean.Bean;
import io.github.mmm.marshall.MarshallingObject;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.value.observable.object.WritableSimpleValue;

/**
 * Implementation of {@link RpcRequest} as {@link Bean}.
 *
 * @param <D> type of the {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data} for this request.
 *        May be {@link Void} for no result.
 * @since 1.0.0
 */
public abstract class RpcRequestBean<D> extends Bean implements RpcRequest<D> {

  /**
   * The constructor.
   */
  public RpcRequestBean() {

    super();
  }

  @Override
  public MarshallingObject getRequestMarshalling() {

    return this;
  }

  @Override
  public String getPathVariable(String name) {

    Object value = get(name);
    if (value != null) {
      return value.toString();
    }
    return RpcRequest.super.getPathVariable(name);
  }

  @Override
  public void setPathVariable(String name, String value) {

    WritableProperty<?> property = getProperty(name);
    if (property instanceof WritableSimpleValue) {
      ((WritableSimpleValue<?>) property).setAsString(value);
    } else {
      RpcRequest.super.setPathVariable(name, value);
    }
  }

}
