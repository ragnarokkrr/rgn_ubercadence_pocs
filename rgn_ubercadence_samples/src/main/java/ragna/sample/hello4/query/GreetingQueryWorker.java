package ragna.sample.hello4.query;

import com.uber.cadence.worker.Worker;
import ragna.common.constants.Domain;

public class GreetingQueryWorker {

  static final String TASK_LIST = "HelloQuery";

  public static void main(String[] args) {
    Worker.Factory factory = new Worker.Factory(Domain.DOMAIN_QUERY);
    Worker worker = factory.newWorker(TASK_LIST);
    worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class);
    factory.start();
  }
}
