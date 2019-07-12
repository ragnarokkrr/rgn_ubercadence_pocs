package ragna.sample.hello3.async;

import com.uber.cadence.workflow.WorkflowMethod;

public interface GreetingWorkflow {

  @WorkflowMethod(
      executionStartToCloseTimeoutSeconds = 15,
      taskList = GreetingAsyncWorker.TASK_LIST)
  String getGreeting(String name);
}
