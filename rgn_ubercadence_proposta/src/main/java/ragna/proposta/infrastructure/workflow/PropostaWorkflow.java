package ragna.proposta.infrastructure.workflow;

import com.uber.cadence.workflow.QueryMethod;
import com.uber.cadence.workflow.SignalMethod;
import com.uber.cadence.workflow.WorkflowMethod;
import ragna.proposta.domain.model.PropostaConcluida;
import ragna.proposta.domain.model.PropostaIniciada;
import ragna.proposta.infrastructure.config.UberCadenceConstants;

public interface PropostaWorkflow {

  @WorkflowMethod(
      executionStartToCloseTimeoutSeconds = 100 * 60,
      taskList = UberCadenceConstants.TASK_LIST)
  void iniciarPropostaWorkflow(PropostaIniciada propostaIniciada);

  @SignalMethod
  void receberRecomendacaoAnalise(RecomendacaoAnalise recomendacaoAnalise);

  @SignalMethod
  void concluirProposta(PropostaConcluida propostaConcluida);

  @QueryMethod
  RecomendacaoAnalise getRecomendacaoAnaliseEvent();

  @QueryMethod
  PropostaConcluida getPropostaConcluidaEvent();

  @QueryMethod
  PropostaIniciada getPropostaIniciada();
}
