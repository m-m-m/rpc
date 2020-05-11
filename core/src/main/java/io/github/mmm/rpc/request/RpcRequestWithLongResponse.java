/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.request;

import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.standard.DatatypeMarshalling;

/**
 * {@link RpcRequest} returning a {@link Long} as response.
 *
 * @since 1.0.0
 */
public interface RpcRequestWithLongResponse extends RpcRequest<Long> {

  @Override
  default Marshalling<Long> getResponseMarshalling() {

    return DatatypeMarshalling.of(Long.class);
  }

}
