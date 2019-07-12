package ragna.sample.hello1;

import com.uber.cadence.client.WorkflowClient;
import ragna.common.constants.Domain;

public class HelloActivityCaller {
  public static void main(String[] args) {

    // Start a workflow execution
    WorkflowClient workflowClient = WorkflowClient.newInstance(Domain.DOMAIN);

    // Get a workflow stub using the same task list the worker uses.
    GreetingWorkflow workflow = workflowClient.newWorkflowStub(GreetingWorkflow.class);

    // Execute a workflow waiting for it to complete.
    String greeting = workflow.getGreeting("World!");

    System.out.println("RESULT: " + greeting);
    System.exit(0);
  }
}
