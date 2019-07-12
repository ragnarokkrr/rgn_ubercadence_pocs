package ragna.common.workflow;

import java.util.UUID;
import lombok.*;
import org.springframework.util.Assert;

@RequiredArgsConstructor(staticName = "from")
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class PropostaWorkflowId {
  private @NonNull UUID id;
  private @NonNull String nome;

  public String encodedId() {
    return String.format("PROPOSTA::%s::%s", id, nome);
  }

  public static PropostaWorkflowId parse(String workflowId) {
    Assert.isTrue(workflowId.startsWith("PROPOSTA::"), () -> "Workflow Id invalido!");
    String[] splitted = workflowId.split("::");
    return new PropostaWorkflowId(UUID.fromString(splitted[1]), splitted[2]);
  }
}
