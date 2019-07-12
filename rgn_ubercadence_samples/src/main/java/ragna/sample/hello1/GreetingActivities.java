package ragna.sample.hello1;

import com.uber.cadence.activity.ActivityMethod;

public interface GreetingActivities {

  @ActivityMethod(scheduleToCloseTimeoutSeconds = 2)
  String composeGreeting(String greeting, String name);
}
