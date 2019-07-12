package ragna.sample.hello3.async;

import com.uber.cadence.worker.Worker;
import ragna.common.constants.Domain;

public class GreetingAsyncWorker {
  static final String TASK_LIST = "HelloAsync";

  public static void main(String[] args) {
    Worker.Factory factory = new Worker.Factory(Domain.DOMAIN_ASYNC);
    Worker worker = factory.newWorker(TASK_LIST);
    worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class);
    worker.registerActivitiesImplementations(new GreetingActivitiesImpl());
    factory.start();
  }
}
