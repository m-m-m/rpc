package io.github.mmm.rpc.request.entity;

import java.util.Objects;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.rpc.request.RpcRequest;

/**
 * {@link RpcRequest} for a (CRUD) operation on an {@link EntityBean}.
 *
 * @param <D> type of {@link io.github.mmm.rpc.response.RpcDataResponse#getData() response data}.
 * @param <E> type of {@link EntityBean}.
 * @since 1.0.0
 */
public abstract class RpcEntityRequest<D, E extends EntityBean> implements RpcRequest<D> {

  private final E entity;

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity}.
   */
  public RpcEntityRequest(E entity) {

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
   * @return the CRUD operation name (e.g. "Find" or "Save").
   */
  protected abstract String getOperation();

  @Override
  public String getPermission() {

    return getOperation() + "." + getEntity().getType().getStableName();
  }

  @Override
  public String getPathPattern() {

    return "/" + getEntity().getType().getStableName();
  }

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param id the {@link Id}.
   * @return a new and empty {@link EntityBean} instance for the {@link Id#getEntityType() type} of the given {@link Id}.
   */
  protected static <E extends EntityBean> E createEntity(Id<E> id) {

    Objects.requireNonNull(id, "id");
    Class<E> type = id.getEntityType();
    Objects.requireNonNull(type, "id.type");
    return BeanFactory.get().create(type);
  }

}
