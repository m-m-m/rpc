/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.java;

import java.net.http.HttpHeaders;

import io.github.mmm.rpc.response.AttributeReadHttpHeader;

/**
 *
 */
public class ResponseHeaders implements AttributeReadHttpHeader {

  private final HttpHeaders headers;

  /**
   * The constructor.
   *
   * @param headers the {@link HttpHeaders} to wrap.
   */
  @SuppressWarnings("exports")
  public ResponseHeaders(HttpHeaders headers) {

    super();
    this.headers = headers;
  }

  @Override
  public String getHeader(String name) {

    return this.headers.firstValue(name).orElse(null);
  }

}
