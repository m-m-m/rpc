/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.request;

import java.time.Instant;

import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.standard.DatatypeMarshalling;

/**
 * {@link RpcRequest} returning a {@link Instant} as response.
 */
public interface RpcRequestWithInstantResponse extends RpcRequest<Instant> {

  @Override
  default Marshalling<Instant> getResponseMarshalling() {

    return DatatypeMarshalling.of(Instant.class);
  }

}
