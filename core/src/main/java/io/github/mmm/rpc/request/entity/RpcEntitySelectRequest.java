package io.github.mmm.rpc.request.entity;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.marshall.Marshalling;
import io.github.mmm.marshall.MarshallingObject;
import io.github.mmm.orm.statement.select.SelectStatement;
import reactor.core.publisher.Flux;

/**
 * {@link RpcEntityRequest} for {@link io.github.mmm.rpc.client.RpcEntityClient#save(EntityBean) save} operation.
 *
 * @param <E> type of the result.
 * @since 1.0.0
 */
public class RpcEntitySelectRequest<E extends EntityBean> extends RpcEntityRequest<Flux<E>, E> {

  private SelectStatement<E> statement;

  /**
   * The constructor.
   *
   * @param select the {@link #getStatement() select statement}.
   */
  public RpcEntitySelectRequest(SelectStatement<E> select) {

    super(select.getSelect().getResultBean());
    this.statement = select;
  }

  /**
   * @return the {@link SelectStatement}.
   */
  public SelectStatement<E> getStatement() {

    return this.statement;
  }

  @Override
  public String getOperation() {

    return "Select";
  }

  @Override
  public MarshallingObject getRequestMarshalling() {

    // TODO
    // return StatementMarshalling.get();
    return null;
  }

  @Override
  public Marshalling<Flux<E>> getResponseMarshalling() {

    // TODO
    return null;
  }

}
