package ragna.sample.hello2.exception;

import com.uber.cadence.worker.Worker;
import ragna.common.constants.Domain;

public class HelloException {
  static final String TASK_LIST = "HelloException";

  public static void main(String[] args) {
    //
    Worker.Factory factory = new Worker.Factory(Domain.DOMAIN_EXCEPTION);
    Worker worker = factory.newWorker(TASK_LIST);
    worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class, GreetingChildImpl.class);
    worker.registerActivitiesImplementations(new GreetingActivitiesImpl());
    factory.start();
  }
}
