package ragna.sample.hello5.signal;

import com.uber.cadence.worker.Worker;
import ragna.common.constants.Domain;

public class GreetingSignalWorker {
  static final String TASK_LIST = "HelloSignal";

  public static void main(String[] args) {
    Worker.Factory factory = new Worker.Factory(Domain.DOMAIN_SIGNAL);
    Worker worker = factory.newWorker(TASK_LIST);
    worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class);
    factory.start();
  }
}
