package ragna.analise.domain.model;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ragna.common.workflow.PropostaWorkflowId;

@Data
@Builder
public class Proposta {
  private @NonNull UUID id;
  private @NonNull String cliente;
  private @NonNull BigDecimal valor;

  public PropostaWorkflowId workflowId() {
    return PropostaWorkflowId.from(id, cliente);
  }
}
