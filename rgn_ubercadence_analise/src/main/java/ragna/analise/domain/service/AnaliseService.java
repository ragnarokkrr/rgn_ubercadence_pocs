package ragna.analise.domain.service;

import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ragna.analise.application.messaging.proposta.AnaliseResponsePublisher;
import ragna.analise.domain.model.Analise;
import ragna.analise.domain.model.AnaliseAprovada;
import ragna.analise.domain.model.AnaliseCriada;
import ragna.analise.domain.model.AnaliseReprovada;
import ragna.analise.domain.repository.AnaliseRepository;

@Service
@Slf4j
public class AnaliseService {
  private AnaliseRepository analiseRepository;
  private AnaliseResponsePublisher analiseResponsePublisher;

  @Autowired
  public AnaliseService(
      AnaliseRepository analiseRepository, AnaliseResponsePublisher analiseResponsePublisher) {
    this.analiseRepository = analiseRepository;
    this.analiseResponsePublisher = analiseResponsePublisher;
  }

  public Analise novaAnalise(AnaliseCommand analiseCommand) {

    Analise analise =
        Analise.novaAnaliseParaProposta(
            analiseCommand.getPropostaId(),
            analiseCommand.getClienteNome(),
            analiseCommand.getValorProposta());

    Analise saved = analiseRepository.insert(analise);
    log.debug("analise criada : {}", saved);

    AnaliseCriada analiseCriada =
        AnaliseCriada.builder()
            .analista(saved.getAnalista())
            .dataStatus(saved.getDataStatus())
            .id(saved.getId())
            .observacao(saved.getObservacao())
            .propostaId(saved.getProposta().getId())
            .propostaWorkflowId(saved.getProposta().workflowId())
            .status(saved.getStatus())
            .build();
    analiseResponsePublisher.sendEvent(analiseCriada);

    return saved;
  }

  public Analise aprovar(UUID id, ParecerAnaliseCommand parecerAnaliseCommand) {
    Analise analise = analiseRepository.findById(id);
    analise.aprovar(parecerAnaliseCommand.getAnalista());
    Analise saved = analiseRepository.save(analise);

    AnaliseAprovada analiseAprovada =
        AnaliseAprovada.builder()
            .analista(saved.getAnalista())
            .dataStatus(saved.getDataStatus())
            .id(saved.getId())
            .observacao(saved.getObservacao())
            .propostaId(saved.getProposta().getId())
            .propostaWorkflowId(saved.getProposta().workflowId())
            .status(saved.getStatus())
            .build();
    analiseResponsePublisher.sendEvent(analiseAprovada);

    return saved;
  }

  public Analise reprovar(UUID id, ParecerAnaliseCommand parecerAnaliseCommand) {
    Analise analise = analiseRepository.findById(id);
    analise.reprovar(parecerAnaliseCommand.getAnalista());
    Analise saved = analiseRepository.save(analise);

    AnaliseReprovada analiseReprovada =
        AnaliseReprovada.builder()
            .analista(saved.getAnalista())
            .dataStatus(saved.getDataStatus())
            .id(saved.getId())
            .observacao(saved.getObservacao())
            .propostaId(saved.getProposta().getId())
            .propostaWorkflowId(saved.getProposta().workflowId())
            .status(saved.getStatus())
            .build();
    analiseResponsePublisher.sendEvent(analiseReprovada);

    return saved;
  }

  public List<Analise> list() {
    return analiseRepository.list();
  }
}
