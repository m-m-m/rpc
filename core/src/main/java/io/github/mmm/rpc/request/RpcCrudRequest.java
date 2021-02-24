package io.github.mmm.rpc.request;

import java.util.Objects;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;

/**
 * {@link RpcRequest} for CRUD operation.
 *
 * @param <D> type of {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data}.
 * @param <E> type of {@link EntityBean}.
 * @since 1.0.0
 */
public abstract class RpcCrudRequest<D, E extends EntityBean> implements RpcRequest<D> {

  private final E entity;

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity}.
   */
  public RpcCrudRequest(E entity) {

    super();
    this.entity = entity;
  }

  /**
   * @return the entity to send or a prototype.
   */
  public E getEntity() {

    return this.entity;
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param id the {@link Id}.
   * @return a new and empty {@link EntityBean} instance for the {@link Id#getType() type} of the given {@link Id}.
   */
  protected static <E extends EntityBean> E createEntity(Id<E> id) {

    Objects.requireNonNull(id, "id");
    Class<E> type = id.getType();
    Objects.requireNonNull(type, "id.type");
    return BeanFactory.get().create(type);
  }

}
