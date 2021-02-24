package io.github.mmm.rpc.server.java.impl;

import io.github.mmm.rpc.server.RpcHandler;
import io.github.mmm.rpc.server.java.RpcPath;

/**
 * Container for an {@link RpcHandler}.
 *
 * @since 1.0.0
 */
public class RpcHandlerContainer extends RpcHandlerRequest {

  private final RpcPath path;

  /**
   * The constructor.
   *
   * @param handler the {@link #getHandler() handler}.
   */
  public RpcHandlerContainer(RpcHandler<?, ?> handler) {

    super(handler, handler.createRequest());
    this.path = new RpcPath(this.request.getPath());
  }

  /**
   * @return the parsed {@link RpcPath}.
   */
  public RpcPath getPath() {

    return this.path;
  }

}
