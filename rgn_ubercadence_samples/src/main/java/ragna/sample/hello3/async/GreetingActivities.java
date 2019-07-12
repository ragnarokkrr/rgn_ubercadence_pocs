package ragna.sample.hello3.async;

import com.uber.cadence.activity.ActivityMethod;

public interface GreetingActivities {

  @ActivityMethod(scheduleToCloseTimeoutSeconds = 10)
  String composeGreeting(String greeting, String name);
}
