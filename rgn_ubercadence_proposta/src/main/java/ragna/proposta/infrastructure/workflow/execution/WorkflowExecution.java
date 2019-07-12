package ragna.proposta.infrastructure.workflow.execution;

import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
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
    PropostaWorkflow workflow = newPropostaWorkflowStub(propostaWfId);
    WorkflowClient.start(workflow::iniciarPropostaWorkflow, propostaIniciada);
    log.info("Workflow Iniciado: {}", propostaWfId);
  }

  public void anexarRecomendacaoAnalise(RecomendacaoAnalise recomendacaoAnalise) {
    log.info("Configurando Workflow: {}", recomendacaoAnalise.getPropostaWorkflowId());
    String propostaWfId = recomendacaoAnalise.getPropostaWorkflowId().encodedId();

    PropostaWorkflow propostaWorkflow = newPropostaWorkflowStub(propostaWfId);
    propostaWorkflow.receberRecomendacaoAnalise(recomendacaoAnalise);
  }

  public void concluirProposta(PropostaAnalisada propostaAnalisada) {
    String propostaWfId = propostaAnalisada.getPropostaWorkflowId().encodedId();

    PropostaWorkflow propostaWorkflow = newPropostaWorkflowStub(propostaWfId);
    PropostaConcluida propostaConcluida =
        PropostaConcluida.builder()
            .cliente(propostaAnalisada.getCliente())
            .id(propostaAnalisada.getId())
            .occuredOn(propostaAnalisada.occuredOn())
            .valor(propostaAnalisada.getValor())
            .build();

    propostaWorkflow.concluirProposta(propostaConcluida);
  }

  private PropostaWorkflow newPropostaWorkflowStub(String propostaWfId) {
    WorkflowOptions workflowOptions =
        new WorkflowOptions.Builder()
            .setTaskList(UberCadenceConstants.TASK_LIST)
            .setExecutionStartToCloseTimeout(Duration.ofSeconds(30))
            .setWorkflowId(propostaWfId)
            .build();

    log.info("Iniciando Workflow, '{}'", propostaWfId);
    WorkflowClient workflowClient = WorkflowClient.newInstance(UberCadenceConstants.DOMAIN);
    return workflowClient.newWorkflowStub(PropostaWorkflow.class, workflowOptions);
  }
}
