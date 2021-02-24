package io.github.mmm.rpc.request;

/**
 * Interface for an object defining an HTTP {@link #getMethod() method}.
 *
 * @since 1.0.0
 */
public interface HttpMethod {

  /** {@link #getMethod() Method} {@value}. */
  public static final String METHOD_POST = "POST";

  /** {@link #getMethod() Method} {@value}. */
  public static final String METHOD_GET = "GET";

  /** {@link #getMethod() Method} {@value}. */
  public static final String METHOD_DELETE = "DELETE";

  /**
   * @return the HTTP method such as {@link #METHOD_GET GET}, {@link #METHOD_POST POST}, or {@link #METHOD_DELETE
   *         DELETE}.
   */
  String getMethod();

}
