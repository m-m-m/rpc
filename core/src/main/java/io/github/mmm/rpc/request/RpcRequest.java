/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.request;

import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.MarshallingObject;

/**
 * Request of an RPC service. It acts as command of the command-pattern that represents a specific operation of the
 * service to invoke.
 *
 * @param <R> type of the {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data} for this request.
 *        May be {@link Void} for no result.
 * @since 1.0.0
 * @see io.github.mmm.rpc.client.RpcClient#call(RpcRequest)
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
