/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java;

import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.marshall.StructuredFormatFactory;
import io.github.mmm.marshall.StructuredFormatProvider;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.server.RpcHandler;
import io.github.mmm.rpc.server.RpcService;

/**
 * Abstract base implementation of {@link RpcService}.
 */
public class AbstractRpcService implements RpcService {

  private final Map<Class<?>, RpcHandler<?, ?>> request2handlerMap;

  private final Map<String, RpcHandler<?, ?>> pathMethod2handlerMap;

  /**
   * The constructor.
   */
  @SuppressWarnings("rawtypes")
  protected AbstractRpcService() {

    super();
    this.request2handlerMap = new HashMap<>();
    this.pathMethod2handlerMap = new HashMap<>();
    ServiceLoader<RpcHandler> serviceLoader = ServiceLoader.load(RpcHandler.class);
    for (RpcHandler<?, ?> handler : serviceLoader) {
      register(handler);
    }
  }

  /**
   * @param handler the {@link RpcHandler} to register.
   */
  private void register(RpcHandler<?, ?> handler) {

    RpcRequest<?> request = handler.createRequest();
    Class<?> requestClass = request.getClass();
    RpcHandler<?, ?> duplicate = this.request2handlerMap.put(requestClass, handler);
    if (duplicate != null) {
      throw new DuplicateObjectException(handler, requestClass, duplicate);
    }
    String key = createPathMethod(request);
    duplicate = this.pathMethod2handlerMap.put(key, handler);
    if (duplicate != null) {
      throw new DuplicateObjectException(handler, requestClass, duplicate);
    }
  }

  private static String createPathMethod(RpcRequest<?> request) {

    return createPathMethod(request.getPath(), request.getMethod());
  }

  private static String createPathMethod(String path, String method) {

    return path + '@' + method;
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void handle(String method, String path, String format, Reader requestReader, Writer responseWriter) {

    String key = createPathMethod(path, method);
    RpcHandler handler = this.pathMethod2handlerMap.get(key);
    if (handler == null) {
      throw new ObjectNotFoundException(RpcHandler.class, key);
    }
    handle(handler, format, requestReader, responseWriter);
  }

  private <RSP, REQ extends RpcRequest<RSP>> void handle(RpcHandler<RSP, REQ> handler, String format,
      Reader requestReader, Writer responseWriter) {

    REQ request = handler.createRequest();
    StructuredFormatProvider provider = StructuredFormatFactory.get().getProvider(format);
    StructuredFormat formatBuilder = provider.create();
    StructuredReader reader = formatBuilder.reader(requestReader);
    request.getRequestMarshalling().read(reader);
    try {
      RSP response = handler.handle(request);
      Marshalling<RSP> marshalling = request.getResponseMarshalling();
      StructuredWriter writer = formatBuilder.writer(responseWriter);
      marshalling.writeObject(writer, response);
    } catch (Throwable t) {
      // TODO: write error response...
    }
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public <R> R handle(RpcRequest<R> request) {

    Class<?> requestClass = request.getClass();
    RpcHandler handler = this.request2handlerMap.get(requestClass);
    if (handler == null) {
      throw new ObjectNotFoundException(RpcHandler.class, requestClass);
    }
    return (R) handler.handle(request);
  }

}
