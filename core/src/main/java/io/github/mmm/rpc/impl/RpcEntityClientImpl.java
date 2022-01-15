/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.impl;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.bean.db.statement.select.SelectStatement;
import io.github.mmm.entity.id.Id;
import io.github.mmm.rpc.client.RpcClient;
import io.github.mmm.rpc.client.RpcEntityClient;
import io.github.mmm.rpc.client.RpcInvocation;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.request.entity.RpcEntityDeleteRequest;
import io.github.mmm.rpc.request.entity.RpcEntityFindRequest;
import io.github.mmm.rpc.request.entity.RpcEntitySaveRequest;
import io.github.mmm.rpc.request.entity.RpcEntitySelectRequest;
import reactor.core.publisher.Flux;

/**
 * Implementation of {@link RpcEntityClient}.
 *
 * @since 1.0.0
 */
public class RpcEntityClientImpl implements RpcEntityClient {

  /** The singleton instance. */
  public static final RpcEntityClientImpl INSTANCE = new RpcEntityClientImpl();

  private final RpcClient client;

  /**
   * The constructor.
   */
  public RpcEntityClientImpl() {

    super();
    this.client = RpcClient.get();
  }

  @Override
  public <D> RpcInvocation<D> call(RpcRequest<D> request) {

    return this.client.call(request);
  }

  @Override
  public <E extends EntityBean> RpcInvocation<E> save(E entity) {

    return call(new RpcEntitySaveRequest<>(entity));
  }

  @Override
  public <E extends EntityBean> RpcInvocation<E> find(Id<E> id) {

    return call(new RpcEntityFindRequest<>(id));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public RpcInvocation<Void> delete(Id<?> id) {

    return call(new RpcEntityDeleteRequest(id));
  }

  @Override
  public <E extends EntityBean> RpcInvocation<Flux<E>> select(SelectStatement<E> statement) {

    return call(new RpcEntitySelectRequest<>(statement));
  }

}
