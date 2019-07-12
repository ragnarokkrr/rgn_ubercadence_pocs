package ragna.proposta.domain.model;

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
public class RecomendacaoAnalise {
  private UUID id;
  private Analista analista;
  private LocalDateTime dataRecomendacao;
  private Status status;

  public enum Status {
    Aprovada,
    Reprovada,
    Erro,
    NaoReconhecido;

    public static Status valueOfOrDefault(String enumName) {
      try {
        return valueOf(enumName);
      } catch (Throwable throwable) {
        return NaoReconhecido;
      }
    }
  }
}
