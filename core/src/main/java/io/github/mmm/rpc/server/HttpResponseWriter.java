package io.github.mmm.rpc.server;

import java.io.OutputStream;
import java.io.Writer;

/**
 * Interface to write the HTTP response. It abstracts from {@code java.net.http.HttpResponse} or
 * {@code javax.servlet.http.HttpServletResponse}.
 */
public interface HttpResponseWriter {

  /**
   * @return the {@link OutputStream} to write the response body.
   */
  OutputStream getOutputStream();

  /**
   * @return the {@link Writer} to write the response body.
   */
  Writer getWriter();

  /**
   * @return the {@link #setStatus(int) status that has been sent} or {@code null} if no status has been sent yet.
   */
  Integer getStatus();

  /**
   * ATTENTION: The status shall be send only once. Typically you should never call this method as an end-user (from
   * outside this framework).
   *
   * @param status the HTTP status to send.
   */
  default void setStatus(int status) {

    setStatus(status, null);
  }

  /**
   * ATTENTION: The status shall be send only once. Typically you should never call this method as an end-user (from
   * outside this framework).
   *
   * @param status the HTTP status to send.
   * @param statusText the optional status text.
   */
  void setStatus(int status, String statusText);

  /**
   * @param name the name of the HTTP header to send.
   * @param value the value of the HTTP header to send.
   */
  void setHeader(String name, String value);
}