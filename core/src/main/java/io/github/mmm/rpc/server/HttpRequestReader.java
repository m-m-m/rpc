package io.github.mmm.rpc.server;

import java.io.Reader;

import io.github.mmm.marshall.StructuredFormatProvider;
import io.github.mmm.rpc.request.HttpMethod;

/**
 * Interface to read from an HTTP request. It abstracts from {@code java.net.http.HttpRequest} or
 * {@code javax.servlet.http.HttpServletRequest}).
 */
public interface HttpRequestReader extends HttpMethod {

  /**
   * @return the mime type as raw content-type compatible with {@link StructuredFormatProvider#getId() format name}.
   */
  String getMimeType();

  /**
   * @return the path of the HTTP request relative to the context-root.
   */
  String getPath();

  /**
   * @return the {@link Reader} to read the request body.
   */
  Reader getReader();

  /**
   * @param name the name of the HTTP header.
   * @return the value of the HTTP header.
   */
  String getHeader(String name);
}