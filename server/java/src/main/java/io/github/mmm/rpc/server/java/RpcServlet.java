/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.mmm.rpc.server.RpcService;

/**
 * {@link HttpServlet} exposing {@link AbstractRpcService}.
 *
 * @since 1.0.0
 */
public class RpcServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private RpcService rpcService;

  /**
   * The constructor.
   *
   * @param rpcService the {@link RpcService} to expose.
   */
  public RpcServlet(RpcService rpcService) {

    super();
    this.rpcService = rpcService;
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String path = // request.getPathInfo();
        request.getRequestURI().substring(request.getContextPath().length());
    String contentType;
    contentType = request.getContentType();
    if (contentType == null) {
      contentType = request.getHeader("Accept");
    }
    if (contentType != null) {
      int index = contentType.indexOf(';');
      if (index > 0) {
        // strip stuff like charset (application/json; charset=UTF-8)
        contentType = contentType.substring(0, index);
      }
    }
    this.rpcService.handle(request.getMethod(), path, contentType, request.getReader(), response.getWriter());
  }

}
