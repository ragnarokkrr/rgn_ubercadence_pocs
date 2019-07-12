package ragna.analise.domain.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ParecerAnaliseCommand {
  private @NonNull String analista;
  private @NonNull String observacao;
}
