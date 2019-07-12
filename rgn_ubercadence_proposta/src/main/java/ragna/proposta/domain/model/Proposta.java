package ragna.proposta.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ragna.common.workflow.PropostaWorkflowId;
import ragna.proposta.domain.model.RecomendacaoAnalise.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Proposta {
  private UUID id;
  private Cliente cliente;
  private BigDecimal valor;

  private RecomendacaoAnalise recomendacaoAnalise;

  public static Proposta criarProposta(String nomeCliente, BigDecimal valor) {
    return Proposta.builder()
        .cliente(Cliente.builder().nome(nomeCliente).build())
        .valor(valor)
        .build();
  }

  public PropostaWorkflowId workflowId() {
    return PropostaWorkflowId.from(id, cliente.getNome());
  }

  public void anexarRecomendacaoAnalise(
      UUID analiseId, String analista, LocalDateTime dataRecomendacao, String status) {
    this.recomendacaoAnalise =
        RecomendacaoAnalise.builder()
            .id(analiseId)
            .analista(Analista.builder().nome(analista).build())
            .dataRecomendacao(dataRecomendacao)
            .status(Status.valueOfOrDefault(status))
            .build();
  }
}
