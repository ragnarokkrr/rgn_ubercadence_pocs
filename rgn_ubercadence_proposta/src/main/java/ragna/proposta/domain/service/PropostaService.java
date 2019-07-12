package ragna.proposta.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ragna.proposta.domain.model.Proposta;
import ragna.proposta.domain.model.PropostaAnalisada;
import ragna.proposta.domain.model.PropostaIniciada;
import ragna.proposta.domain.repository.PropostaRepository;
import ragna.proposta.infrastructure.events.spring.DomainEventPublisher;

@Service
@Slf4j
public class PropostaService {
  private PropostaRepository propostaRepository;
  private DomainEventPublisher domainEventPublisher;

  @Autowired
  public PropostaService(
      PropostaRepository propostaRepository, DomainEventPublisher domainEventPublisher) {
    this.propostaRepository = propostaRepository;
    this.domainEventPublisher = domainEventPublisher;
  }

  public Proposta iniciarProposta(PropostaCommand propostaCommand) {

    Proposta proposta =
        Proposta.criarProposta(propostaCommand.getNomeCliente(), propostaCommand.getValor());

    Proposta saved = propostaRepository.insert(proposta);

    domainEventPublisher.publishEvent(
        PropostaIniciada.builder()
            .id(saved.getId())
            .nomeCliente(saved.getCliente().getNome())
            .valor(saved.getValor())
            .occuredOn(LocalDateTime.now())
            .propostaWorkflowId(saved.workflowId())
            .build());

    return saved;
  }

  public Proposta anexarRecomendacaoAnalise(RecomendacaoAnaliseCommand recomendacaoAnaliseCommand) {
    Proposta proposta = propostaRepository.findById(recomendacaoAnaliseCommand.getPropostaId());

    proposta.anexarRecomendacaoAnalise(
        recomendacaoAnaliseCommand.getAnaliseId(),
        recomendacaoAnaliseCommand.getAnalista(),
        recomendacaoAnaliseCommand.getDataRecomendacao(),
        recomendacaoAnaliseCommand.getStatus());
    Proposta saved = propostaRepository.save(proposta);

    domainEventPublisher.publishEvent(
        PropostaAnalisada.builder()
            .id(saved.getId())
            .cliente(saved.getCliente().toBuilder().build())
            .valor(saved.getValor())
            .analista(saved.getRecomendacaoAnalise().getAnalista().toBuilder().build())
            .analiseStatus(saved.getRecomendacaoAnalise().getStatus())
            .propostaWorkflowId(saved.workflowId())
            .occuredOn(LocalDateTime.now())
            .build());

    return saved;
  }

  public List<Proposta> listarPropostas() {

    return propostaRepository.list();
  }

  public Proposta findById(UUID propostaId) {
    return propostaRepository.findById(propostaId);
  }
}
