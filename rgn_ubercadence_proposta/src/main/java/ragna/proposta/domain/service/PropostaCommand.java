package ragna.proposta.domain.service;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PropostaCommand {
  private String nomeCliente;
  private BigDecimal valor;
}
