package io.github.mmm.rpc.server;

import io.github.mmm.entity.bean.EntityBean;

/**
 * Abstract base implementation of {@link RpcEntityHandler}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 */
public abstract class AbstractRpcEntityHandler<E extends EntityBean> implements RpcEntityHandler<E> {

  private final E prototype;

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   */
  public AbstractRpcEntityHandler(E prototype) {

    super();
    this.prototype = prototype;
  }

  /**
   * @return the prototype instance of the {@link EntityBean} managed by this {@link RpcEntityHandler}.
   */
  public E getPrototype() {

    return this.prototype;
  }

}
