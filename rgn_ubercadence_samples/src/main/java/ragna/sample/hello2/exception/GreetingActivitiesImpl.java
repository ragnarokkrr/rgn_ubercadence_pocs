package ragna.sample.hello2.exception;

import com.uber.cadence.workflow.Workflow;
import java.io.IOException;

public class GreetingActivitiesImpl implements GreetingActivities {
  @Override
  public String composeGreeting(String greeting, String name) {
    try {
      throw new IOException(greeting + " - " + name);
    } catch (IOException e) {
      // Wrapping the exception as checked exceptions in activity and workflow interface methods
      // are pohibited.
      // It will be unwrapped and attached as a cause to ActivityFailureException
      throw Workflow.wrap(e);
    }
  }
}
