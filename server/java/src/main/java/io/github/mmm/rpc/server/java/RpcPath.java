package io.github.mmm.rpc.server.java;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.github.mmm.rpc.request.RpcRequest;

/**
 * A parsed {@link io.github.mmm.rpc.request.RpcRequest#getPath() RPC request path}.
 *
 * @since 1.0.0
 */
public class RpcPath {

  private final String path;

  private final String prefix;

  private final String variable;

  private final RpcPath suffix;

  /**
   * The constructor.
   *
   * @param path the {@link #getPath() path}.
   */
  public RpcPath(String path) {

    super();
    if (!path.startsWith("/")) {
      path = "/" + path;
    }
    this.path = path;
    int start = path.indexOf('{');
    if (start < 0) {
      this.prefix = path;
      this.variable = null;
      this.suffix = null;
    } else {
      int end = path.indexOf('}');
      if (end < start + 2) {
        throw new IllegalArgumentException(path);
      }
      this.prefix = path.substring(0, start);
      this.variable = path.substring(start + 1, end);
      if ((path.length() - end) > 1) {
        this.suffix = new RpcPath(path.substring(end + 1));
      } else {
        this.suffix = null;
      }
    }
  }

  /**
   * @return the original path of this segment.
   * @see io.github.mmm.rpc.request.RpcRequest#getPath()
   */
  public String getPath() {

    return this.path;
  }

  /**
   * @return the static path prefix of this segment.
   */
  public String getPrefix() {

    return this.prefix;
  }

  /**
   * @return the variable name of this segment or {@code null} for no variable.
   */
  public String getVariable() {

    return this.variable;
  }

  /**
   * @return the next {@link RpcPath} segment to append as suffix or {@code null} if this is the last segment.
   */
  public RpcPath getSuffix() {

    return this.suffix;
  }

  /**
   * @return {@code true} if this {@link RpcPath} is static and has no {@link #getVariable() variable}.
   */
  public boolean isStatic() {

    return (this.variable == null) && (this.suffix == null);
  }

  /**
   * @param requestPath the actual request path to match.
   * @return the {@link Map} of path variables or {@code null} if the given {@code path} does not match.
   */
  public Map<String, String> matchPath(String requestPath) {

    if ((this.variable == null) && (this.suffix == null)) {
      if (this.prefix.equals(requestPath)) {
        return Collections.emptyMap();
      } else {
        return null;
      }
    } else if (!requestPath.startsWith(this.prefix)) {
      return null;
    }
    return matchPath(requestPath, this.prefix.length());
  }

  private Map<String, String> matchPath(String requestPath, int offset) {

    Map<String, String> map = null;
    String value = null;
    if (this.suffix != null) {
      while (true) {
        int index = requestPath.indexOf(this.suffix.prefix, offset);
        if (index < 0) {
          return null;
        }
        offset = index + this.suffix.prefix.length();
        map = this.suffix.matchPath(requestPath, offset);
        if (map != null) {
          value = requestPath.substring(index, offset);
          break;
        }
      }
    }
    if (map == null) {
      map = new HashMap<>();
    }
    if (this.variable != null) {
      if (value == null) {
        value = requestPath.substring(offset);
      }
      map.put(this.variable, value);
    }
    return map;
  }

  /**
   * @param request the {@link RpcRequest} to {@link RpcRequest#getPathVariable(String) resolve potential variables}.
   * @return the resulting path.
   */
  public String buildPath(RpcRequest<?> request) {

    StringBuilder pathBuilder = new StringBuilder();
    buildPath(pathBuilder, request);
    return pathBuilder.toString();
  }

  /**
   * @param pathBuilder the {@link StringBuilder} to build the path.
   * @param request the {@link RpcRequest} to {@link RpcRequest#getPathVariable(String) resolve potential variables}.
   */
  public void buildPath(StringBuilder pathBuilder, RpcRequest<?> request) {

    pathBuilder.append(this.prefix);
    if (this.variable != null) {
      String value = request.getPathVariable(this.variable);
      if (value == null) {
        throw new IllegalStateException(
            "Variable '" + this.variable + "' could not be resolved by RPC request " + request);
      }
      pathBuilder.append(value);
    }
    if (this.suffix != null) {
      this.suffix.buildPath(pathBuilder, request);
    }
  }

  @Override
  public String toString() {

    return this.path;
  }

}
