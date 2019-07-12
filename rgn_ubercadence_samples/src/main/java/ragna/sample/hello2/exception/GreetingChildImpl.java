package ragna.sample.hello2.exception;

import com.uber.cadence.activity.ActivityOptions;
import com.uber.cadence.workflow.Workflow;
import java.time.Duration;

public class GreetingChildImpl implements GreetingChild {
  private final GreetingActivities activities =
      Workflow.newActivityStub(
          GreetingActivities.class,
          new ActivityOptions.Builder().setScheduleToCloseTimeout(Duration.ofSeconds(10)).build());

  @Override
  public String composeGreeting(String greeting, String name) {

    return activities.composeGreeting(greeting, name);
  }
}
