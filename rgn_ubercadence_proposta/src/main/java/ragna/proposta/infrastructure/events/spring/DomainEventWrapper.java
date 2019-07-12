package ragna.proposta.infrastructure.events.spring;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import ragna.proposta.infrastructure.events.DomainEvent;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class DomainEventWrapper extends ApplicationEvent {
  private final DomainEvent domainEvent;

  /**
   * Create a new DomainEventWrapper.
   *
   * @param source the object on which the event initially occurred (never {@code null})
   */
  public DomainEventWrapper(Object source, DomainEvent domainEvent) {
    super(source);
    this.domainEvent = domainEvent;
  }
}
