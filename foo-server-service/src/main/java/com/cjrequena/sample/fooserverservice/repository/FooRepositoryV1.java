package com.cjrequena.sample.fooserverservice.repository;

import com.cjrequena.sample.fooserverservice.domain.FooEntityV1;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface FooRepositoryV1 extends ReactiveSortingRepository<FooEntityV1, String> {
}
