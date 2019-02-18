package fun.nxzh.guilin.basket.listener;

import com.rabbitmq.client.Channel;
import fun.nxzh.guilin.basket.config.event.IntegrationEventProcessor;
import java.io.IOException;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class IntegrationEventListener {

  @StreamListener(
      value = IntegrationEventProcessor.INPUT,
      condition = "headers['type']=='UserCheckoutAccepted'")
  public void handle1(
      @Payload String value,
      @Header(AmqpHeaders.CHANNEL) Channel channel,
      @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag)
      throws IOException {
    System.out.println("Received: " + value);
    channel.basicAck(deliveryTag, false);
  }

  @StreamListener(value = IntegrationEventProcessor.INPUT, condition =
      "headers['type']=='UserCheckoutAccepted'")
  public void handle2(
      @Payload String value,
      @Header(AmqpHeaders.CHANNEL) Channel channel,
      @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag)
      throws IOException {
    System.out.println("Received: " + value);

    channel.basicAck(deliveryTag, false);
  }
}
