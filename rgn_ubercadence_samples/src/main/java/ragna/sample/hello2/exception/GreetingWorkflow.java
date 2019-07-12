package ragna.sample.hello2.exception;

import com.uber.cadence.workflow.WorkflowMethod;

public interface GreetingWorkflow {
  @WorkflowMethod
  String getGreeting(String name);
}
