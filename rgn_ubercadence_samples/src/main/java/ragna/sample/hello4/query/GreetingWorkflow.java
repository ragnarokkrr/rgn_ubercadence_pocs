package ragna.sample.hello4.query;

import com.uber.cadence.workflow.QueryMethod;
import com.uber.cadence.workflow.WorkflowMethod;

public interface GreetingWorkflow {

  @WorkflowMethod
  void createGreeting(String name);

  @QueryMethod
  String queryGreeting();
}
