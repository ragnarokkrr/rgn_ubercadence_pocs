package ragna.sample.hello5.signal;

import com.uber.cadence.workflow.Workflow;
import java.util.ArrayList;
import java.util.List;

public class GreetingWorkflowImpl implements GreetingWorkflow {

  List<String> messageQueue = new ArrayList<>(10);
  boolean exit = false;

  @Override
  public List<String> getGreetings() {
    List<String> receivedMessages = new ArrayList<>(10);
    while (true) {
      Workflow.await(() -> messageQueue.isEmpty() || exit);
      if (messageQueue.isEmpty() && exit) {
        return receivedMessages;
      }
      String message = messageQueue.remove(0);
      receivedMessages.add(message);
    }
  }

  @Override
  public void waitForName(String name) {
    messageQueue.add("Hello " + name + "!");
  }

  @Override
  public void exit() {
    exit = true;
  }
}
