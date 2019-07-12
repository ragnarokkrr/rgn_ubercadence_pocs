package ragna.analise.infrastructure.events;

import java.time.LocalDateTime;

public interface DomainEvent {
  LocalDateTime occuredOn();
}
