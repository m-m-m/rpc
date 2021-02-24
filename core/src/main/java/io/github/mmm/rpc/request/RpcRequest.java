/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.request;

import java.util.Map;
import java.util.Map.Entry;

import io.github.mmm.base.exception.ObjectNotFoundException;
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
   * @return the path of this command (e.g. "/MyEntity/{id}"). May contain {@link #getPathVariable(String) variables} in
   *         the form {@code {«variable-name»}}.
   */
  String getPathPattern();

  /**
   * Feel free to override with a more efficient implementation for your final request implementation.
   *
   * @return the actual path value of this command with all {@link #getPathVariable(String) variables} resolved.
   */
  default String getPathValue() {

    String path = getPathPattern();
    int varStart = path.indexOf('{');
    if (varStart < 0) {
      return path;
    }
    int start = 0;
    int length = path.length();
    StringBuilder sb = new StringBuilder(length + 6);
    while (varStart >= 0) {
      sb.append(path.substring(start, varStart));
      start = varStart + 1;
      int varEnd = path.indexOf('}', start);
      if (varEnd < start) {
        throw new IllegalArgumentException(path);
      }
      String var = path.substring(start, varEnd);
      String value = getPathVariable(var);
      if (value == null) {
        throw new ObjectNotFoundException("variable", var);
      }
      sb.append(value);
      start = varEnd + 1;
      varStart = path.indexOf('{', start);
    }
    if (start < length) {
      sb.append(path.substring(start));
    }
    return sb.toString();
  }

  /**
   * @param variables the {@link Map} with the variables.
   */
  default void setPathVariables(Map<String, String> variables) {

    for (Entry<String, String> entry : variables.entrySet()) {
      setPathVariable(entry.getKey(), entry.getValue());
    }
  }

  /**
   * @param name the name of the variable.
   * @param value the value of the variable as {@link String}.
   */
  default void setPathVariable(String name, String value) {

  }

  /**
   * @param name the name of the variable.
   * @return the value of the variable as {@link String}.
   */
  default String getPathVariable(String name) {

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
