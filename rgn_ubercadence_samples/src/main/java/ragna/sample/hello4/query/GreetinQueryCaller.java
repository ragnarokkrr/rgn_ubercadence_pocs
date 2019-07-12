package ragna.sample.hello4.query;

import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import java.time.Duration;
import ragna.common.constants.Domain;

public class GreetinQueryCaller {
  public static void main(String[] args) throws InterruptedException {
    // Start a workflow execution. Usually this is done from another program.
    WorkflowClient workflowClient = WorkflowClient.newInstance(Domain.DOMAIN_QUERY);

    WorkflowOptions workflowOptions =
        new WorkflowOptions.Builder()
            .setTaskList(GreetingQueryWorker.TASK_LIST)
            .setExecutionStartToCloseTimeout(Duration.ofSeconds(30))
            .build();

    GreetingWorkflow workflow =
        workflowClient.newWorkflowStub(GreetingWorkflow.class, workflowOptions);

    // Start workflow asynchronously to not use another thread to query.
    WorkflowClient.start(workflow::createGreeting, "World");
    // After start for getGreeting returns, the workflow is guaranteed to be started.
    // So we can send a signal to it using workflow stub.

    System.out.println(workflow.queryGreeting());
    Thread.sleep(2500);

    System.out.println(workflow.queryGreeting());
    System.exit(0);
  }
}
