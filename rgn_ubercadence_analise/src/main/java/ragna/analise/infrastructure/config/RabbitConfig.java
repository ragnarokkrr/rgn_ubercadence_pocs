package ragna.analise.infrastructure.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  @Value("${queue.analise.response}")
  private String analiseResponseQueueName;

  @Value("${queue.analise.request}")
  private String analiseRequestQueueName;

  @Bean("queueAnaliseResponse")
  Queue queueAnaliseResponse() {
    return new Queue(analiseResponseQueueName, false);
  }

  @Bean("queueAnaliseRequest")
  Queue queueAnaliseRequest() {
    return new Queue(analiseRequestQueueName, false);
  }
}
