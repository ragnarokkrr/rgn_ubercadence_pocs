package ragna.sample.hello2.exception;

import com.uber.cadence.workflow.WorkflowMethod;

public interface GreetingChild {

  @WorkflowMethod
  String composeGreeting(String greeting, String name);
}
