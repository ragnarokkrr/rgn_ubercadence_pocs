package ragna.analise.application.messaging.proposta;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ragna.analise.application.messaging.proposta.events.PropostaIniciadaRequest;
import ragna.analise.domain.service.AnaliseCommand;
import ragna.analise.domain.service.AnaliseService;
import ragna.common.constants.Messaging;
import ragna.common.constants.Messaging.PropostaEventTypes;

@Component
@Slf4j
public class AnaliseRequestConsumer {

  private ObjectMapper objectMapper;

  private AnaliseService analiseService;

  @Autowired
  public AnaliseRequestConsumer(ObjectMapper objectMapper, AnaliseService analiseService) {
    this.objectMapper = objectMapper;
    this.analiseService = analiseService;
  }

  @RabbitListener(queues = {"${queue.analise.request}"})
  public void receive(@Headers Map<String, String> headers, @Payload String payload) {
    log.info("HEADERS: {}", headers);
    log.info("ANALISE: {}", payload);
    String typedName = headers.get(Messaging.TYPED_NAME_HEADER);

    if (!PropostaEventTypes.PROPOSTA_INICIADA.equals(typedName)) {
      log.info("{} nao reconhecido: {}", typedName, payload);
    }

    PropostaIniciadaRequest propostaIniciadaRequest = deserialize(payload);

    AnaliseCommand analiseCommand =
        AnaliseCommand.builder()
            .clienteNome(propostaIniciadaRequest.getNomeCliente())
            .propostaId(propostaIniciadaRequest.getId())
            .propostaWorkflowId(propostaIniciadaRequest.getPropostaWorkflowId())
            .valorProposta(propostaIniciadaRequest.getValor())
            .build();

    log.info("Vai chamar novaAnalise: {}", analiseCommand);
    analiseService.novaAnalise(analiseCommand);
  }

  private PropostaIniciadaRequest deserialize(String payload) {
    try {
      return objectMapper.readValue(payload, PropostaIniciadaRequest.class);
    } catch (IOException e) {
      throw new SerializationException(
          String.format("Nao foi possivel deserializar: %s", payload), e);
    }
  }
}
