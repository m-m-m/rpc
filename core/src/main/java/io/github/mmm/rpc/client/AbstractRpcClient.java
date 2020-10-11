/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import io.github.mmm.marshall.JsonFormat;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.rpc.discovery.RpcServiceDiscovery;
import io.github.mmm.rpc.discovery.StaticServiceDiscovery;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.response.RpcException;

/**
 * Abstract base implementation of {@link RpcClient}.
 *
 * @since 1.0.0
 */
public abstract class AbstractRpcClient implements RpcClient {

  private RpcServiceDiscovery serviceDiscovery;

  private Consumer<RpcException> defaultErrorHandler;

  private StructuredFormat defaultFormat;

  private Map<String, String> defaultHeaders;

  /**
   * The constructor.
   */
  public AbstractRpcClient() {

    super();
    this.defaultErrorHandler = error -> error.printStackTrace(); // TODO use logger
  }

  /**
   * @return the {@link StructuredFormat} to use by default for marshalling and unmarshaling the data (request and
   *         response).
   * @see RpcInvocation#format(StructuredFormat)
   */
  public StructuredFormat getDefaultFormat() {

    if (this.defaultFormat == null) {
      this.defaultFormat = JsonFormat.of();
    }
    return this.defaultFormat;
  }

  /**
   * ATTENTION: This is a global setting that will influence your application. Only use this method from bootstrapping
   * code setting up your application but never call this from a (reusable) module or library called after
   * bootstrapping.
   *
   * @param format the new {@link #getDefaultFormat() default format}.
   */
  public void setDefaultFormat(StructuredFormat format) {

    Objects.requireNonNull(format, "format");
    this.defaultFormat = format;
  }

  /**
   * @return the default {@link Consumer} to {@link Consumer#accept(Object) handle} {@link Throwable errors}. A failure
   *         {@link Consumer} is {@link Consumer#accept(Object) invoked} asynchronously on failure with the
   *         {@link Throwable} that occurred when {@link io.github.mmm.rpc.server.RpcHandler#handle(RpcRequest)
   *         handling} the {@link RpcRequest}.
   */
  public Consumer<RpcException> getDefaultErrorHandler() {

    return this.defaultErrorHandler;
  }

  /**
   * ATTENTION: This is a global setting that will influence your application. Only use this method from bootstrapping
   * code setting up your application but never call this from a (reusable) module or library called after
   * bootstrapping.
   *
   * @param errorHandler the new {@link #getDefaultErrorHandler() default error handler}.
   */
  public void setDefaultErrorHandler(Consumer<RpcException> errorHandler) {

    Objects.requireNonNull(errorHandler);
    this.defaultErrorHandler = errorHandler;
  }

  /**
   * @param serviceDiscovery new value of {@link #getServiceDiscovery()}.
   */
  public void setServiceDiscovery(RpcServiceDiscovery serviceDiscovery) {

    this.serviceDiscovery = serviceDiscovery;
  }

  /**
   * @param baseUrl the base URL to use for all services.
   */
  public void setServiceDiscovery(String baseUrl) {

    setServiceDiscovery(new StaticServiceDiscovery(baseUrl));
  }

  /**
   * @return the {@link RpcServiceDiscovery}.
   */
  protected RpcServiceDiscovery getServiceDiscovery() {

    if (this.serviceDiscovery == null) {
      this.serviceDiscovery = RpcServiceDiscovery.get();
    }
    return this.serviceDiscovery;
  }

  /**
   * @return a new {@link Map} initialized with the default HTTP headers.
   */
  protected Map<String, String> createDefaultHeaders() {

    if (this.defaultHeaders == null) {
      return new HashMap<>();
    }
    return new HashMap<>(this.defaultHeaders);
  }

  /**
   * Configures a default HTTP header to send with the every request (e.g. for authentication).
   *
   * @param key the name of the HTTP header.
   * @param value the value of the HTTP header.
   * @see RpcInvocation#header(String, String)
   */
  public void setDefaultHeader(String key, String value) {

    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    if (this.defaultHeaders == null) {
      this.defaultHeaders = new HashMap<>();
    }
    this.defaultHeaders.put(key, value);
  }

}
