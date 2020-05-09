/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.response;

import java.util.UUID;

import io.github.mmm.base.i18n.Localizable;

/**
 * {@link RpcException} thrown if the response was received but indicates an error on the HTTP server via
 * {@link #getStatus() status code}. Common examples are:
 * <ul>
 * <li>403 - access denied</li>
 * <li>404 - not found</li>
 * <li>500 - internal server error</li>
 * </ul>
 *
 * @since 1.0.0
 */
public final class RpcResponseException extends RpcException {

  private static final long serialVersionUID = 1L;

  private final boolean technical;

  private final String code;

  /**
   * The constructor.
   *
   * @param message the {@link #getMessage() message} describing the problem briefly.
   * @param status the {@link #getStatus() status code}.
   * @param headers the {@link #getHeader(String) headers}.
   * @param technical the {@link #isTechnical() technical} flag.
   * @param code the {@link #getCode() code}.
   * @param uuid the explicit {@link #getUuid() UUID} or <code>null</code> to initialize by default (from given
   *        {@link Throwable} or as new {@link UUID}).
   */
  public RpcResponseException(Localizable message, int status, AttributeReadHttpHeader headers, boolean technical,
      String code, UUID uuid) {

    super(message, status, headers, null, uuid);
    this.technical = technical;
    this.code = code;
  }

  @Override
  public String getCode() {

    return this.code;
  }

  @Override
  public boolean isTechnical() {

    return this.technical;
  }

}
