package ragna.proposta.infrastructure.workflow.execution;

import static com.google.common.base.Predicates.instanceOf;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.None;

import io.vavr.control.Option.None;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ragna.proposta.domain.model.PropostaAnalisada;
import ragna.proposta.domain.model.PropostaIniciada;
import ragna.proposta.infrastructure.events.DomainEvent;
import ragna.proposta.infrastructure.events.spring.DomainEventWrapper;

@Component
@Slf4j
public class WorkflowEventListeners implements ApplicationListener<DomainEventWrapper> {

  private WorkflowExecution workflowExecution;

  @Autowired
  public WorkflowEventListeners(WorkflowExecution workflowExecution) {
    this.workflowExecution = workflowExecution;
  }

  @Override
  public void onApplicationEvent(DomainEventWrapper event) {
    log.info("WorkflowEventListeners");
    DomainEvent domainEvent = event.getDomainEvent();

    Match(domainEvent)
        .of(
            Case($(instanceOf(PropostaIniciada.class)), this::handlePropostaIniciada),
            Case($(instanceOf(PropostaAnalisada.class)), this::handlePropostaAnalisada),
            Case($(), this::handleDefault));
  }

  private None<Object> handleDefault(DomainEvent o) {
    log.info("evento (NO ACTION): {}", o);
    return None();
  }

  private Object handlePropostaIniciada(PropostaIniciada propostaIniciada) {
    workflowExecution.iniciarProposta(propostaIniciada);
    return propostaIniciada;
  }

  private Object handlePropostaAnalisada(PropostaAnalisada propostaAnalisada) {
    workflowExecution.concluirProposta(propostaAnalisada);
    return propostaAnalisada;
  }
}
