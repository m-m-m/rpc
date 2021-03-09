package io.github.mmm.rpc.request.entity;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.marshall.MarshallingObject;

/**
 * {@link RpcEntityRequest} for sending only a single {@link io.github.mmm.entity.id.Id}.
 *
 * @param <D> type of {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data}.
 * @param <E> type of {@link EntityBean}.
 * @since 1.0.0
 */
public abstract class RpcEntityIdRequest<D, E extends EntityBean> extends RpcEntityRequest<D, E> {

  private Id<E> id;

  /**
   * The constructor.
   *
   * @param id the {@link #getId() primary key}.
   */
  public RpcEntityIdRequest(Id<E> id) {

    this(id, createEntity(id));
  }

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} as prototype.
   */
  public RpcEntityIdRequest(E entity) {

    this(null, entity);
  }

  /**
   * The constructor.
   *
   * @param id the {@link #getId() primary key}.
   * @param entity the {@link #getEntity() entity} as prototype.
   */
  public RpcEntityIdRequest(Id<E> id, E entity) {

    super(entity);
    this.id = id;
  }

  /**
   * @return id the {@link Id}.
   */
  public Id<E> getId() {

    return this.id;
  }

  @Override
  public String getPathPattern() {

    return super.getPathPattern() + "/{id}";
  }

  @Override
  public MarshallingObject getRequestMarshalling() {

    return null;
  }

}
