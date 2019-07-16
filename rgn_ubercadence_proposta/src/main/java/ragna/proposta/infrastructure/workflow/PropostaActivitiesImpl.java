package ragna.proposta.infrastructure.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ragna.proposta.application.messaging.analise.AnaliseRequestProducer;
import ragna.proposta.domain.model.PropostaConcluida;
import ragna.proposta.domain.model.PropostaIniciada;
import ragna.proposta.domain.service.PropostaService;
import ragna.proposta.domain.service.RecomendacaoAnaliseCommand;

@Component
@Slf4j
public class PropostaActivitiesImpl implements PropostaActivities {
  private PropostaService propostaService;
  private AnaliseRequestProducer analiseRequestProducer;

  @Autowired
  public PropostaActivitiesImpl(
      PropostaService propostaService, AnaliseRequestProducer analiseRequestProducer) {
    this.propostaService = propostaService;
    this.analiseRequestProducer = analiseRequestProducer;
  }

  @Override
  public void enviarPropostaParaAnalise(PropostaIniciada propostaIniciada) {
    log.info("ENVIANDO P/ ANALISE: {}", propostaIniciada);
    this.analiseRequestProducer.sendEvent(propostaIniciada);
  }

  @Override
  public void receberRecomendacaoAnalise(RecomendacaoAnaliseCommand recomendacaoAnaliseCommand) {
    log.info("ACTIVITY - RECEBENDO de Analise: {}", recomendacaoAnaliseCommand);
    this.propostaService.anexarRecomendacaoAnalise(recomendacaoAnaliseCommand);
  }

  @Override
  public void concluirProposta(PropostaConcluida propostaConcluida) {
    log.info("ACTIVITY - CONCLUINDO PROPOSTA: {}", propostaConcluida);
    // TODO implementar concluirProposta
  }
}
