package com.cjrequena.sample.fooclientservice.service;

import com.cjrequena.sample.fooclientservice.dto.FooDTOV1;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

  @Autowired
  public FooServiceV1(WebClient.Builder webClientBuilder, CircuitBreakerFactory circuitBreakerFactory, @Qualifier("fooServerWebClient") WebClient fooServerWebClient) {
    this.webClientBuilder = webClientBuilder;
    this.circuitBreakerFactory = circuitBreakerFactory;
    this.fooServerWebClient = fooServerWebClient;
  }

  //@TimeLimiter(name = FOO_SERVICE)
  //@CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "createFallbackMethod")
  @Bulkhead(name = FOO_SERVICE)
  @Retry(name = FOO_SERVICE)
  public Mono<ResponseEntity> create(FooDTOV1 dto) throws Exception {
    log.debug("{}", dto);
    //    return fooServerWebClient
    //      .post()
    //      .uri("/foo-server-service/fooes/")
    //      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_STREAM_JSON_VALUE)
    //      .header("Accept-Version", "vnd.foo-service.v1")
    //      .body(Mono.just(dto), FooDTOV1.class)
    //      .retrieve()
    //      .bodyToMono(ResponseEntity.class)
    //      .onErrorMap(ex -> new InterruptedException(ex.getMessage()))
    //      .doOnNext(log::info);

    final WebClient webClient = webClientBuilder.baseUrl("http:://localhost:9080").build();
    return webClient
      .post()
      .uri("/foo-server-service/fooes/")
      .header("accept-version", "vnd.foo-service.v1")
      .bodyValue(dto)
      .retrieve()
      .bodyToMono(ResponseEntity.class)
      .onErrorMap(ex -> {
       log.error(ex);
       return new RuntimeException(ex);
      })
      .doOnNext(log::info);
  }

  public ResponseEntity<Void> createFallbackMethod(FooDTOV1 dto, Throwable ex) throws Throwable {
    log.debug("createFallbackMethod", ex);
    throw ex;
  }

  //  //@TimeLimiter(name = FOO_SERVICE)
  //  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "retrieveByIdFallbackMethod")
  //  @Bulkhead(name = FOO_SERVICE)
  //  @Retry(name = FOO_SERVICE)
  //  public ResponseEntity<FooDTOV1> retrieveById(Long id) throws FeignNotFoundServiceException {
  //    return fooServerServiceV1Feign.retrieveById(id);
  //  }

  //  public ResponseEntity<FooDTOV1> retrieveByIdFallbackMethod(Long id, Throwable ex) throws FeignNotFoundServiceException {
  //    log.debug("retrieveByIdFallbackMethod");
  //    throw (FeignNotFoundServiceException) ex;
  //  }

  //  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "retrieveFallbackMethod")
  //  @Bulkhead(name = FOO_SERVICE)
  //  @Retry(name = FOO_SERVICE)
  //  public ResponseEntity<List<FooDTOV1>> retrieve(String fields, String filters, String sort, Integer offset, Integer limit) throws FeignBadRequestServiceException {
  //    return fooServerServiceV1Feign.retrieve(fields, filters, sort, offset, limit);
  //  }

  //  public ResponseEntity<List<FooDTOV1>> retrieveFallbackMethod(String fields, String filters, String sort, Integer offset, Integer limit, Throwable ex)
  //    throws FeignBadRequestServiceException {
  //    log.debug("retrieveFallbackMethod");
  //    throw (FeignBadRequestServiceException) ex;
  //  }

  //  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "updateFallbackMethod")
  //  @Bulkhead(name = FOO_SERVICE)
  //  @Retry(name = FOO_SERVICE)
  //  public ResponseEntity<Void> update(Long id, FooDTOV1 dto) throws FeignNotFoundServiceException {
  //    return fooServerServiceV1Feign.update(id, dto);
  //  }

  //  public ResponseEntity<Void> updateFallbackMethod(Long id, FooDTOV1 dto, Throwable ex) throws FeignNotFoundServiceException {
  //    log.debug("updateFallbackMethod");
  //    throw (FeignNotFoundServiceException) ex;
  //  }

  //  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "patchFallbackMethod")
  //  @Bulkhead(name = FOO_SERVICE)
  //  @Retry(name = FOO_SERVICE)
  //  public ResponseEntity<Void> patch(Long id, JsonPatch patchDocument) throws FeignNotFoundServiceException {
  //    return fooServerServiceV1Feign.patch(id, patchDocument);
  //  }

  //  public ResponseEntity<Void> patchFallbackMethod(Long id, JsonPatch patchDocument, Throwable ex) throws FeignNotFoundServiceException {
  //    log.debug("patchFallbackMethod");
  //    throw (FeignNotFoundServiceException) ex;
  //  }

  //  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "mergeFallbackMethod")
  //  @Bulkhead(name = FOO_SERVICE)
  //  @Retry(name = FOO_SERVICE)
  //  public ResponseEntity<Void> merge(Long id, JsonMergePatch mergePatchDocument) throws FeignNotFoundServiceException {
  //    return fooServerServiceV1Feign.patch(id, mergePatchDocument);
  //  }

  //  public ResponseEntity<Void> mergeFallbackMethod(Long id, JsonMergePatch mergePatchDocument, Throwable ex) throws FeignNotFoundServiceException {
  //    log.debug("mergeFallbackMethod");
  //    throw (FeignNotFoundServiceException) ex;
  //  }

  //  @CircuitBreaker(name = FOO_SERVICE, fallbackMethod = "deleteFallbackMethod")
  //  @Bulkhead(name = FOO_SERVICE)
  //  @Retry(name = FOO_SERVICE)
  //  public ResponseEntity<Void> delete(Long id) throws FeignNotFoundServiceException {
  //    return fooServerServiceV1Feign.delete(id);
  //  }

  //  public ResponseEntity<Void> deleteFallbackMethod(Long id, Throwable ex) throws FeignNotFoundServiceException {
  //    log.debug("deleteFallbackMethod");
  //    throw (FeignNotFoundServiceException) ex;
  //  }

}
