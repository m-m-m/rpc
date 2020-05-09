/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.response;

/**
 * Implementation of {@link RpcResponse} for success with {@link #getData() unmarshalled response data}.
 *
 * @param <R> type of {@link #getData() data}.
 * @since 1.0.0
 */
public class RpcDataResponse<R> implements RpcResponse {

  private final int status;

  private final AttributeReadHttpHeader headers;

  private final R data;

  /**
   * The constructor.
   *
   * @param status the {@link #getStatus() status code}.
   * @param headers the HTTP headers.
   * @param data the {@link #getData() data}.
   */
  public RpcDataResponse(int status, AttributeReadHttpHeader headers, R data) {

    super();
    this.status = status;
    this.headers = headers;
    this.data = data;
  }

  @Override
  public final boolean isError() {

    return false;
  }

  @Override
  public int getStatus() {

    return this.status;
  }

  @Override
  public String getHeader(String name) {

    return this.headers.getHeader(name);
  }

  /**
   * @return the data received. May be {@code null} if no data is transmitted (e.g. if {@link #getStatus() status} is
   *         204 = no content)}.
   */
  public R getData() {

    return this.data;
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    sb.append(this.status);
    sb.append(':');
    sb.append(this.data);
    return sb.toString();
  }

}
