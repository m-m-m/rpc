package io.github.mmm.rpc.request.entity;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.MarshallingObject;

/**
 * {@link RpcEntityRequest} for {@link io.github.mmm.rpc.client.RpcEntityClient#save(EntityBean) save} operation.
 *
 * @param <E> type of {@link EntityBean}.
 * @since 1.0.0
 */
public class RpcEntitySaveRequest<E extends EntityBean> extends RpcEntityRequest<E, E> {

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} to save.
   */
  public RpcEntitySaveRequest(E entity) {

    super(entity);
  }

  @Override
  public String getOperation() {

    return "Save";
  }

  @Override
  public MarshallingObject getRequestMarshalling() {

    return getEntity();
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Marshalling<E> getResponseMarshalling() {

    return (Marshalling) getEntity().copy();
  }

}
