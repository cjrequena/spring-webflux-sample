package com.cjrequena.sample.fooclientservice.service;

import com.cjrequena.sample.fooclientservice.dto.FooDTOV1;
import com.cjrequena.sample.fooclientservice.exception.service.WebClientBadRequestServiceException;
import com.cjrequena.sample.fooclientservice.exception.service.WebClientConflictServiceException;
import com.cjrequena.sample.fooclientservice.exception.service.WebClientNotFoundServiceException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 * @version 1.0
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class FooServiceV1 {

  private static final String FOO_SERVICE = "foo-service";

  private CircuitBreakerFactory circuitBreakerFactory;
  private WebClient.Builder webClientBuilder;
  private WebClient fooServerWebClient;
  private WebClient lbFooServerWebClient;

  @Autowired
  public FooServiceV1(WebClient.Builder webClientBuilder, CircuitBreakerFactory circuitBreakerFactory, @Qualifier("fooServerWebClient") WebClient fooServerWebClient,
    @Qualifier("lbFooServerWebClient") WebClient lbFooServerWebClient) {
    this.webClientBuilder = webClientBuilder;
    this.circuitBreakerFactory = circuitBreakerFactory;
    this.fooServerWebClient = fooServerWebClient;
    this.lbFooServerWebClient = lbFooServerWebClient;
  }

  //@TimeLimiter(name = FOO_SERVICE)
  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "createFallbackMethod")
  @Bulkhead(name = FOO_SERVICE)
  @Retry(name = FOO_SERVICE)
  public Mono<ResponseEntity<Void>> create(FooDTOV1 dto) {
    return lbFooServerWebClient
      .post()
      .uri("/foo-server-service/fooes/")
      .header(HttpHeaders.CONTENT_TYPE, APPLICATION_NDJSON_VALUE)
      .header("Accept-Version", "vnd.foo-service.v1")
      .body(Mono.just(dto), FooDTOV1.class)
      //.exchangeToMono(response -> Mono.just(response.mutate().build()));
      .retrieve()
      .onStatus(httpStatus -> HttpStatus.CONFLICT.equals(httpStatus), clientResponse -> Mono.error(new WebClientConflictServiceException()))
      .toBodilessEntity()
      .doOnNext(log::info);
  }

  public Mono<ClientResponse> createFallbackMethod(FooDTOV1 dto, Throwable ex) throws Throwable {
    log.debug("createFallbackMethod", ex);
    throw ex;
  }

  //@TimeLimiter(name = FOO_SERVICE)
  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "retrieveByIdFallbackMethod")
  @Bulkhead(name = FOO_SERVICE)
  @Retry(name = FOO_SERVICE)
  public Mono<ResponseEntity<FooDTOV1>> retrieveById(String id) {
    return lbFooServerWebClient
      .get()
      .uri("/foo-server-service/fooes/" + id)
      .header(HttpHeaders.CONTENT_TYPE, APPLICATION_NDJSON_VALUE)
      .header("Accept-Version", "vnd.foo-service.v1")
      .retrieve()
      .onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus), clientResponse -> Mono.error(new WebClientNotFoundServiceException()))
      .toEntity(FooDTOV1.class)
      .doOnNext(log::info);
  }

  public Mono<ResponseEntity<FooDTOV1>> retrieveByIdFallbackMethod(String id, Throwable ex) throws Throwable {
    log.debug("retrieveByIdFallbackMethod");
    throw ex;
  }

  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "retrieveFallbackMethod")
  @Bulkhead(name = FOO_SERVICE)
  @Retry(name = FOO_SERVICE)
  public Mono<ResponseEntity<Flux<FooDTOV1>>> retrieve() {
    return lbFooServerWebClient
      .get()
      .uri("/foo-server-service/fooes/")
      .header(HttpHeaders.CONTENT_TYPE, APPLICATION_NDJSON_VALUE)
      .header("Accept-Version", "vnd.foo-service.v1")
      .retrieve()
      .onStatus(httpStatus -> HttpStatus.BAD_REQUEST.equals(httpStatus), clientResponse -> Mono.error(new WebClientBadRequestServiceException()))
      .toEntityFlux(FooDTOV1.class);
  }

  public Mono<ResponseEntity<Flux<FooDTOV1>>> retrieveFallbackMethod(Throwable ex) throws Throwable {
    log.debug("retrieveFallbackMethod");
    throw ex;
  }

  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "updateFallbackMethod")
  @Bulkhead(name = FOO_SERVICE)
  @Retry(name = FOO_SERVICE)
  public Mono<ResponseEntity<Void>> update(String id, FooDTOV1 dto) {
    return lbFooServerWebClient
      .put()
      .uri("/foo-server-service/fooes/" + id)
      .header(HttpHeaders.CONTENT_TYPE, APPLICATION_NDJSON_VALUE)
      .header("Accept-Version", "vnd.foo-service.v1")
      .body(Mono.just(dto), FooDTOV1.class)
      //.exchangeToMono(response -> Mono.just(response.mutate().build()));
      .retrieve()
      .onStatus(HttpStatus.CONFLICT::equals, clientResponse -> Mono.error(new WebClientConflictServiceException()))
      .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.error(new WebClientNotFoundServiceException()))
      .toEntity(Void.class)
      .doOnNext(log::info);
  }

  public Mono<ResponseEntity<Void>> updateFallbackMethod(String id, FooDTOV1 dto, Throwable ex) throws Throwable {
    log.debug("updateFallbackMethod");
    throw ex;
  }

  //  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "patchFallbackMethod")
  //  @Bulkhead(name = FOO_SERVICE)
  //  @Retry(name = FOO_SERVICE)
  //  public ResponseEntity<Void> patch(Long id, JsonPatch patchDocument) throws WebClientNotFoundServiceException {
  //    return fooServerServiceV1Feign.patch(id, patchDocument);
  //  }

  //  public ResponseEntity<Void> patchFallbackMethod(Long id, JsonPatch patchDocument, Throwable ex) throws WebClientNotFoundServiceException {
  //    log.debug("patchFallbackMethod");
  //    throw (WebClientNotFoundServiceException) ex;
  //  }

  //  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "mergeFallbackMethod")
  //  @Bulkhead(name = FOO_SERVICE)
  //  @Retry(name = FOO_SERVICE)
  //  public ResponseEntity<Void> merge(Long id, JsonMergePatch mergePatchDocument) throws WebClientNotFoundServiceException {
  //    return fooServerServiceV1Feign.patch(id, mergePatchDocument);
  //  }

  //  public ResponseEntity<Void> mergeFallbackMethod(Long id, JsonMergePatch mergePatchDocument, Throwable ex) throws WebClientNotFoundServiceException {
  //    log.debug("mergeFallbackMethod");
  //    throw (WebClientNotFoundServiceException) ex;
  //  }

  //  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "deleteFallbackMethod")
  //  @Bulkhead(name = FOO_SERVICE)
  //  @Retry(name = FOO_SERVICE)
  //  public ResponseEntity<Void> delete(Long id) throws WebClientNotFoundServiceException {
  //    return fooServerServiceV1Feign.delete(id);
  //  }

  //  public ResponseEntity<Void> deleteFallbackMethod(Long id, Throwable ex) throws WebClientNotFoundServiceException {
  //    log.debug("deleteFallbackMethod");
  //    throw (WebClientNotFoundServiceException) ex;
  //  }

}
