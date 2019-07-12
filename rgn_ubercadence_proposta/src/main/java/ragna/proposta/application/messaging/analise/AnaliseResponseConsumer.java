package ragna.proposta.application.messaging.analise;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ragna.common.constants.Messaging;
import ragna.common.constants.Messaging.AnaliseEventTypes;
import ragna.proposta.application.messaging.analise.events.AnaliseAprovada;
import ragna.proposta.application.messaging.analise.events.AnaliseReprovada;
import ragna.proposta.infrastructure.workflow.RecomendacaoAnalise;
import ragna.proposta.infrastructure.workflow.execution.WorkflowExecution;

@Component
@Slf4j
public class AnaliseResponseConsumer {

  private WorkflowExecution workflowExecution;
  private ObjectMapper objectMapper;

  @Autowired
  public AnaliseResponseConsumer(WorkflowExecution workflowExecution, ObjectMapper objectMapper) {
    this.workflowExecution = workflowExecution;
    this.objectMapper = objectMapper;
  }

  @RabbitListener(queues = {"${queue.analise.response}"})
  public void receive(@Headers Map<String, String> headers, @Payload String payload) {
    log.info("HEADERS: " + headers);
    log.info("ANALISE: " + payload);
    String typedName = headers.get(Messaging.TYPED_NAME_HEADER);

    Optional<RecomendacaoAnalise> recomendacaoAnaliseOption =
        Match(typedName)
            .of(
                Case(
                    $(AnaliseEventTypes.ANALISE_REPROVADA),
                    () -> unmarshalPropostaReprovada(payload)),
                Case(
                    $(AnaliseEventTypes.ANALISE_APROVADA),
                    () -> unmarshalPropostaAprovada(payload)),
                Case($(), Optional.empty()));

    recomendacaoAnaliseOption.ifPresentOrElse(
        workflowExecution::anexarRecomendacaoAnalise,
        () -> log.info("Mensagem nao reconhecida: {} {}", typedName, payload));
  }

  private Optional<RecomendacaoAnalise> unmarshalPropostaReprovada(String payload) {
    try {
      AnaliseReprovada analiseReprovada = objectMapper.readValue(payload, AnaliseReprovada.class);
      return Optional.of(
          RecomendacaoAnalise.builder()
              .analista(analiseReprovada.getAnalista())
              .dataStatus(analiseReprovada.getDataStatus())
              .id(analiseReprovada.getId())
              .propostaId(analiseReprovada.getPropostaId())
              .observacao(analiseReprovada.getObservacao())
              .occuredOn(analiseReprovada.getOccuredOn())
              .propostaWorkflowId(analiseReprovada.getPropostaWorkflowId())
              .status(analiseReprovada.getStatus())
              .build());
    } catch (IOException e) {
      log.error("Cannot parse {}. Payload: {}", AnaliseReprovada.class.getName(), payload);
    }

    return Optional.empty();
  }

  private Optional<RecomendacaoAnalise> unmarshalPropostaAprovada(String payload) {
    try {
      AnaliseAprovada analiseAprovada = objectMapper.readValue(payload, AnaliseAprovada.class);
      return Optional.of(
          RecomendacaoAnalise.builder()
              .analista(analiseAprovada.getAnalista())
              .dataStatus(analiseAprovada.getDataStatus())
              .id(analiseAprovada.getId())
              .propostaId(analiseAprovada.getPropostaId())
              .observacao(analiseAprovada.getObservacao())
              .occuredOn(analiseAprovada.getOccuredOn())
              .propostaWorkflowId(analiseAprovada.getPropostaWorkflowId())
              .status(analiseAprovada.getStatus())
              .build());
    } catch (IOException e) {
      log.error("Cannot parse {}. Payload: {}", AnaliseAprovada.class.getName(), payload);
    }

    return Optional.empty();
  }
}
