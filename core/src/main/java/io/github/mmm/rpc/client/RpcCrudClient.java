/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.client;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;

/**
 * Extends {@link RpcCrudClient} with CRUD operations via {@link #save(EntityBean) save}, {@link #find(Id) find}, and
 * {@link #delete(Id)}.
 *
 * @since 1.0.0
 */
public interface RpcCrudClient extends RpcClient {

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param entity the {@link EntityBean} to save.
   * @return the {@link RpcInvocation} to configure additional options and finally
   *         {@link RpcInvocation#sendAsync(java.util.function.Consumer) send} the request to save. The
   *         {@link io.github.mmm.rpc.response.RpcDataResponse#getData() data from the received response} is the updated
   *         {@link EntityBean} with server-side changes ({@link Id} assigned on create, {@link Id#getVersion() version}
   *         updated, etc).
   */
  <E extends EntityBean> RpcInvocation<E> save(E entity);

  /**
   * @param <E> type of the {@link EntityBean}.
   * @param id the {@link Id primary key} of the requested {@link EntityBean}. Has to have a {@link Id#getType() type}.
   * @return the {@link RpcInvocation} to configure additional options and finally
   *         {@link RpcInvocation#sendAsync(java.util.function.Consumer) send} the request to find. The
   *         {@link io.github.mmm.rpc.response.RpcDataResponse#getData() data from the received response} is the
   *         {@link EntityBean} retrieved from the server-side or {@code null} if no such entity exists (and
   *         {@link io.github.mmm.rpc.response.RpcResponse#getStatus() status} is 404).
   */
  <E extends EntityBean> RpcInvocation<E> find(Id<E> id);

  /**
   * @param id the {@link Id} of the {@link EntityBean} to delete. Has to have a {@link Id#getType() type}.
   * @return the {@link RpcInvocation} to configure additional options and finally
   *         {@link RpcInvocation#sendAsync(java.util.function.Consumer) send} the request to delte. The
   *         {@link io.github.mmm.rpc.response.RpcDataResponse#getData() data from the received response} is
   *         {@link Void} and therefore always {@code null}. The delete operation is designed idempotent: It is
   *         successful both if the entity has been successfully deleted or did not exist. If you receive a
   *         {@link io.github.mmm.rpc.response.RpcResponse#isSuccess() successful} response it is guaranteed that the
   *         entity with the given {@link Id} does not exist anymore. In case of an
   *         {@link io.github.mmm.rpc.response.RpcResponse#isError() error} the deletion failed and can be retried.
   */
  RpcInvocation<Void> delete(Id<?> id);

  /**
   * @return the instance of this {@link RpcCrudClient}.
   */
  static RpcCrudClient get() {

    return io.github.mmm.rpc.impl.RpcCrudClientImpl.INSTANCE;
  }

}
