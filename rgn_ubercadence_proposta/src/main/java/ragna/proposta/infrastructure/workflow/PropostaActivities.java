package ragna.proposta.infrastructure.workflow;

import com.uber.cadence.activity.ActivityMethod;
import ragna.proposta.domain.model.PropostaConcluida;
import ragna.proposta.domain.model.PropostaIniciada;
import ragna.proposta.domain.service.RecomendacaoAnaliseCommand;

public interface PropostaActivities {
  @ActivityMethod(scheduleToCloseTimeoutSeconds = 10 * 60)
  void enviarPropostaParaAnalise(PropostaIniciada propostaIniciada);

  @ActivityMethod(scheduleToCloseTimeoutSeconds = 10 * 60)
  void receberRecomendacaoAnalise(RecomendacaoAnaliseCommand recomendacaoAnaliseCommand);

  void concluirProposta(PropostaConcluida propostaConcluida);
}
