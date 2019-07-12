package ragna.sample.hello1;

import com.uber.cadence.worker.Worker;
import ragna.common.constants.Domain;

public class HelloActivity {

  static final String TASK_LIST = "HelloActivity";

  public static void main(String[] args) {
    System.out.println("Avengers assemble!");

    // Start a worker that hosts both workflow and activity implementations
    Worker.Factory factory = new Worker.Factory(Domain.DOMAIN);
    Worker worker = factory.newWorker(TASK_LIST);

    // Activities are stateless and thread safe. So a shared instance is used.
    worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class);

    // Actvities are stateless and thread safe. So a shared instance is used.
    worker.registerActivitiesImplementations(new GreetingActivitiesImpl());

    // Start listening to the workflow and activity task list
    factory.start();

    // Start a workflow execution. Usually this is done from another program.

    // ragna.sample.hello1.HelloActivityCaller
  }
}
