package io.github.mmm.rpc.server.java.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.server.RpcHandler;
import io.github.mmm.rpc.server.java.RpcPath;

/**
 * Map for efficient management of {@link io.github.mmm.rpc.server.RpcHandler}s as {@link RpcHandlerContainer}s and .
 *
 * @since 1.0.0
 */
public class RpcHandlerMap {

  private final Map<String, RpcPathHanlderMap> methodMap;

  /**
   * The constructor.
   */
  public RpcHandlerMap() {

    super();
    this.methodMap = new HashMap<>();
  }

  /**
   * @param method the {@link io.github.mmm.rpc.request.RpcRequest#getMethod() HTTP method}.
   * @param path the {@link io.github.mmm.rpc.request.RpcRequest#getPath() URL path}.
   * @return the {@link RpcHandlerRequest} or {@code null} if no matching {@link io.github.mmm.rpc.server.RpcHandler}
   *         could be found.
   */
  public RpcHandlerRequest get(String method, String path) {

    RpcPathHanlderMap pathMap = this.methodMap.get(method);
    if (pathMap == null) {
      return null;
    }
    return pathMap.get(path);
  }

  /**
   * @param container the {@link RpcHandlerContainer} to register.
   */
  public void add(RpcHandlerContainer container) {

    String method = container.getRequest().getMethod();
    RpcPathHanlderMap pathMap = this.methodMap.computeIfAbsent(method, x -> new RpcPathHanlderMap());
    pathMap.add(container);
  }

  private static class RpcPathHanlderMap {

    private final Map<String, RpcHandlerContainer> pathMap;

    private final List<RpcHandlerContainer> dynamicContainers;

    /**
     * The constructor.
     */
    public RpcPathHanlderMap() {

      super();
      this.pathMap = new HashMap<>();
      this.dynamicContainers = new ArrayList<>();
    }

    public void add(RpcHandlerContainer container) {

      RpcPath path = container.getPath();
      if (path.isStatic()) {
        this.pathMap.put(path.getPath(), container);
      } else {
        this.dynamicContainers.add(container);
      }
    }

    public RpcHandlerRequest get(String path) {

      RpcHandlerContainer container = this.pathMap.get(path);
      if (container != null) {
        return container;
      }
      for (RpcHandlerContainer candidate : this.dynamicContainers) {
        Map<String, String> variables = candidate.getPath().matchPath(path);
        if (variables != null) {
          RpcHandler<?, ?> handler = candidate.getHandler();
          RpcRequest<?> request = handler.createRequest();
          if (!candidate.getPath().isStatic()) {
            request.setPathVariables(variables);
          }
          return new RpcHandlerRequest(handler, request);
        }
      }
      return null;
    }

  }

}
