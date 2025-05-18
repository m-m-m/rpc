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
public class RpcEntityFindRequest<E extends EntityBean> extends RpcEntityIdRequest<E, E> {

  /**
   * The constructor.
   *
   * @param id the {@link #getId() primary key} of the entity to find.
   */
  public RpcEntityFindRequest(Id<E> id) {

    super(id);
  }

  /**
   * The constructor.
   *
   * @param id the {@link #getId() primary key} of the entity to find.
   * @param entity the {@link #getEntity() entity} as prototype.
   */
  public RpcEntityFindRequest(Id<E> id, E entity) {

    super(id, entity);
  }

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} as prototype.
   */
  public RpcEntityFindRequest(E entity) {

    super(entity);
  }

  @Override
  public String getMethod() {

    return METHOD_GET;
  }

  @Override
  public String getOperation() {

    return "Find";
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Marshalling<E> getResponseMarshalling() {

    return (Marshalling) getEntity().copy();
  }

}
