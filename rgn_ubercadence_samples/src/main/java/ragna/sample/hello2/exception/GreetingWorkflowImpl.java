package ragna.sample.hello2.exception;

import com.uber.cadence.workflow.Workflow;

public class GreetingWorkflowImpl implements GreetingWorkflow {
  @Override
  public String getGreeting(String name) {
    GreetingChild child = Workflow.newChildWorkflowStub(GreetingChild.class);
    return child.composeGreeting("Hello", name);
  }
}
