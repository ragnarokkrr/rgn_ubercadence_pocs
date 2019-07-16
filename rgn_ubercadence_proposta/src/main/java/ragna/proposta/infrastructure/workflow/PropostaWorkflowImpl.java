package ragna.proposta.infrastructure.workflow;

import com.uber.cadence.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import ragna.common.workflow.PropostaWorkflowId;
import ragna.proposta.domain.model.PropostaConcluida;
import ragna.proposta.domain.model.PropostaIniciada;
import ragna.proposta.domain.service.RecomendacaoAnaliseCommand;

@Slf4j
public class PropostaWorkflowImpl implements PropostaWorkflow {

  private final PropostaActivities propostaActivities =
      Workflow.newActivityStub(PropostaActivities.class);

  private boolean exit = false;
  private boolean firstRun = false;

  private PropostaWorkflowId propostaWorkflowId;
  private PropostaIniciada propostaIniciada;
  private RecomendacaoAnalise recomendacaoAnalise;
  private PropostaConcluida propostaConcluida;

  @Override
  public void iniciarPropostaWorkflow(PropostaIniciada propostaIniciada) {

    while (true) {
      Workflow.await(() -> !this.isStopCondition());

      if (!firstRun) {
        propostaWorkflowId = propostaIniciada.getPropostaWorkflowId();

        log.info("INICIANDO WORKFLOW: {} {}", propostaWorkflowId.encodedId(), propostaIniciada);
        this.propostaWorkflowId = propostaIniciada.getPropostaWorkflowId();
        this.propostaIniciada = propostaIniciada;
        propostaActivities.enviarPropostaParaAnalise(propostaIniciada);
        firstRun = true;
      }
    }
  }

  @Override
  public void receberRecomendacaoAnalise(RecomendacaoAnalise recomendacaoAnalise) {
    this.recomendacaoAnalise = recomendacaoAnalise;
    log.info(
        "WORKFLOW RECEBENDO RECOMENDACAO ANALISE: {} {} {}",
        propostaWorkflowId.encodedId(),
        recomendacaoAnalise,
        propostaActivities);
    RecomendacaoAnaliseCommand recomendacaoAnaliseCommand =
        RecomendacaoAnaliseCommand.builder()
            .analiseId(recomendacaoAnalise.getId())
            .analista(recomendacaoAnalise.getAnalista())
            .dataRecomendacao(recomendacaoAnalise.getDataStatus())
            .propostaId(recomendacaoAnalise.getPropostaId())
            .propostaWorkflowId(recomendacaoAnalise.getPropostaWorkflowId())
            .status(recomendacaoAnalise.getStatus())
            .recomendacao(recomendacaoAnalise.getObservacao())
            .build();
    log.info(
        "WORKFLOW RECEBENDO RECOMENDACAO ANALISE 2: {} {}",
        propostaWorkflowId.encodedId(),
        recomendacaoAnalise);
    propostaActivities.receberRecomendacaoAnalise(recomendacaoAnaliseCommand);
    log.info(
        "WORKFLOW RECEBENDO RECOMENDACAO ANALISE 3: {} {}",
        propostaWorkflowId.encodedId(),
        recomendacaoAnalise);
  }

  @Override
  public void concluirProposta(PropostaConcluida propostaConcluida) {
    this.propostaConcluida = propostaConcluida;
    log.info("WORKFLOW CONCLUINDO PROPOSTA: {} {}", propostaWorkflowId, propostaConcluida);
    propostaActivities.concluirProposta(propostaConcluida);
    this.exit = true;
  }

  @Override
  public RecomendacaoAnalise getRecomendacaoAnaliseEvent() {
    return this.recomendacaoAnalise;
  }

  @Override
  public PropostaConcluida getPropostaConcluidaEvent() {
    return this.propostaConcluida;
  }

  @Override
  public PropostaIniciada getPropostaIniciada() {
    return this.propostaIniciada;
  }

  private boolean isStopCondition() {
    return firstRun && exit;
  }
}
