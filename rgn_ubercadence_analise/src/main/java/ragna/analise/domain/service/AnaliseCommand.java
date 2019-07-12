package ragna.analise.domain.service;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ragna.common.workflow.PropostaWorkflowId;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnaliseCommand {
  private @NonNull UUID propostaId;
  private @NonNull String clienteNome;
  private @NonNull BigDecimal valorProposta;
  private @NonNull PropostaWorkflowId propostaWorkflowId;
}
