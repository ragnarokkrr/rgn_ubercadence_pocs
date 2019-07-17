package ragna.proposta.infrastructure.workflow;

import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import com.uber.cadence.testing.TestWorkflowEnvironment;
import com.uber.cadence.worker.Worker;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ragna.common.workflow.PropostaWorkflowId;
import ragna.proposta.application.messaging.analise.AnaliseRequestProducer;
import ragna.proposta.domain.model.Cliente;
import ragna.proposta.domain.model.PropostaConcluida;
import ragna.proposta.domain.model.PropostaIniciada;
import ragna.proposta.domain.repository.PropostaRepository;
import ragna.proposta.domain.service.PropostaService;
import ragna.proposta.infrastructure.config.UberCadenceConstants;
import ragna.proposta.infrastructure.events.spring.DomainEventPublisher;

@RunWith(MockitoJUnitRunner.class)
public class PropostaWorkflowTest {
  private TestWorkflowEnvironment testEnv;
  /** Prints a history of the workflow under test in case of a test failure. */
  @Rule
  public TestWatcher watchman =
      new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
          if (testEnv != null) {
            System.err.println(testEnv.getDiagnostics());
            testEnv.close();
          }
        }
      };

  private Worker worker;
  private WorkflowClient workflowClient;

  @Mock private PropostaRepository propostaRepository;

  @Mock private DomainEventPublisher domainEventPublisher;

  @Mock private AnaliseRequestProducer analiseRequestProducer;

  @InjectMocks private PropostaService propostaService;

  @Before
  public void setUp() {

    PropostaActivities propostaActivities =
        new PropostaActivitiesImpl(propostaService, analiseRequestProducer);

    testEnv = TestWorkflowEnvironment.newInstance();

    worker = testEnv.newWorker(UberCadenceConstants.TASK_LIST);
    worker.registerWorkflowImplementationTypes(PropostaWorkflowImpl.class);
    worker.registerActivitiesImplementations(propostaActivities);

    workflowClient = testEnv.newWorkflowClient();
  }

  @After
  public void tearDown() {
    testEnv.close();
  }

  @Test
  public void whenHappyPath_beHappy() {
    // Get a workflow stub using the same task list the worker uses.
    WorkflowOptions workflowOptions =
        new WorkflowOptions.Builder()
            .setTaskList(UberCadenceConstants.TASK_LIST)
            .setExecutionStartToCloseTimeout(Duration.ofDays(30))
            .build();

    UUID propostaId = UUID.randomUUID();
    UUID analiseId = UUID.randomUUID();

    PropostaWorkflow propostaWorkflow =
        workflowClient.newWorkflowStub(PropostaWorkflow.class, workflowOptions);

    PropostaWorkflowId workflowId = PropostaWorkflowId.from(propostaId, "John");
    LocalDateTime now = LocalDateTime.now();
    PropostaIniciada propostaIniciada =
        PropostaIniciada.builder()
            .id(propostaId)
            .nomeCliente("John")
            .occuredOn(now)
            .propostaWorkflowId(workflowId)
            .valor(BigDecimal.TEN)
            .build();

    WorkflowClient.start(propostaWorkflow::iniciarPropostaWorkflow, propostaIniciada);

    RecomendacaoAnalise recomendacaoAnalise =
        RecomendacaoAnalise.builder()
            .propostaWorkflowId(workflowId)
            .status("Status")
            .occuredOn(now)
            .observacao("Obs")
            .propostaId(propostaId)
            .id(analiseId)
            .dataStatus(now)
            .analista("John Connor")
            .build();

    propostaWorkflow.receberRecomendacaoAnalise(recomendacaoAnalise);

    PropostaConcluida propostaConcluida =
        PropostaConcluida.builder()
            .propostaWorkflowId(workflowId)
            .valor(BigDecimal.TEN)
            .occuredOn(now)
            .id(propostaId)
            .cliente(Cliente.builder().nome("John").build())
            .build();

    // TODO assertions before WF finishes
    propostaWorkflow.concluirProposta(propostaConcluida);
  }
}
