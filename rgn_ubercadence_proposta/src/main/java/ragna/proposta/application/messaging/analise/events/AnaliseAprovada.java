package ragna.proposta.application.messaging.analise.events;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ragna.common.workflow.PropostaWorkflowId;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AnaliseAprovada {

  private UUID id;
  private String status;
  private String analista;
  private LocalDateTime dataStatus;
  private String observacao;
  private UUID propostaId;
  private PropostaWorkflowId propostaWorkflowId;
  private LocalDateTime occuredOn;
}
