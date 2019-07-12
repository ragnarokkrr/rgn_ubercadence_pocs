package ragna.sample.hello5.signal;

import com.uber.cadence.workflow.SignalMethod;
import com.uber.cadence.workflow.WorkflowMethod;
import java.util.List;

public interface GreetingWorkflow {

  @WorkflowMethod
  List<String> getGreetings();

  @SignalMethod
  void waitForName(String name);

  @SignalMethod
  void exit();
}
