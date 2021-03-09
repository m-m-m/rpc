/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.rpc.server.HttpRequestReader;

/**
 * Implementation of {@link HttpRequestReader} wrapping {@link HttpServletRequest}.
 *
 * @since 1.0.0
 */
public class HttpServletRequestReader implements HttpRequestReader {

  private final HttpServletRequest request;

  private String path;

  private String mimeType;

  /**
   * The constructor.
   *
   * @param request the {@link HttpServletRequest}.
   */
  @SuppressWarnings("exports")
  public HttpServletRequestReader(HttpServletRequest request) {

    super();
    this.request = request;
  }

  @Override
  public InputStream getInputStream() {

    try {
      return this.request.getInputStream();
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public Reader getReader() {

    try {
      return this.request.getReader();
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  @Override
  public String getPath() {

    if (this.path == null) {
      this.path = this.request.getPathInfo();
      if (this.path == null) {
        this.path = this.request.getRequestURI().substring(this.request.getContextPath().length());
      }
    }
    return this.path;
  }

  @Override
  public String getMethod() {

    return this.request.getMethod();
  }

  @Override
  public String getMimeType() {

    if (this.mimeType == null) {
      String contentType;
      contentType = this.request.getContentType();
      if (contentType == null) {
        contentType = this.request.getHeader("Accept");
      }
      if (contentType != null) {
        int index = contentType.indexOf(';');
        if (index > 0) {
          // strip stuff like charset (application/json; charset=UTF-8)
          contentType = contentType.substring(0, index);
        }
      }
      this.mimeType = contentType;
    }
    return this.mimeType;
  }

  @Override
  public String getHeader(String name) {

    return this.request.getHeader(name);
  }

}
