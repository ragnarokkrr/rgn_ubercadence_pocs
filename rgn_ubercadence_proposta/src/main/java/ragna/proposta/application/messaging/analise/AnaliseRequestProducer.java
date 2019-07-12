package ragna.proposta.application.messaging.analise;

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
import ragna.common.constants.Messaging;
import ragna.proposta.domain.model.PropostaIniciada;
import ragna.proposta.infrastructure.exception.PropostaException;

@Component
@Slf4j
public class AnaliseRequestProducer {

  private RabbitTemplate rabbitTemplate;

  private ObjectMapper objectMapper;

  private Queue queueAnaliseRequest;

  @Autowired
  public AnaliseRequestProducer(
      RabbitTemplate rabbitTemplate,
      ObjectMapper objectMapper,
      @Qualifier("queueAnaliseRequest") Queue queueAnaliseRequest) {
    this.rabbitTemplate = rabbitTemplate;
    this.objectMapper = objectMapper;
    this.queueAnaliseRequest = queueAnaliseRequest;
  }

  public void sendEvent(PropostaIniciada propostaIniciada) {
    log.info("Enviando para analise: {}", propostaIniciada);
    String typedName = propostaIniciada.getClass().getTypeName();
    String payload = serialize(propostaIniciada);

    Message message =
        MessageBuilder.withBody(payload.getBytes())
            .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
            .setHeader(Messaging.TYPED_NAME_HEADER, typedName)
            .build();
    rabbitTemplate.send(queueAnaliseRequest.getName(), message);
  }

  private String serialize(Object domainEvent) {
    try {
      return objectMapper.writeValueAsString(domainEvent);
    } catch (JsonProcessingException e) {
      throw new PropostaException("Erro serialziando", e);
    }
  }
}
