package fun.nxzh.guilin.basket.config.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface IntegrationEventProcessor {
  String INPUT = "int_input";
  String OUTPUT = "int_output";

  @Input(INPUT)
  SubscribableChannel input();

  @Output(OUTPUT)
  MessageChannel output();
}
