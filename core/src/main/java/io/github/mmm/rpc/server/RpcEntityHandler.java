package io.github.mmm.rpc.server;

import io.github.mmm.entity.bean.EntityBean;
import io.github.mmm.entity.id.Id;
import io.github.mmm.orm.statement.select.SelectStatement;
import reactor.core.publisher.Flux;

/**
 * A specific handler for (CRUD) operations on a specific {@link EntityBean}.
 *
 * @param <E> type of the managed {@link EntityBean}.
 * @since 1.0.0
 * @see RpcHandler
 */
public interface RpcEntityHandler<E extends EntityBean> {

  /**
   * @param entity the {@link EntityBean} to save.
   * @return the updated {@link EntityBean} that has been saved.
   * @see io.github.mmm.rpc.client.RpcEntityClient#save(EntityBean)
   */
  E save(E entity);

  /**
   * @param id the {@link Id} of the requested {@link EntityBean}.
   * @return the {@link EntityBean} or {@code null} if no such entity exists.
   * @see io.github.mmm.rpc.client.RpcEntityClient#find(Id)
   */
  E find(Id<E> id);

  /**
   * @param id the {@link Id} of the {@link EntityBean} to delete.
   * @see io.github.mmm.rpc.client.RpcEntityClient#delete(Id)
   */
  void delete(Id<E> id);

  /**
   * @param statement the {@link SelectStatement} to execute.
   * @return {@link Flux} of the selected entities.
   * @see io.github.mmm.rpc.client.RpcEntityClient#select(SelectStatement)
   */
  Flux<E> select(SelectStatement<E> statement);

}
