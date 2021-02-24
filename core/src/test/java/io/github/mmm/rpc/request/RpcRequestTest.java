package io.github.mmm.rpc.request;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.marshall.Marshalling;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * Test of {@link RpcRequest}.
 */
public class RpcRequestTest extends Assertions {

  /** Test of {@link RpcRequest#getPathValue()}. */
  @Test
  public void testGetPathValue() {

    // given
    TestRequest request = new TestRequest();
    request.Name.set("Foo");
    request.Id.setValue(4711);
    // when
    String path = request.getPathValue();
    // then
    assertThat(path).isEqualTo("/prefix/Foo/4711/suffix");
  }

  /** Test of {@link RpcRequest#setPathVariables(Map)}. */
  @Test
  public void testSetPathVariables() {

    // given
    TestRequest request = new TestRequest();
    Map<String, String> map = Map.of("Name", "Foo", "Id", "4711");
    // when
    request.setPathVariables(map);
    // then
    assertThat(request.Name.get()).isEqualTo("Foo");
    assertThat(request.Id.get()).isEqualTo(4711);
  }

  /** {@link RpcRequestBean} for testing. */
  public static class TestRequest extends RpcRequestBean<Void> {

    /** The name. */
    public final StringProperty Name;

    /** The primary key. */
    public final IntegerProperty Id;

    /**
     * The constructor.
     */
    public TestRequest() {

      super();
      this.Name = add().newString("Name");
      this.Id = add().newInteger("Id");
    }

    @Override
    public String getPathPattern() {

      return "/prefix/{Name}/{Id}/suffix";
    }

    @Override
    public String getPermission() {

      return "TestPermission";
    }

    @Override
    public Marshalling<Void> getResponseMarshalling() {

      return null;
    }

  }

}
