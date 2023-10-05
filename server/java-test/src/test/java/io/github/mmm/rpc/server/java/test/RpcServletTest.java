/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.github.mmm.rpc.client.AbstractRpcClient;
import io.github.mmm.rpc.client.RpcClient;
import io.github.mmm.rpc.client.RpcInvocation;
import io.github.mmm.rpc.response.RpcDataResponse;
import io.github.mmm.rpc.response.RpcResponseException;
import io.github.mmm.rpc.server.java.RpcServlet;
import io.github.mmm.rpc.server.java.test.app.TestApp;
import io.github.mmm.rpc.server.java.test.app.TestRequest;
import io.github.mmm.rpc.server.java.test.app.TestResult;
import io.github.mmm.rpc.server.java.test.app.TestRpcServlet;

/**
 * Test of {@link RpcServlet} via {@link TestRpcServlet} with spring-boot.
 */
@SpringBootTest(classes = TestApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class RpcServletTest extends Assertions {

  @LocalServerPort
  private int port;

  /**
   * Use {@link RpcClient} to invoke the servlet that has been launched by spring-boot as a background server.
   */
  @Test
  public void testService() {

    RpcClient rpcClient = RpcClient.get();
    assertThat(rpcClient).isInstanceOf(AbstractRpcClient.class);
    ((AbstractRpcClient) rpcClient).setServiceDiscovery("http://localhost:" + this.port + "/services/rest/");
    TestRequest request = new TestRequest();
    request.Id.set(4711L);
    RpcInvocation<TestResult> invocation = rpcClient.call(request);
    RpcDataResponse<TestResult> response = invocation.headerAuthBasic("rpc", "mmm").sendSync();
    assertThat(response.getStatus()).isEqualTo(200);
    TestResult data = response.getData();
    assertThat(data).isNotNull();
    assertThat(data.Name.get()).isEqualTo("John Doe");
    assertThat(data.Age.get()).isEqualTo(42);
  }

  /**
   * Test with wrong password.
   */
  @Test
  public void testInvalidCredentials() {

    RpcClient rpcClient = RpcClient.get();
    assertThat(rpcClient).isInstanceOf(AbstractRpcClient.class);
    ((AbstractRpcClient) rpcClient).setServiceDiscovery("http://localhost:" + this.port + "/services/rest/");
    TestRequest request = new TestRequest();
    request.Id.set(4711L);
    RpcInvocation<TestResult> invocation = rpcClient.call(request);
    try {
      // RpcDataResponse<TestResult> response =
      invocation.headerAuthBasic("rpc", "invalid-password").sendSync();
      failBecauseExceptionWasNotThrown(RpcResponseException.class);
    } catch (RpcResponseException e) {
      assertThat(e.getStatus()).isEqualTo(401);
      assertThat(e.getCode()).isEqualTo(RpcResponseException.CODE_DEFAULT);
      assertThat(e.getNlsMessage().getMessage()).isEqualTo("401:Unauthorized");
    }
  }

}
