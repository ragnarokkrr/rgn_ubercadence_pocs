package ragna.proposta.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ragna.common.workflow.PropostaWorkflowId;
import ragna.proposta.infrastructure.events.DomainEvent;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PropostaConcluida implements DomainEvent {

  private @NonNull UUID id;
  private @NonNull Cliente cliente;
  private @NonNull BigDecimal valor;
  private @NonNull LocalDateTime occuredOn;
  private @NonNull PropostaWorkflowId propostaWorkflowId;

  @Override
  public LocalDateTime occuredOn() {
    return occuredOn;
  }
}
