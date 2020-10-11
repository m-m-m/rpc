/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.discovery;

import io.github.mmm.marshall.StructuredFormatProvider;
import io.github.mmm.rpc.request.RpcRequest;

/**
 * Abstract base implementation of {@link RpcServiceDiscovery}.
 *
 * @since 1.0.0
 */
public abstract class AbstractRpcServiceDiscovery implements RpcServiceDiscovery {

  /**
   * @param request the {@link RpcRequest}.
   * @param format the {@link StructuredFormatProvider#getId() format name} used for marshalling and unmarshaling the
   *        data (request and response).
   * @return the base URL to call excluding the {@link RpcRequest#getPath() request path}.
   */
  protected abstract String getBaseUrl(RpcRequest<?> request, String format);

  @Override
  public String getUrl(RpcRequest<?> request, String format) {

    String baseUrl = getBaseUrl(request, format);
    if (baseUrl == null) {
      throw new IllegalStateException("Service discovery failed for " + request);
    }
    if ((baseUrl == null) || !baseUrl.startsWith("http")) {
      throw new IllegalStateException("Service discovery resolved invalid URL '" + baseUrl + "' for " + request);
    }
    // prevent double slashes as servers might reject for security reasons
    boolean baseSlash = baseUrl.endsWith("/");
    String path = request.getPath();
    boolean pathSlash = path.startsWith("/");
    if (baseSlash && pathSlash) {
      return baseUrl + path.substring(1);
    } else if (!baseSlash && !pathSlash) {
      return baseUrl + "/" + path;
    } else {
      return baseUrl + path;
    }
  }

}
