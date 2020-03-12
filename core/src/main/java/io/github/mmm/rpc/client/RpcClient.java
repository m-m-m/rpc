/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client;

import java.util.function.Consumer;

import io.github.mmm.marshall.StructuredFormatFactory;
import io.github.mmm.marshall.StructuredFormatProvider;
import io.github.mmm.rpc.request.RpcRequest;

/**
 * Interface to call a {@link RpcRequest} from the client.
 *
 * @since 1.0.0
 */
public interface RpcClient {

  /**
   * @return the default {@link Consumer} to {@link Consumer#accept(Object) handle} {@link Throwable errors}. A failure
   *         {@link Consumer} is {@link Consumer#accept(Object) invoked} asynchronously on failure with the
   *         {@link Throwable} that occurred when {@link io.github.mmm.rpc.server.RpcHandler#handle(RpcRequest)
   *         handling} the {@link RpcRequest}.
   */
  Consumer<Throwable> getDefaultFailureConsumer();

  /**
   * ATTENTION: This is a global setting that will influence your application. Only use this method from bootstrapping
   * code setting up your application but never call this from a (reusable) module or library called after
   * bootstrapping.
   *
   * @param defaultFailureConsumer the new value of {@link #getDefaultFailureConsumer()}.
   */
  void setDefaultFailureConsumer(Consumer<Throwable> defaultFailureConsumer);

  /**
   * @return the default {@link StructuredFormatProvider#getName() format} used for marshalling and unmarshaling the
   *         data (request and response).
   */
  default String getDefaultFormat() {

    return StructuredFormatFactory.NAME_JSON;
  }

  /**
   * This method invokes the given {@link RpcRequest}.
   *
   * @param <R> type of the result.
   * @param request is the {@link RpcRequest} to invoke.
   * @param successConsumer is the {@link Consumer} that is asynchronously {@link Consumer#accept(Object) invoked} on
   *        success with when the response of the invoked {@link RpcRequest}. {@link java.lang.reflect.Method} has been
   *        received.
   * @param failureConsumer is the explicit {@link #getDefaultFailureConsumer() failure consumer}.
   * @param format the explicit {@link #getDefaultFormat() data format}.
   */
  <R> void call(RpcRequest<R> request, Consumer<R> successConsumer, Consumer<Throwable> failureConsumer, String format);

  /**
   * Same as {@link #call(RpcRequest, Consumer)} but using the default failure callback.
   *
   * @param <R> type of the result.
   * @param request is the {@link RpcRequest} to invoke.
   * @param successConsumer is the {@link Consumer} that is asynchronously {@link Consumer#accept(Object) invoked} on
   *        success with when the response of the invoked {@link RpcRequest}. {@link java.lang.reflect.Method} has been
   *        received.
   */
  default <R> void call(RpcRequest<R> request, Consumer<R> successConsumer) {

    call(request, successConsumer, getDefaultFailureConsumer(), getDefaultFormat());
  }

  /**
   * @param request the {@link RpcRequest}
   */
  default void call(RpcRequest<Void> request) {

    call(request, null, getDefaultFailureConsumer(), getDefaultFormat());
  }

}
