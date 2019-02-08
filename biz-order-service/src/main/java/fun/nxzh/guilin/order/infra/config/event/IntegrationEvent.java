package fun.nxzh.guilin.order.infra.config.event;

import java.time.Instant;

public class IntegrationEvent {

  private String id;
  private String type;
  private String body;
  private Instant occurredOn;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Instant getOccurredOn() {
    return occurredOn;
  }

  public void setOccurredOn(Instant occurredOn) {
    this.occurredOn = occurredOn;
  }
}
