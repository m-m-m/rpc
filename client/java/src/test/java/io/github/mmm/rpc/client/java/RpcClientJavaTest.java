/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client.java;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.time.LocalDate;
import java.util.function.Consumer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import io.github.mmm.rpc.client.RpcClient;
import io.github.mmm.rpc.client.RpcInvocation;
import io.github.mmm.rpc.response.RpcDataResponse;
import io.github.mmm.rpc.response.RpcException;

/**
 * Test of {@link RpcClientJava}.
 */
public class RpcClientJavaTest extends Assertions {

  /**
   * Test {@link RpcClient} with {@link RpcInvocation#sendAsync(Consumer) asynchronous} communication.
   */
  @Test
  public void testAsync() {

    WireMockServer server = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
    server.start();
    String baseUrl = server.baseUrl() + "/services/rest/";
    server.stubFor(post(urlEqualTo("/services/rest/test/path")).withBasicAuth("wiki", "pedia")
        .withRequestBody(equalToJson("{\"Id\":4711}"))
        .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
            .withBody("{\"Name\":\"John Doe\",\"Birthday\":\"1999-12-31\"}")));

    RpcClient rpcClient = RpcClient.get();
    assertThat(rpcClient).isInstanceOf(RpcClientJava.class);
    ((RpcClientJava) rpcClient).setServiceDiscovery(new TestServiceDiscovery(baseUrl));
    TestRequest request = new TestRequest();
    request.Id.set(4711L);
    ErrorHandler errorHandler = new ErrorHandler();
    ResponseHandler responseHandler = new ResponseHandler();
    RpcInvocation<TestResult> invocation = rpcClient.call(request);
    invocation.header(RpcInvocation.HEADER_AUTHORIZATION, "Basic d2lraTpwZWRpYQ==").errorHandler(errorHandler)
        .sendAsync(responseHandler);
    while ((responseHandler.response == null) && (errorHandler.error == null)) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        fail(e.getMessage(), e);
      }
    }
    server.stop();
    if (errorHandler.error != null) {
      throw errorHandler.error;
    }
    assertThat(responseHandler.response.getStatus()).isEqualTo(200);
    assertThat(responseHandler.response.getData().Name.get()).isEqualTo("John Doe");
    assertThat(responseHandler.response.getData().Birthday.get()).isEqualTo(LocalDate.parse("1999-12-31"));
  }

  private static class ErrorHandler implements Consumer<RpcException> {

    private RpcException error;

    @Override
    public void accept(RpcException e) {

      this.error = e;
    }
  }

  private static class ResponseHandler implements Consumer<RpcDataResponse<TestResult>> {

    private RpcDataResponse<TestResult> response;

    @Override
    public void accept(RpcDataResponse<TestResult> resp) {

      this.response = resp;
    }
  }

}
