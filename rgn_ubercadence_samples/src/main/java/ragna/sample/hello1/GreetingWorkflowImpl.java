package ragna.sample.hello1;

import com.uber.cadence.workflow.Workflow;

public class GreetingWorkflowImpl implements GreetingWorkflow {

  private final GreetingActivities activities = Workflow.newActivityStub(GreetingActivities.class);

  @Override
  public String getGreeting(String name) {
    return activities.composeGreeting("Hello", name);
  }
}
