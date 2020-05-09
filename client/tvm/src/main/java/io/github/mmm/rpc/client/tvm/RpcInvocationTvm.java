/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.tvm;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.teavm.jso.ajax.XMLHttpRequest;

import io.github.mmm.marshall.MarshallingObject;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.rpc.client.AbstractRpcInvocation;
import io.github.mmm.rpc.client.RpcInvocation;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.request.RpcServiceDiscovery;
import io.github.mmm.rpc.response.AttributeReadHttpHeader;
import io.github.mmm.rpc.response.RpcDataResponse;
import io.github.mmm.rpc.response.RpcException;

/**
 * Implementation of {@link RpcInvocation} for TeaVM based on {@link XMLHttpRequest}.
 *
 * @param <R> type of the {@link RpcDataResponse#getData() response data}.
 * @since 1.0.0
 */
public class RpcInvocationTvm<R> extends AbstractRpcInvocation<R> implements AttributeReadHttpHeader {

  private final XMLHttpRequest xhr;

  /**
   * The constructor.
   *
   * @param request the {@link RpcRequest} to send.
   * @param serviceDiscovery the {@link RpcServiceDiscovery} to create the URL.
   * @param format the default {@link StructuredFormat}.
   * @param errorHandler the default {@link #errorHandler(Consumer) error handler}.
   * @param headers the default {@link #headers(Map) headers}.
   */
  public RpcInvocationTvm(RpcRequest<R> request, RpcServiceDiscovery serviceDiscovery, StructuredFormat format,
      Consumer<RpcException> errorHandler, Map<String, String> headers) {

    super(request, serviceDiscovery, format, errorHandler, headers);
    this.xhr = XMLHttpRequest.create();
  }

  @Override
  public String getHeader(String name) {

    return this.xhr.getResponseHeader(name);
  }

  @Override
  protected void prepareSend() {

    super.prepareSend();
    for (Entry<String, String> entry : this.headers.entrySet()) {
      this.xhr.setRequestHeader(entry.getKey(), entry.getValue());
    }
    boolean xml = StructuredFormat.ID_XML.equals(this.format.getId());
    if (xml) {
      this.xhr.setResponseType("document");
    }
  }

  @Override
  public void sendAsync(Consumer<RpcDataResponse<R>> responseHandler) {

    prepareSend();
    this.xhr.open(this.request.getMethod(), this.url, true);
    this.xhr.setOnReadyStateChange(() -> {
      if (this.xhr.getReadyState() == XMLHttpRequest.DONE) {
        createResponse(true, responseHandler);
      }
    });
    send();
  }

  @Override
  public RpcDataResponse<R> sendSync() {

    prepareSend();
    this.xhr.open(this.request.getMethod(), this.url, true);
    send();
    return createResponse(false, null);
  }

  private RpcDataResponse<R> createResponse(boolean async, Consumer<RpcDataResponse<R>> responseHandler) {

    int status = this.xhr.getStatus();
    Object payload = null;
    if (async && (responseHandler == null) && isSuccess(status)) {
      return null;
    }
    String responseType = this.xhr.getResponseType();
    if ("text".equals(responseType)) {
      String responseText = this.xhr.getResponseText();
      if ((responseText != null) && !responseType.isEmpty()) {
        payload = responseText;
      }
    } else if ("document".equals(responseType)) {
      payload = this.xhr.getResponseXML();
    } else {
      payload = this.xhr.getResponse();
    }
    return createResponse(status, this.xhr.getStatusText(), payload, this, async, responseHandler);
  }

  private void send() {

    MarshallingObject requestMarshalling = this.request.getRequestMarshalling();
    if (requestMarshalling == null) {
      this.xhr.send();
    } else {
      String data = this.format.write(requestMarshalling);
      this.xhr.send(data);
    }
  }

}
