package ragna.analise.application.messaging.proposta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ragna.analise.infrastructure.events.DomainEvent;
import ragna.analise.infrastructure.exceptions.AnaliseException;
import ragna.common.constants.Messaging;

@Component
@Slf4j
public class AnaliseResponsePublisher {

  private final RabbitTemplate rabbitTemplate;
  private final ObjectMapper objectMapper;
  private final Queue queueAnaliseResponse;

  @Autowired
  public AnaliseResponsePublisher(
      RabbitTemplate rabbitTemplate,
      ObjectMapper objectMapper,
      @Qualifier("queueAnaliseResponse") Queue queueAnaliseResponse) {
    this.rabbitTemplate = rabbitTemplate;
    this.objectMapper = objectMapper;
    this.queueAnaliseResponse = queueAnaliseResponse;
  }

  public void sendEvent(DomainEvent domainEvent) {

    String typedName = domainEvent.getClass().getTypeName();
    String payload = serialize(domainEvent);

    log.info("ENVIANDO {}: {}", queueAnaliseResponse.getName(), payload);

    Message message =
        MessageBuilder.withBody(payload.getBytes())
            .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
            .setHeader(Messaging.TYPED_NAME_HEADER, typedName)
            .build();
    rabbitTemplate.send(queueAnaliseResponse.getName(), message);
  }

  private String serialize(Object domainEvent) {
    try {
      return objectMapper.writeValueAsString(domainEvent);
    } catch (JsonProcessingException e) {
      throw new AnaliseException("Erro serialziando", e);
    }
  }
}
