package ragna.sample.hello3.async;

public class GreetingActivitiesImpl implements GreetingActivities {
  @Override
  public String composeGreeting(String greeting, String name) {
    return greeting + " " + name;
  }
}
