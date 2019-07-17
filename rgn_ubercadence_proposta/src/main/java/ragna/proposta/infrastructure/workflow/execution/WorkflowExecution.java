package ragna.proposta.infrastructure.workflow.execution;

import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import com.uber.cadence.common.RetryOptions;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ragna.proposta.domain.model.PropostaAnalisada;
import ragna.proposta.domain.model.PropostaConcluida;
import ragna.proposta.domain.model.PropostaIniciada;
import ragna.proposta.infrastructure.config.UberCadenceConstants;
import ragna.proposta.infrastructure.workflow.PropostaWorkflow;
import ragna.proposta.infrastructure.workflow.RecomendacaoAnalise;

@Component
@Slf4j
public class WorkflowExecution {

  public void iniciarProposta(PropostaIniciada propostaIniciada) {

    log.info("Configurando Workflow: {}", propostaIniciada.getPropostaWorkflowId().encodedId());
    String propostaWfId = propostaIniciada.getPropostaWorkflowId().encodedId();
    PropostaWorkflow workflow = startPropostaWorkflowStub(propostaWfId);
    WorkflowClient.start(workflow::iniciarPropostaWorkflow, propostaIniciada);
    log.info("Workflow Iniciado: {}", propostaWfId);
  }

  public void anexarRecomendacaoAnalise(RecomendacaoAnalise recomendacaoAnalise) {
    log.info(
        "ANEXAR RECOMENDACAO: Configurando Workflow: {}",
        recomendacaoAnalise.getPropostaWorkflowId());
    String propostaWfId = recomendacaoAnalise.getPropostaWorkflowId().encodedId();

    PropostaWorkflow propostaWorkflow = getPropostaWorkflowStub(propostaWfId);
    propostaWorkflow.receberRecomendacaoAnalise(recomendacaoAnalise);
  }

  public void concluirProposta(PropostaAnalisada propostaAnalisada) {
    log.info(
        "CONCLUIR PROPOSTA: Configurando Workflow: {}", propostaAnalisada.getPropostaWorkflowId());

    String propostaWfId = propostaAnalisada.getPropostaWorkflowId().encodedId();

    PropostaWorkflow propostaWorkflow = getPropostaWorkflowStub(propostaWfId);

    PropostaConcluida propostaConcluida =
        PropostaConcluida.builder()
            .cliente(propostaAnalisada.getCliente())
            .id(propostaAnalisada.getId())
            .occuredOn(propostaAnalisada.occuredOn())
            .valor(propostaAnalisada.getValor())
            .propostaWorkflowId(propostaAnalisada.getPropostaWorkflowId())
            .build();

    propostaWorkflow.concluirProposta(propostaConcluida);
  }

  private PropostaWorkflow startPropostaWorkflowStub(String propostaWfId) {
    WorkflowOptions workflowOptions =
        new WorkflowOptions.Builder()
            .setTaskList(UberCadenceConstants.TASK_LIST)
            .setExecutionStartToCloseTimeout(Duration.ofMinutes(30))
            .setRetryOptions(
                new RetryOptions.Builder()
                    .setInitialInterval(Duration.ofMinutes(10))
                    .setMaximumAttempts(1)
                    .setExpiration(Duration.ofMinutes(30))
                    .setDoNotRetry()
                    .build())
            .setWorkflowId(propostaWfId)
            .build();

    log.info("Iniciando Workflow, '{}'", propostaWfId);
    WorkflowClient workflowClient = WorkflowClient.newInstance(UberCadenceConstants.DOMAIN);
    return workflowClient.newWorkflowStub(PropostaWorkflow.class, workflowOptions);
  }

  private PropostaWorkflow getPropostaWorkflowStub(String propostaWfId) {
    log.info("Reconectando Workflow, '{}'", propostaWfId);
    WorkflowClient workflowClient = WorkflowClient.newInstance(UberCadenceConstants.DOMAIN);
    return workflowClient.newWorkflowStub(PropostaWorkflow.class, propostaWfId);
  }
}
