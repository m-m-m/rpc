/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.response;

import java.util.UUID;

import io.github.mmm.base.exception.ApplicationException;
import io.github.mmm.base.i18n.Localizable;

/**
 * Exception thrown in case of an error during {@link io.github.mmm.rpc.request.RpcRequest RPC communication}. This can
 * have one of the following reasons:
 * <ul>
 * <li>{@link RpcNetworkException} - a network error occurred.</li>
 * <li>{@link RpcResponseException} - response received but indicates an error via {@link #getStatus() status code}
 * (e.g. 500 - internal server error).</li>
 * <li>{@link RpcParseException} - response received successfully but payload could not be parsed (unmarshalling
 * failed).</li>
 * </ul>
 *
 * @since 1.0.0
 */
public abstract class RpcException extends ApplicationException implements RpcResponse {

  private static final long serialVersionUID = 1L;

  private final int status;

  private final AttributeReadHttpHeader headers;

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   * @param status the {@link #getStatus() status code}.
   * @param headers the {@link #getHeader(String) headers}.
   * @param cause is the {@link #getCause() cause} of this exception. May be <code>null</code>.
   * @param uuid the explicit {@link #getUuid() UUID} or <code>null</code> to initialize by default (from given
   *        {@link Throwable} or as new {@link UUID}).
   */
  public RpcException(Localizable message, int status, AttributeReadHttpHeader headers, Throwable cause, UUID uuid) {

    super(message, cause, uuid);
    this.status = status;
    this.headers = headers;
  }

  @Override
  public boolean isError() {

    return true;
  }

  @Override
  public boolean isForUser() {

    return true;
  }

  @Override
  public int getStatus() {

    return this.status;
  }

  @Override
  public String getHeader(String name) {

    if (this.headers == null) {
      return null;
    }
    return this.headers.getHeader(name);
  }

}
