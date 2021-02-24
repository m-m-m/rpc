/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.impl;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.rpc.client.RpcClient;
import io.github.mmm.rpc.client.RpcCrudClient;
import io.github.mmm.rpc.client.RpcInvocation;
import io.github.mmm.rpc.request.RpcDeleteRequest;
import io.github.mmm.rpc.request.RpcFindRequest;
import io.github.mmm.rpc.request.RpcRequest;
import io.github.mmm.rpc.request.RpcSaveRequest;

/**
 * Implementation of {@link RpcCrudClient}.
 *
 * @since 1.0.0
 */
public class RpcCrudClientImpl implements RpcCrudClient {

  /** The singleton instance. */
  public static final RpcCrudClientImpl INSTANCE = new RpcCrudClientImpl();

  private final RpcClient client;

  /**
   * The constructor.
   */
  public RpcCrudClientImpl() {

    super();
    this.client = RpcClient.get();
  }

  @Override
  public <D> RpcInvocation<D> call(RpcRequest<D> request) {

    return this.client.call(request);
  }

  @Override
  public <E extends EntityBean> RpcInvocation<E> save(E entity) {

    return call(new RpcSaveRequest<>(entity));
  }

  @Override
  public <E extends EntityBean> RpcInvocation<E> find(Id<E> id) {

    return call(new RpcFindRequest<>(id));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public RpcInvocation<Void> delete(Id<?> id) {

    return call(new RpcDeleteRequest(id));
  }

}
