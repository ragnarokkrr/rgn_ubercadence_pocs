package ragna.sample.hello2.exception;

import com.google.common.base.Throwables;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowException;
import com.uber.cadence.client.WorkflowOptions;
import java.time.Duration;
import ragna.common.constants.Domain;

public class HelloExceptionCaller {

  public static void main(String[] args) {
    WorkflowClient workflowClient = WorkflowClient.newInstance(Domain.DOMAIN_EXCEPTION);
    WorkflowOptions workflowOptions =
        new WorkflowOptions.Builder()
            .setTaskList(HelloException.TASK_LIST)
            .setExecutionStartToCloseTimeout(Duration.ofSeconds(30))
            .build();

    GreetingWorkflow workflow =
        workflowClient.newWorkflowStub(GreetingWorkflow.class, workflowOptions);
    try {

      workflow.getGreeting("World");
      throw new IllegalStateException("unreachable");
    } catch (WorkflowException e) {
      Throwable cause = Throwables.getRootCause(e);
      System.out.println(cause.getMessage());
      System.out.println("\nStack Trace:\n" + Throwables.getStackTraceAsString(e));
    }

    System.exit(0);
  }
}
