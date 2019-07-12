package ragna.analise.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ragna.analise.infrastructure.events.DomainEvent;
import ragna.common.workflow.PropostaWorkflowId;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AnaliseCriada implements DomainEvent {
  private UUID id;
  private Status status;
  private String analista;
  private LocalDateTime dataStatus;
  private String observacao;
  private UUID propostaId;
  private PropostaWorkflowId propostaWorkflowId;
  private LocalDateTime occuredOn;

  @Override
  public LocalDateTime occuredOn() {
    return occuredOn;
  }
}
