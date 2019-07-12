package ragna.analise.domain.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ragna.analise.domain.model.Analise;

@Repository
@Slf4j
public class AnaliseRepository {
  private Map<UUID, Analise> analiseMap = new HashMap<>();

  public Analise insert(Analise analise) {
    UUID id = UUID.randomUUID();
    analise.setId(id);
    return update(analise);
  }

  private Analise update(Analise analise) {
    analiseMap.put(analise.getId(), analise);
    return analise;
  }

  public List<Analise> list() {
    return analiseMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
  }

  public Analise findById(UUID id) {
    return analiseMap.get(id);
  }

  public Analise save(Analise analise) {
    // emulate merge
    analiseMap.put(analise.getId(), analise);
    return analise;
  }
}
