package fun.nxzh.guilin.order.infra.config.event;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@EnableBinding({IntegrationEventProcessor.class})
public class RemoteEventPublisherAspectConfiguration {}
