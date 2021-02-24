/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.request;

import java.util.Map;
import java.util.Map.Entry;

import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.MarshallingObject;

/**
 * Request of an RPC service. It acts as command of the command-pattern that represents a specific operation of the
 * service to invoke.
 *
 * @param <D> type of the {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data} for this request.
 *        May be {@link Void} for no result.
 * @since 1.0.0
 * @see io.github.mmm.rpc.client.RpcClient#call(RpcRequest)
 * @see io.github.mmm.rpc.server.RpcHandler#handle(RpcRequest)
 */
public interface RpcRequest<D> extends HttpMethod {

  @Override
  default String getMethod() {

    return METHOD_POST;
  }

  /**
   * @return the (relative) path of this command. May contain variables in the form {@code {«variable-name»}}.
   */
  String getPath();

  /**
   * @param variables the {@link Map} with the variables.
   */
  default void setPathVariables(Map<String, String> variables) {

    for (Entry<String, String> entry : variables.entrySet()) {
      setPathVariable(entry.getKey(), entry.getValue());
    }
  }

  /**
   * @param key the name of the variable.
   * @param value the value of the variable as {@link String}.
   */
  default void setPathVariable(String key, String value) {

  }

  /**
   * @param key the name of the variable.
   * @return the value of the variable as {@link String}.
   */
  default String getPathVariable(String key) {

    return null;
  }

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
  Marshalling<D> getResponseMarshalling();

}
