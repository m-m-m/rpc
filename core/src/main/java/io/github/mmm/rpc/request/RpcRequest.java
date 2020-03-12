/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.request;

import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.MarshallingObject;

/**
 * This is the interface for a command that represents a specific operation of an RPC service.
 *
 * @param <R> type of the result of the operation represented by this command.
 * @since 1.0.0
 * @see io.github.mmm.rpc.client.RpcClient#call(RpcRequest, java.util.function.Consumer)
 * @see io.github.mmm.rpc.server.RpcHandler#handle(RpcRequest)
 */
public interface RpcRequest<R> {

  /** {@link #getMethod() Method} {@value}. */
  public static final String METHOD_POST = "POST";

  /** {@link #getMethod() Method} {@value}. */
  public static final String METHOD_GET = "GET";

  /** {@link #getMethod() Method} {@value}. */
  public static final String METHOD_DELETE = "DELETE";

  /**
   * @return the method to use (when sending via HTTP[S]).
   */
  default String getMethod() {

    return METHOD_POST;
  }

  /**
   * @return the (relative) path of this command.
   */
  String getPath();

  /**
   * @return the permission required to call this request.
   */
  String getPermission();

  /**
   * @return the actual payload of this request as {@link MarshallingObject}. Can be the request itself or a contained
   *         bean.
   */
  MarshallingObject getRequestMarshalling();

  /**
   * @return the {@link Marshalling} for the response of this request.
   */
  Marshalling<R> getResponseMarshalling();

}
