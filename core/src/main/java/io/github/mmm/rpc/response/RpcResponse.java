/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.response;

/**
 * Response of an RPC service representing the result of an {@link io.github.mmm.rpc.request.RpcRequest}.
 *
 * @since 1.0.0
 * @see RpcDataResponse
 * @see RpcException
 */
public interface RpcResponse extends AttributeReadHttpHeader {

  /**
   * @return the HTTP status code of this response. E.g. 200 for OK (successful response transmission), 403 for
   *         forbidden (access denied), 404 for not found, or 500 for internal server error. In case the server could
   *         not be reached due to technical network errors, the status will be {@code -1}.
   */
  int getStatus();

  /**
   * @return {@code true} if the RPC failed and this response represents an error, {@code false} otherwise (in case of a
   *         successful transmission).
   */
  boolean isError();

  /**
   * @return {@code true} if successful, {@code false} in case of an {@link #isError() error}.
   */
  default boolean isSuccess() {

    return !isError();
  }

}
