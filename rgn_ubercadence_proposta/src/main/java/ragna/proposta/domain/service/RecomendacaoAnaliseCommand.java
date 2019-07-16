package ragna.proposta.domain.service;

import java.time.LocalDateTime;
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
public class RecomendacaoAnaliseCommand {
  private @NonNull UUID propostaId;
  private @NonNull UUID analiseId;
  private @NonNull String analista;
  private String recomendacao;
  private @NonNull LocalDateTime dataRecomendacao;
  private @NonNull String status;
  private @NonNull PropostaWorkflowId propostaWorkflowId;
}
