package com.cjrequena.sample.fooserverservice.service;

import com.cjrequena.sample.fooserverservice.domain.FooEntityV1;
import com.cjrequena.sample.fooserverservice.exception.service.DBConflictServiceException;
import com.cjrequena.sample.fooserverservice.exception.service.DBNotFoundServiceException;
import com.cjrequena.sample.fooserverservice.mapper.FooDtoEntityMapperV1;
import com.cjrequena.sample.fooserverservice.repository.FooRepositoryV1;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author
 * @version 1.0
 * @see
 * @since JDK1.8
 */
@Log4j2
@Service
@Transactional
public class FooServiceV1 {
  private FooDtoEntityMapperV1 fooDtoEntityMapperV1;
  private FooRepositoryV1 fooRepositoryV1;
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  public FooServiceV1(FooRepositoryV1 fooRepositoryV1, FooDtoEntityMapperV1 fooDtoEntityMapperV1, ApplicationEventPublisher eventPublisher) {
    this.fooRepositoryV1 = fooRepositoryV1;
    this.fooDtoEntityMapperV1 = fooDtoEntityMapperV1;
    this.eventPublisher = eventPublisher;
  }

  public Mono<FooEntityV1> create(FooEntityV1 entity) throws DBConflictServiceException {
    if (entity.getId() != null) {
      throw new DBConflictServiceException("A new Foo cannot already have an ID " + FooEntityV1.class.getName() + " idexists");
    }
    return fooRepositoryV1.save(entity);
  }

  public Mono<FooEntityV1> retrieveById(String id) {
    return fooRepositoryV1.findById(id)
      .switchIfEmpty(Mono.error(new DBNotFoundServiceException("Foo not found by id: " + id)));
  }

  public Flux<FooEntityV1> retrieve() {
    return this.fooRepositoryV1.findAll();
  }

  public Mono<FooEntityV1> update(FooEntityV1 entity) {
    return fooRepositoryV1.findById(entity.getId())
      .flatMap(e -> fooRepositoryV1.save(entity))
      .switchIfEmpty(Mono.error(new DBNotFoundServiceException()));
  }

  public Void delete(String id) throws DBNotFoundServiceException {
    final FooEntityV1 entity = fooRepositoryV1.findById(id).block();
    if (entity != null && entity.getId() != null) {
      return fooRepositoryV1.delete(entity).block();
    } else {
      throw new DBNotFoundServiceException("Foo not found by id " + id);
    }
  }
}

