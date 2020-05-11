/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import io.github.mmm.rpc.server.RpcService.StatusSender;

/**
 * Implementation of {@link StatusSender}.
 *
 * @since 1.0.0
 */
public class HttpStatusSender implements StatusSender {

  private final HttpServletResponse response;

  /**
   * The constructor.
   *
   * @param response the {@link HttpServletResponse}.
   */
  @SuppressWarnings("exports")
  public HttpStatusSender(HttpServletResponse response) {

    super();
    this.response = response;
  }

  @Override
  public void sendStatus(int status, String statusText) {

    try {
      if ((status >= 200) && (status < 300)) {
        this.response.setStatus(status);
      } else {
        if (statusText == null) {
          this.response.sendError(status);
        } else {
          this.response.sendError(status, statusText);
        }
      }
    } catch (IOException e) {
    }
  }

}
