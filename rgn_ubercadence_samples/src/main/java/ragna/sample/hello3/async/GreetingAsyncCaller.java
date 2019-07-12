package ragna.sample.hello3.async;

import com.uber.cadence.client.WorkflowClient;
import ragna.common.constants.Domain;

public class GreetingAsyncCaller {
  public static void main(String[] args) {
    WorkflowClient workflowClient = WorkflowClient.newInstance(Domain.DOMAIN_ASYNC);
    GreetingWorkflow greetingWorkflow = workflowClient.newWorkflowStub(GreetingWorkflow.class);

    String greeting = greetingWorkflow.getGreeting("World");
    System.out.println(greeting);
    System.exit(0);
  }
}
