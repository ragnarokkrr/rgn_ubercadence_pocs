package ragna.proposta.infrastructure.events.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ragna.proposta.infrastructure.events.DomainEvent;

@Component
@Slf4j
public class DomainEventPublisher {

  private ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  public DomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  public void publishEvent(final DomainEvent domainEvent) {
    log.info("Publicando Evento: {}", domainEvent);
    DomainEventWrapper domainEventWrapper = new DomainEventWrapper(this, domainEvent);
    applicationEventPublisher.publishEvent(domainEventWrapper);
  }
}
