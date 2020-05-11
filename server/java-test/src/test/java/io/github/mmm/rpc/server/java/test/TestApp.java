/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 *
 */
@SpringBootApplication
public class TestApp {
  @Bean
  public ServletRegistrationBean<TestRpcServlet> exampleServletBean(TestRpcServlet servlet) {

    ServletRegistrationBean<TestRpcServlet> bean = new ServletRegistrationBean<>(servlet, "/services/rest/*");
    bean.setLoadOnStartup(1);
    return bean;
  }
}
