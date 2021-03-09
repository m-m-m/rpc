package io.github.mmm.rpc.request.entity;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.marshall.Marshalling;

/**
 * {@link RpcEntityRequest} for {@link io.github.mmm.rpc.client.RpcEntityClient#find(io.github.mmm.entity.id.Id) find}
 * operation.
 *
 * @param <E> type of {@link EntityBean}.
 * @since 1.0.0
 */
public class RpcEntityDeleteRequest<E extends EntityBean> extends RpcEntityIdRequest<Void, E> {

  /**
   * The constructor.
   *
   * @param id the {@link #getId() primary key} of the entity to find.
   */
  public RpcEntityDeleteRequest(Id<E> id) {

    super(id);
  }

  /**
   * The constructor.
   *
   * @param id the {@link #getId() primary key} of the entity to find.
   * @param entity the {@link #getEntity() entity} as prototype.
   */
  public RpcEntityDeleteRequest(Id<E> id, E entity) {

    super(id, entity);
  }

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} as prototype.
   */
  public RpcEntityDeleteRequest(E entity) {

    super(entity);
  }

  @Override
  public String getMethod() {

    return METHOD_DELETE;
  }

  @Override
  public String getOperation() {

    return "Delete";
  }

  @Override
  public Marshalling<Void> getResponseMarshalling() {

    return null;
  }

}
