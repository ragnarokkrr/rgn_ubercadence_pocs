package ragna.proposta.infrastructure.workflow;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ragna.common.workflow.PropostaWorkflowId;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RecomendacaoAnalise {
  private UUID id;
  private String status;
  private String analista;
  private LocalDateTime dataStatus;
  private String observacao;
  private UUID propostaId;
  private PropostaWorkflowId propostaWorkflowId;
  private LocalDateTime occuredOn;
}
