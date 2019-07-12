package ragna.proposta.infrastructure.workflow;

import com.uber.cadence.workflow.CompletablePromise;
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

  private CompletablePromise<RecomendacaoAnalise> recomendacaoAnalisePromise =
      Workflow.newPromise();

  private CompletablePromise<PropostaConcluida> propostaConcluidaPromise = Workflow.newPromise();

  private boolean exit = false;
  private boolean firstRun = false;

  private PropostaWorkflowId propostaWorkflowId;

  @Override
  public void iniciarPropostaWorkflow(PropostaIniciada propostaIniciada) {

    while (true) {

      if (!firstRun) {
        propostaWorkflowId = propostaIniciada.getPropostaWorkflowId();

        log.info("INICIANDO WORKFLOW: {} {}", propostaWorkflowId.encodedId(), propostaIniciada);
        this.propostaWorkflowId = propostaIniciada.getPropostaWorkflowId();
        propostaActivities.enviarPropostaParaAnalise(propostaIniciada);
        firstRun = true;
      }

      Workflow.await(() -> !this.isStopCondition());

      if (isStopCondition()) {
        return;
      }
    }
  }

  @Override
  public void receberRecomendacaoAnalise(RecomendacaoAnalise recomendacaoAnalise) {
    log.info(
        "RECEBENDO RECOMENDACAO ANALISE: {} {}",
        propostaWorkflowId.encodedId(),
        recomendacaoAnalise);
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
    propostaActivities.receberRecomendacaoAnalise(recomendacaoAnaliseCommand);
    this.recomendacaoAnalisePromise.complete(recomendacaoAnalise);
  }

  @Override
  public RecomendacaoAnalise getRecomendacaoAnaliseEvent() {
    return recomendacaoAnalisePromise.get();
  }

  @Override
  public void concluirProposta(PropostaConcluida propostaConcluida) {
    log.info("CONCLUINDO PROPOSTA: {} {}", propostaWorkflowId, propostaConcluida);
    propostaActivities.concluirProposta(propostaConcluida);
    this.propostaConcluidaPromise.complete(propostaConcluida);
    this.exit = true;
  }

  @Override
  public PropostaConcluida getPropostaConcluidaEvent() {
    return propostaConcluidaPromise.get();
  }

  private boolean isStopCondition() {
    return firstRun && exit;
  }
}
