package ragna.analise.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Analise {
  private UUID id;
  private Status status;
  private String analista;
  private Proposta proposta;
  private LocalDateTime dataStatus;
  private String observacao;

  public static Analise novaAnaliseParaProposta(UUID propostaId, String cliente, BigDecimal valor) {
    return Analise.builder()
        .proposta(Proposta.builder().id(propostaId).cliente(cliente).valor(valor).build())
        .status(Status.EmAnalise)
        .build();
  }

  public void aprovar(String analista) {
    this.analista = analista;
    this.dataStatus = LocalDateTime.now();
    this.status = Status.Aprovada;
  }

  public void reprovar(String analista) {
    this.analista = analista;
    this.dataStatus = LocalDateTime.now();
    this.status = Status.Reprovada;
  }

  public void registrarErro(String erro) {
    this.observacao = erro;
    this.dataStatus = LocalDateTime.now();
    this.status = Status.Erro;
  }
}
