package ragna.sample.hello4.query;

import com.uber.cadence.workflow.Workflow;
import java.time.Duration;

public class GreetingWorkflowImpl implements GreetingWorkflow {
  private String greeting;

  @Override
  public void createGreeting(String name) {
    greeting = "Hello " + name + "!";
    // Workflow code always uses WorkflowThread.sleep
    // and Workflow.currentTimeMillos instead of standard Java ones.
    Workflow.sleep(Duration.ofSeconds(2));
    greeting = "Bye " + name + "!";
  }

  @Override
  public String queryGreeting() {
    return greeting;
  }
}
