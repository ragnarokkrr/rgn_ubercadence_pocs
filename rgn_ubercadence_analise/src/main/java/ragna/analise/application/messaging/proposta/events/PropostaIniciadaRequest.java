package ragna.analise.application.messaging.proposta.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ragna.common.workflow.PropostaWorkflowId;

@Data
@NoArgsConstructor
public class PropostaIniciadaRequest {
  private @NonNull UUID id;
  private @NonNull String nomeCliente;
  private @NonNull BigDecimal valor;
  private @NonNull LocalDateTime occuredOn;
  private @NonNull PropostaWorkflowId propostaWorkflowId;
}
