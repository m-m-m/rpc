/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java;

import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.ApplicationException;
import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.marshall.MarshallableObject;
import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.marshall.StructuredFormatFactory;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.nls.exception.TechnicalErrorUserException;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.response.RpcErrorData;
import io.github.mmm.rpc.server.RpcHandler;
import io.github.mmm.rpc.server.RpcService;

/**
 * Abstract base implementation of {@link RpcService}.
 */
public class AbstractRpcService implements RpcService {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractRpcService.class);

  private final Map<Class<?>, RpcHandler<?, ?>> request2handlerMap;

  private final Map<String, RpcHandler<?, ?>> pathMethod2handlerMap;

  /**
   * The constructor.
   */
  protected AbstractRpcService() {

    super();
    this.request2handlerMap = new HashMap<>();
    this.pathMethod2handlerMap = new HashMap<>();
  }

  /**
   * Initializes this service via Service-Loader.
   */
  @SuppressWarnings("rawtypes")
  protected void init() {

    if (this.request2handlerMap.isEmpty()) {
      ServiceLoader<RpcHandler> serviceLoader = ServiceLoader.load(RpcHandler.class);
      for (RpcHandler<?, ?> handler : serviceLoader) {
        register(handler);
      }
    }
  }

  /**
   * @param handler the {@link RpcHandler} to register.
   */
  protected void register(RpcHandler<?, ?> handler) {

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

    if (path.startsWith("/")) {
      return path + '@' + method;
    }
    return "/" + path + '@' + method;
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public int handle(String method, String path, String format, Reader requestReader, Writer responseWriter,
      StatusSender statusSender) {

    RpcHandler handler = getHandler(method, path);
    return handle(handler, format, requestReader, responseWriter, statusSender);
  }

  @SuppressWarnings({ "rawtypes" })
  private RpcHandler getHandler(String method, String path) {

    init();
    String key = createPathMethod(path, method);
    RpcHandler handler = this.pathMethod2handlerMap.get(key);
    if (handler == null) {
      throw new ObjectNotFoundException(RpcHandler.class.getSimpleName(), key);
    }
    return handler;
  }

  private <D, R extends RpcRequest<D>> int handle(RpcHandler<D, R> handler, String format, Reader requestReader,
      Writer responseWriter, StatusSender statusSender) {

    R request = handler.createRequest();
    StructuredFormat structuredFormat = StructuredFormatFactory.get().create(format);
    StructuredReader reader = structuredFormat.reader(requestReader);
    int status = 200;
    try (StructuredWriter writer = structuredFormat.writer(responseWriter)) {
      request.getRequestMarshalling().read(reader);
      D response = handler.handle(request);
      if (response == null) {
        status = 204;
        statusSender.sendStatus(status);
      } else {
        if (response instanceof MarshallableObject) {
          ((MarshallableObject) response).write(writer);
        } else {
          Marshalling<D> marshalling = request.getResponseMarshalling();
          if (marshalling == null) {
            throw new ObjectNotFoundException("responseMarshalling", request.getClass().getName());
          }
          marshalling.writeObject(writer, response);
        }
      }
    } catch (Throwable t) {
      ApplicationException userError = TechnicalErrorUserException.convert(t);
      LOG.error("RpcHandler {} failed:", handler.getClass().getName(), userError);
      RpcErrorData errorData = RpcErrorData.of(userError);
      String error = structuredFormat.write(errorData);
      status = 500;
      statusSender.sendStatus(status, error);
    }
    return status;
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public <R> R handle(RpcRequest<R> request) {

    Class<? extends RpcRequest> requestClass = request.getClass();
    RpcHandler handler = getHandler(requestClass);
    return (R) handler.handle(request);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private <R, C extends RpcRequest<R>> RpcHandler<R, C> getHandler(Class<C> requestClass) {

    init();
    RpcHandler handler = this.request2handlerMap.get(requestClass);
    if (handler == null) {
      throw new ObjectNotFoundException(RpcHandler.class.getSimpleName(), requestClass);
    }
    return handler;
  }

}
