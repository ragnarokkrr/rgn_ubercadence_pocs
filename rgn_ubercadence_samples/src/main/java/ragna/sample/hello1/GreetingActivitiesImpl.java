package ragna.sample.hello1;

public class GreetingActivitiesImpl implements GreetingActivities {

  @Override
  public String composeGreeting(String greeting, String name) {
    return greeting + " " + name + "!";
  }
}
