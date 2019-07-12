package ragna.sample.hello3.async;

import com.uber.cadence.workflow.Async;
import com.uber.cadence.workflow.Promise;
import com.uber.cadence.workflow.Workflow;

public class GreetingWorkflowImpl implements GreetingWorkflow {

  private final GreetingActivities activities = Workflow.newActivityStub(GreetingActivities.class);

  @Override
  public String getGreeting(String name) {
    // Async.invoke takes method  reference and activity parameters and returns Promise.
    Promise<String> hello = Async.function(activities::composeGreeting, "Hello", name);
    Promise<String> bye = Async.function(activities::composeGreeting, "Bye", name);

    // Promise is similar to the Java Future. Promise#get blocks until result is ready.
    return hello.get() + "\n " + bye.get();
  }
}
