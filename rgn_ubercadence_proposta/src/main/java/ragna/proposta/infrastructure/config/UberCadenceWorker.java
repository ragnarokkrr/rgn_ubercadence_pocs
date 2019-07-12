package ragna.proposta.infrastructure.config;

import com.uber.cadence.worker.Worker;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;

// @Component
@Slf4j
public class UberCadenceWorker {
  private final List<?> workflowActivities;
  private final String domain;
  private final String taskList;
  private final Class<?>[] workflowImplementationClasses;
  private final ExecutorService executorService;

  UberCadenceWorker(
      String domain,
      String taskList,
      List<Class<?>> workflowImplementationClasses,
      List<?> workflowActivities,
      ExecutorService executorService) {
    this.domain = domain;
    this.taskList = taskList;
    this.workflowImplementationClasses =
        workflowImplementationClasses.toArray(new Class<?>[workflowImplementationClasses.size()]);
    this.executorService = executorService;
    this.workflowActivities = workflowActivities;
  }

  @PostConstruct
  public void init() {
    if (ArrayUtils.isEmpty(workflowImplementationClasses)) {
      log.debug("Nenhum Workflow configurado.");
      return;
    }
    executorService.submit(this::startCadenceWorker);
  }

  @PreDestroy
  public void beanDestroy() {
    if (executorService != null) {
      executorService.shutdownNow();
    }
  }

  private void startCadenceWorker() {
    log.info("WORKER - configurando");
    Worker.Factory factory = new Worker.Factory(domain);
    Worker worker = factory.newWorker(taskList);
    worker.registerWorkflowImplementationTypes(workflowImplementationClasses);
    worker.registerActivitiesImplementations(workflowActivities.toArray());

    log.info(
        "WORKER - iniciando: '{}', '{}', '{}'",
        domain,
        taskList,
        Arrays.asList(workflowImplementationClasses));
    factory.start();
  }
}
