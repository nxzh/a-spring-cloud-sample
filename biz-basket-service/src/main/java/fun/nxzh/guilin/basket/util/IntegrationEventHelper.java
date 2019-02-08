package fun.nxzh.guilin.basket.util;

import fun.nxzh.guilin.basket.config.event.IntegrationEvent;
import java.time.Instant;
import java.util.UUID;

public class IntegrationEventHelper {
  public static IntegrationEvent createEvent(Object object, String type) {
    IntegrationEvent event = new IntegrationEvent();
    event.setBody(JsonHelper.parse(object));
    event.setOccurredOn(Instant.now());
    event.setId(UUID.randomUUID().toString());
    event.setType(type);
    return event;
  }
}
