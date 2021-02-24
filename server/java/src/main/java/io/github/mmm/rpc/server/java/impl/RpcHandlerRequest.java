package io.github.mmm.rpc.server.java.impl;

import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.server.RpcHandler;

/**
 * Container for an {@link RpcHandler} together with a {@link RpcRequest}.
 *
 * @since 1.0.0
 */
public class RpcHandlerRequest {

  /** @see #getHandler() */
  protected final RpcHandler<?, ?> handler;

  /** @see #getRequest() */
  protected final RpcRequest<?> request;

  /**
   * The constructor.
   *
   * @param handler the {@link #getHandler() handler}.
   * @param request the {@link #getRequest() request}.
   */
  public RpcHandlerRequest(RpcHandler<?, ?> handler, RpcRequest<?> request) {

    super();
    this.handler = handler;
    this.request = request;
  }

  /**
   * @param <D> type of the {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data}.
   * @param <R> type of the specific {@link RpcRequest}.
   * @return the {@link RpcHandler}.
   */
  @SuppressWarnings("unchecked")
  public <D, R extends RpcRequest<D>> RpcHandler<D, R> getHandler() {

    return (RpcHandler<D, R>) this.handler;
  }

  /**
   * @param <D> type of the {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data}.
   * @return the {@link RpcRequest}. Will be a prototype in case of {@link RpcHandlerContainer}.
   */
  @SuppressWarnings("unchecked")
  public <D> RpcRequest<D> getRequest() {

    return (RpcRequest<D>) this.request;
  }

}
