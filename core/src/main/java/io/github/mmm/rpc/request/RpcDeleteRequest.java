package io.github.mmm.rpc.request;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.marshall.Marshalling;

/**
 * {@link RpcCrudRequest} for {@link io.github.mmm.rpc.client.RpcCrudClient#find(io.github.mmm.entity.id.Id) find}
 * operation.
 *
 * @param <E> type of {@link EntityBean}.
 * @since 1.0.0
 */
public class RpcDeleteRequest<E extends EntityBean> extends RpcCrudIdRequest<Void, E> {

  /**
   * The constructor.
   *
   * @param id the {@link #getId() primary key} of the entity to find.
   */
  public RpcDeleteRequest(Id<E> id) {

    super(id);
  }

  /**
   * The constructor.
   *
   * @param id the {@link #getId() primary key} of the entity to find.
   * @param entity the {@link #getEntity() entity} as prototype.
   */
  public RpcDeleteRequest(Id<E> id, E entity) {

    super(id, entity);
  }

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} as prototype.
   */
  public RpcDeleteRequest(E entity) {

    super(entity);
  }

  @Override
  public String getMethod() {

    return METHOD_DELETE;
  }

  @Override
  public String getPermission() {

    return "Delete." + getEntity().getType().getStableName();
  }

  @Override
  public Marshalling<Void> getResponseMarshalling() {

    return null;
  }

}
