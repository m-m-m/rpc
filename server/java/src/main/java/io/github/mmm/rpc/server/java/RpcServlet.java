/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java;

import java.io.IOException;

import io.github.mmm.rpc.server.RpcService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    this.rpcService.handle(new HttpServletRequestReader(request), new HttpServletResponseWriter(response));
  }

}
