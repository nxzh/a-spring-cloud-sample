package fun.nxzh.guilin.order.infra.config.event;

public interface IntegrationEventBus {

  void publish(IntegrationEvent integrationEvent);
}
