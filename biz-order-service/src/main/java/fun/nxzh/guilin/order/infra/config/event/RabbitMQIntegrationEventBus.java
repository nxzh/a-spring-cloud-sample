package fun.nxzh.guilin.order.infra.config.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQIntegrationEventBus implements IntegrationEventBus {

  @Autowired private IntegrationEventProcessor integrationEventProcessor;

  @Override
  public void publish(IntegrationEvent integrationEvent) {
    integrationEventProcessor
        .output()
        .send(
            MessageBuilder.withPayload(integrationEvent)
                .setHeader("type", integrationEvent.getType())
                .build());
  }
}
