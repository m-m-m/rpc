/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.response;

/**
 * {@link RpcException} if the transmission succeeded but the response could not be parsed (unmarshalling failed). In
 * this case the {@link #getStatus() status code} may indicate a success (e.g. 200 - OK).
 *
 * @since 1.0.0
 */
public final class RpcParseException extends RpcException {

  /** The {@link #getCode() code}. */
  public static final String CODE = "RpcParse";

  private static final long serialVersionUID = 1L;

  /**
   * @param error the unmarshalling exception.
   * @param status the HTTP status.
   * @param responseHeaders the HTTP response headers.
   */
  public RpcParseException(Throwable error, int status, AttributeReadHttpHeader responseHeaders) {

    super(getNlsMessage(error), status, responseHeaders, error, null);
  }

  @Override
  public String getCode() {

    return CODE;
  }

}
