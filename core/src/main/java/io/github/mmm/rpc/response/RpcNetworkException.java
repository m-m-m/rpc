/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.response;

import io.github.mmm.base.i18n.Localizable;

/**
 * {@link RpcException} thrown if a network error occurred. The HTTP server might not even be reached (host not found,
 * connection refused), or the communication failed (timeout, etc.).<br>
 * As no HTTP response was received, the {@link #getStatus() status code} will be {@code -1} and there are no
 * {@link #getHeader(String) headers} available.
 *
 * @since 1.0.0
 */
public final class RpcNetworkException extends RpcException {

  private static final long serialVersionUID = 1L;

  /** The {@link #getCode() code}. */
  public static final String CODE = "RpcNet";

  /**
   * The constructor.
   *
   * @param message the {@link #getNlsMessage() message}.
   * @param cause the optional network error {@link #getCause() cause}.
   */
  public RpcNetworkException(Localizable message, Throwable cause) {

    super(message, -1, null, cause, null);
  }

  @Override
  public String getCode() {

    return CODE;
  }

}
