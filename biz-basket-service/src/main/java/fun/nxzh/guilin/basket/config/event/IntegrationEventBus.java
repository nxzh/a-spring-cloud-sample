package fun.nxzh.guilin.basket.config.event;

public interface IntegrationEventBus {

  void publish(IntegrationEvent integrationEvent);
}
