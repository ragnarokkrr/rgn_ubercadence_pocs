package ragna.proposta.domain.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ragna.proposta.domain.model.Proposta;

@Repository
@Slf4j
public class PropostaRepository {
  private Map<UUID, Proposta> propostaMap = new HashMap<>();

  public Proposta insert(Proposta proposta) {
    UUID id = generateId();
    proposta.setId(id);

    propostaMap.put(proposta.getId(), proposta);
    return proposta;
  }

  private UUID generateId() {
    return UUID.randomUUID();
  }

  public List<Proposta> list() {

    return propostaMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
  }

  public Proposta findById(UUID propostaId) {
    return propostaMap.get(propostaId);
  }

  public Proposta save(Proposta proposta) {
    propostaMap.put(proposta.getId(), proposta);
    return proposta;
  }
}
