/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.rpc.response;

import java.util.UUID;

import io.github.mmm.base.exception.ApplicationException;
import io.github.mmm.marshall.MarshallableObject;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.marshall.UnmarshallableObject;

/**
 * Simple container to marshall and unmarshall {@link ApplicationException errors}.
 *
 * @since 1.0.0
 */
public class RpcErrorData implements MarshallableObject, UnmarshallableObject {

  private static final String PROPERTY_MESSAGE = "message";

  private static final String PROPERTY_CODE = "code";

  private static final String PROPERTY_UUID = "uuid";

  private static final String PROPERTY_TECHNICAL = "technical";

  private String message;

  private String code;

  private UUID uuid;

  private boolean technical;

  /**
   * The constructor.
   */
  public RpcErrorData() {

    super();
    this.code = "RpcError";
    this.technical = true;
  }

  /**
   * @return message the {@link Throwable#getLocalizedMessage() error message}.
   */
  public String getMessage() {

    return this.message;
  }

  /**
   * @param message new value of {@link #getMessage()}.
   */
  public void setMessage(String message) {

    this.message = message;
  }

  /**
   * @return code the {@link ApplicationException#getCode() error code}.
   */
  public String getCode() {

    return this.code;
  }

  /**
   * @param code new value of {@link #getCode()}.
   */
  public void setCode(String code) {

    this.code = code;
  }

  /**
   * @return uuid the {@link ApplicationException#getUuid() UUID} or {@code null} if not available.
   */
  public UUID getUuid() {

    return this.uuid;
  }

  /**
   * @param uuid new value of {@link #getUuid()}.
   */
  public void setUuid(UUID uuid) {

    this.uuid = uuid;
  }

  /**
   * @return technical - {@code true} if {@link ApplicationException#isTechnical() technical error}, {@code false}
   *         otherwise.
   */
  public boolean isTechnical() {

    return this.technical;
  }

  /**
   * @param technical new value of {@link #isTechnical()}.
   */
  public void setTechnical(boolean technical) {

    this.technical = technical;
  }

  @Override
  public void write(StructuredWriter writer) {

    writer.writeStartObject();
    writer.writeName(PROPERTY_MESSAGE);
    writer.writeValueAsString(this.message);
    writer.writeName(PROPERTY_CODE);
    writer.writeValueAsString(this.code);
    if (this.uuid != null) {
      writer.writeName(PROPERTY_UUID);
      writer.writeValueAsString(this.uuid.toString());
    }
    writer.writeName(PROPERTY_TECHNICAL);
    writer.writeValueAsBoolean(this.technical);
    writer.writeEnd();
  }

  @Override
  public void read(StructuredReader reader) {

    if (reader.readStartObject()) {
      while (!reader.readEnd()) {
        String name = reader.readName();
        switch (name) {
          case PROPERTY_MESSAGE:
            setMessage(reader.readValueAsString());
            break;
          case PROPERTY_CODE:
            setCode(reader.readValueAsString());
            break;
          case PROPERTY_UUID:
            setUuid(UUID.fromString(reader.readValueAsString()));
            break;
          case PROPERTY_TECHNICAL:
            setTechnical(reader.readValueAsBoolean());
            break;
          default:
            // ignore unknown property for compatibility...
            reader.readValue();
            break;
        }
      }
    }
  }

  /**
   * @param error the {@link Throwable} to marshall.
   * @return the {@link RpcErrorData}.
   */
  public static RpcErrorData of(Throwable error) {

    RpcErrorData data = new RpcErrorData();
    data.setMessage(error.getLocalizedMessage());
    boolean technical = true;
    UUID uuid = null;
    String code = "Rpc";
    if (error instanceof ApplicationException) {
      ApplicationException applicationException = (ApplicationException) error;
      assert (applicationException.isForUser());
      technical = applicationException.isTechnical();
      uuid = applicationException.getUuid();
      code = applicationException.getCode();
    }
    data.setTechnical(technical);
    data.setUuid(uuid);
    data.setCode(code);
    return data;
  }

}
