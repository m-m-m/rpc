/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.server.java.test.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * {@link WebSecurityConfigurerAdapter} for {@link TestApp}.
 */
@Configuration
@SuppressWarnings("deprecation")
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.httpBasic().realmName("mmm-rpc") //
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
        .and().csrf().disable(); //
    // .authorizeRequests().antMatchers("/error/**").permitAll().anyRequest().authenticated();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.inMemoryAuthentication().withUser("rpc").password("mmm").roles("USER");
  }

  /** @return for testing use {@code NoOpPasswordEncoder}. */
  @Bean
  public NoOpPasswordEncoder passwordEncoder() {

    return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
  }
}
