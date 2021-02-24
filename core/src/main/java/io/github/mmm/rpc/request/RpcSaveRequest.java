package io.github.mmm.rpc.request;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.MarshallingObject;

/**
 * {@link RpcCrudRequest} for {@link io.github.mmm.rpc.client.RpcCrudClient#save(EntityBean) save} operation.
 *
 * @param <E> type of {@link EntityBean}.
 * @since 1.0.0
 */
public class RpcSaveRequest<E extends EntityBean> extends RpcCrudRequest<E, E> {

  /**
   * The constructor.
   *
   * @param entity the {@link #getEntity() entity} to save.
   */
  public RpcSaveRequest(E entity) {

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

    return (Marshalling) getEntity().copy(false);
  }

}
