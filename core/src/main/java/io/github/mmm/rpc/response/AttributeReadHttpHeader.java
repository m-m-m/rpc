/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.response;

/**
 * Interface to {@link #getHeader(String) read} HTTP headers.
 *
 * @since 1.0.0
 */
public interface AttributeReadHttpHeader {

  /**
   * @param name the name of the HTTP header.
   * @return the corresponding header value or {@code null} if no such header is available.
   */
  String getHeader(String name);

}
