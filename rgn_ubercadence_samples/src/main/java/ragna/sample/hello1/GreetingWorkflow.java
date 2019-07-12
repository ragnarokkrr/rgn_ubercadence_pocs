package ragna.sample.hello1;

import com.uber.cadence.workflow.WorkflowMethod;

public interface GreetingWorkflow {

  @WorkflowMethod(executionStartToCloseTimeoutSeconds = 100, taskList = HelloActivity.TASK_LIST)
  String getGreeting(String name);
}
