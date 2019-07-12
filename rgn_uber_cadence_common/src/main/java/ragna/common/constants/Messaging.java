package ragna.common.constants;

public interface Messaging {
  String TYPED_NAME_HEADER = "typedName";

  interface PropostaEventTypes {
    String PROPOSTA_INICIADA = "ragna.proposta.domain.model.PropostaIniciada";
  }

  interface AnaliseEventTypes {
    String ANALISE_APROVADA = "ragna.analise.domain.model.AnaliseAprovada";
    String ANALISE_CRIADA = "ragna.analise.domain.model.AnaliseCriada";
    String ANALISE_REPROVADA = "ragna.analise.domain.model.AnaliseReprovada";
  }
}
