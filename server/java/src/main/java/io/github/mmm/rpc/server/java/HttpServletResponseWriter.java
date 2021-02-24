/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.rpc.server.HttpResponseWriter;

/**
 * Implementation of {@link HttpResponseWriter} wrapping {@link HttpServletResponse}.
 *
 * @since 1.0.0
 */
public class HttpServletResponseWriter implements HttpResponseWriter {

  private static final Logger LOG = LoggerFactory.getLogger(HttpServletResponseWriter.class);

  private final HttpServletResponse response;

  private Integer status;

  /**
   * The constructor.
   *
   * @param response the {@link HttpServletResponse}.
   */
  @SuppressWarnings("exports")
  public HttpServletResponseWriter(HttpServletResponse response) {

    super();
    this.response = response;
  }

  @Override
  public Writer getWriter() {

    try {
      return this.response.getWriter();
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public Integer getStatus() {

    return this.status;
  }

  @Override
  public void setStatus(int status, String statusText) {

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
      this.status = Integer.valueOf(status);
    } catch (IOException e) {
      LOG.warn("Exception occurred whilst sending HTTP status {}", status, e);
    }
  }

  @Override
  public void setHeader(String name, String value) {

    this.response.setHeader(name, value);
  }

}
