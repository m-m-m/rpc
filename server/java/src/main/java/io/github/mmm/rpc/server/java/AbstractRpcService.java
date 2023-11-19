/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java;

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
import io.github.mmm.marshall.StandardFormat;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.marshall.StructuredFormatFactory;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredTextFormat;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.nls.exception.TechnicalErrorUserException;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.response.RpcErrorData;
import io.github.mmm.rpc.server.HttpRequestReader;
import io.github.mmm.rpc.server.HttpResponseWriter;
import io.github.mmm.rpc.server.RpcHandler;
import io.github.mmm.rpc.server.RpcService;
import io.github.mmm.rpc.server.java.impl.RpcHandlerContainer;
import io.github.mmm.rpc.server.java.impl.RpcHandlerMap;
import io.github.mmm.rpc.server.java.impl.RpcHandlerRequest;

/**
 * Abstract base implementation of {@link RpcService}.
 */
public class AbstractRpcService implements RpcService {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractRpcService.class);

  private final Map<Class<?>, RpcHandler<?, ?>> request2handlerMap;

  private final RpcHandlerMap handlerMap;

  /**
   * The constructor.
   */
  protected AbstractRpcService() {

    super();
    this.request2handlerMap = new HashMap<>();
    this.handlerMap = new RpcHandlerMap();
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

    RpcHandlerContainer container = new RpcHandlerContainer(handler);
    RpcRequest<?> request = container.getRequest();
    Class<?> requestClass = request.getClass();
    RpcHandler<?, ?> duplicate = this.request2handlerMap.put(requestClass, handler);
    if (duplicate != null) {
      throw new DuplicateObjectException(handler, requestClass, duplicate);
    }
    this.handlerMap.add(container);
  }

  @Override
  public void handle(HttpRequestReader request, HttpResponseWriter response) {

    RpcHandlerRequest handlerRequest = getHandlerRequest(request);
    handle(handlerRequest.getHandler(), handlerRequest.getRequest(), request, response);
  }

  private RpcHandlerRequest getHandlerRequest(HttpRequestReader request) {

    init();
    String method = request.getMethod();
    String path = request.getPath();
    RpcHandlerRequest handlerRequest = this.handlerMap.get(method, path);
    if (handlerRequest == null) {
      throw new ObjectNotFoundException(RpcHandler.class.getSimpleName(), createPathMethod(path, method));
    }
    return handlerRequest;
  }

  private <D, R extends RpcRequest<D>> void handle(RpcHandler<D, R> handler, R request, HttpRequestReader requestReader,
      HttpResponseWriter responseWriter) {

    String mimeType = requestReader.getMimeType();
    StructuredFormat structuredFormat = StructuredFormatFactory.get().create(mimeType);
    StructuredReader reader;
    if (structuredFormat.isText()) {
      StructuredTextFormat textFormat = (StructuredTextFormat) structuredFormat;
      reader = textFormat.reader(requestReader.getReader());
    } else {
      assert (structuredFormat.isBinary());
      reader = structuredFormat.reader(requestReader.getInputStream());
    }
    int status = 200;
    try (StructuredWriter writer = createWriter(structuredFormat, responseWriter)) {
      request.getRequestMarshalling().read(reader);
      D response = handler.handle(request);
      if (response == null) {
        status = 204;
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
      responseWriter.setStatus(status);
    } catch (Throwable t) {
      ApplicationException userError = TechnicalErrorUserException.convert(t);
      LOG.error("RpcHandler {} failed:", handler.getClass().getName(), userError);
      RpcErrorData errorData = RpcErrorData.of(userError);
      StructuredTextFormat textFormat;
      if (structuredFormat.isText()) {
        textFormat = (StructuredTextFormat) structuredFormat;
      } else {
        textFormat = StandardFormat.json();
      }
      String error = textFormat.write(errorData);
      status = 500;
      responseWriter.setStatus(status, error);
    }
  }

  private StructuredWriter createWriter(StructuredFormat structuredFormat, HttpResponseWriter responseWriter) {

    if (structuredFormat.isText()) {
      StructuredTextFormat textFormat = (StructuredTextFormat) structuredFormat;
      return textFormat.writer(responseWriter.getWriter());
    }
    assert (structuredFormat.isBinary());
    structuredFormat.writer(responseWriter.getOutputStream());
    return null;
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

  private static String createPathMethod(String path, String method) {

    if (path.startsWith("/")) {
      return path + '@' + method;
    }
    return "/" + path + '@' + method;
  }

}
