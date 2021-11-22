package com.cjrequena.sample.fooserverservice.web.rest;

import com.cjrequena.sample.fooserverservice.dto.FooDTOV1;
import com.cjrequena.sample.fooserverservice.exception.service.DBConflictServiceException;
import com.cjrequena.sample.fooserverservice.exception.service.DBNotFoundServiceException;
import com.cjrequena.sample.fooserverservice.exception.web.BadRequestWebException;
import com.cjrequena.sample.fooserverservice.exception.web.ConflictWebException;
import com.cjrequena.sample.fooserverservice.exception.web.NotFoundWebException;
import com.cjrequena.sample.fooserverservice.mapper.FooDtoEntityMapperV1;
import com.cjrequena.sample.fooserverservice.service.FooServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

import static com.cjrequena.sample.fooserverservice.common.Constant.VND_FOO_SERVICE_V1;
import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 */
@SuppressWarnings("unchecked")
@Log4j2
@RestController
@RequestMapping(value = "/foo-server-service")
public class FooResourceV1 {

  public static final String CACHE_CONTROL = "Cache-Control";

  @Autowired
  private FooServiceV1 fooServiceV1;
  @Autowired
  private FooDtoEntityMapperV1 fooDtoEntityMapperV1;

  @Operation(
    summary = "Create a new foo.",
    description = "Create a new foo.",
    parameters = {
      @Parameter(
        name = "Accept-Version",
        required = true,
        in = ParameterIn.HEADER,
        schema = @Schema(
          name = "accept-version",
          type = "string",
          allowableValues = {VND_FOO_SERVICE_V1}
        )
      )
    },
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_NDJSON_VALUE, schema = @Schema(implementation = FooDTOV1.class)))
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "201", description = "Created - The request was successful, we created a new resource and the response body contains the representation."),
      @ApiResponse(responseCode = "204", description = "No Content - The request was successful, we created a new resource and the response body does not contains the representation."),
      @ApiResponse(responseCode = "400", description = "Bad Request - The data given in the POST failed validation. Inspect the response body for details."),
      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
      @ApiResponse(responseCode = "408", description = "Request Timeout"),
      @ApiResponse(responseCode = "409", description = "Conflict - The request could not be processed because of conflict in the request"),
      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't create the resource. Please try again."),
      @ApiResponse(responseCode = "503", description = "Service Unavailable - We are temporarily unable. Please wait for a bit and try again. ")
    }
  )
  @PostMapping(
    path = "/fooes",
    produces = {APPLICATION_NDJSON_VALUE}
  )
  public Mono<ResponseEntity<Void>> create(@Valid @RequestBody FooDTOV1 dto, ServerHttpRequest request, UriComponentsBuilder ucBuilder)
    throws ConflictWebException, BadRequestWebException {
    try {
      if (dto.getId() != null) {
        throw new BadRequestWebException("A new Foo cannot already have an ID " + FooDTOV1.class.getName() + " idexists");
      }
      return fooServiceV1.create(fooDtoEntityMapperV1.toEntity(dto))
        .map(entity -> {
          HttpHeaders headers = new HttpHeaders();
          headers.set(CACHE_CONTROL, "no store, private, max-age=0");
          headers.set("id", entity.getId());
          final URI location = ucBuilder.path(new StringBuilder().append(request.getPath()).append("/{id}").toString()).buildAndExpand(entity.getId()).toUri();
          return ResponseEntity.created(location).headers(headers).build();
        });
    } catch (DBConflictServiceException ex) {
      throw new ConflictWebException();
    }
  }

  @Operation(
    summary = "Get a foo by id.",
    description = "Get a foo by id.",
    parameters = {
      @Parameter(
        name = "accept-version",
        required = true, in =
        ParameterIn.HEADER,
        schema = @Schema(
          name = "Accept-Version",
          type = "string",
          allowableValues = {VND_FOO_SERVICE_V1}
        )
      )
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "OK - The request was successful and the response body contains the representation requested."),
      @ApiResponse(responseCode = "400", description = "Bad Request - The data given in the GET failed validation. Inspect the response body for details."),
      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
      @ApiResponse(responseCode = "404", description = "Not Found"),
      @ApiResponse(responseCode = "408", description = "Request Timeout"),
      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't return the representation due to an internal server error."),
      @ApiResponse(responseCode = "503", description = "Service Unavailable - We are temporarily unable to return the representation. Please wait for a bit and try again."),
    }
  )
  @GetMapping(
    path = "/fooes/{id}",
    produces = {APPLICATION_NDJSON_VALUE}
  )
  public Mono<ResponseEntity<FooDTOV1>> retrieveById(@PathVariable(value = "id") String id) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(CACHE_CONTROL, "no store, private, max-age=0");
    return fooServiceV1.retrieveById(id)
      .map(entity -> ResponseEntity.ok().headers(headers).body(fooDtoEntityMapperV1.toDTO(entity)))
      .onErrorMap(ex -> {
          if (ex instanceof DBNotFoundServiceException) {
            return new NotFoundWebException();
          }
          return ex;
        }
      );
  }

  @Operation(
    summary = "Get a list of fooes.",
    description = "Get a list of fooes.",
    parameters = {
      @Parameter(
        name = "accept-version",
        required = true, in =
        ParameterIn.HEADER,
        schema = @Schema(
          name = "accept-version",
          type = "string",
          allowableValues = {VND_FOO_SERVICE_V1}
        )
      )
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "OK - The request was successful and the response body contains the representation requested."),
      @ApiResponse(responseCode = "400", description = "Bad Request - The data given in the GET failed validation. Inspect the response body for details."),
      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
      @ApiResponse(responseCode = "404", description = "Not Found"),
      @ApiResponse(responseCode = "408", description = "Request Timeout"),
      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't return the representation due to an internal server error."),
      @ApiResponse(responseCode = "503", description = "Service Unavailable - We are temporarily unable to return the representation. Please wait for a bit and try again."),
    }
  )
  @GetMapping(
    path = "/fooes",
    produces = {APPLICATION_NDJSON_VALUE}
  )
  public Mono<ResponseEntity<Flux<FooDTOV1>>> retrieve() {
    //    final List<FooDTOV1> fooDTOV1s = this.fooServiceV1.retrieve().map(entity -> fooDtoEntityMapperV1.toDTO(entity)).collectList().block();
    //    HttpHeaders headers = new HttpHeaders();
    //    headers.set(CACHE_CONTROL, "no store, private, max-age=0");
    //    return Mono.just(ResponseEntity.ok().headers(headers).body(fooDTOV1s));

    HttpHeaders headers = new HttpHeaders();
    headers.set(CACHE_CONTROL, "no store, private, max-age=0");
    final Flux<FooDTOV1> fooDTOV1Flux = this.fooServiceV1.retrieve().map(entity -> fooDtoEntityMapperV1.toDTO(entity));
    return Mono.just(ResponseEntity.ok().headers(headers).body(fooDTOV1Flux));
  }

  @Operation(
    summary = "Update a foo by id.",
    description = "Update a foo by id.",
    parameters = {
      @Parameter(
        name = "accept-version",
        required = true, in =
        ParameterIn.HEADER,
        schema = @Schema(
          name = "accept-version",
          type = "string",
          allowableValues = {VND_FOO_SERVICE_V1}
        )
      )
    },
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FooDTOV1.class)))
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "OK - The request was successful, we updated the resource and the response body contains the representation."),
      @ApiResponse(responseCode = "204", description = "No Content - The request was successful, we created a new resource and the response body does not contains the representation."),
      @ApiResponse(responseCode = "400", description = "Bad Request - The data given in the PUT failed validation. Inspect the response body for details."),
      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
      @ApiResponse(responseCode = "408", description = "Request Timeout"),
      @ApiResponse(responseCode = "409", description = "Conflict - The request could not be processed because of conflict in the request"),
      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't create the resource. Please try again."),
      @ApiResponse(responseCode = "503", description = "Service Unavailable - We are temporarily unable. Please wait for a bit and try again. ")
    }
  )
  @PutMapping(
    path = "/fooes/{id}",
    produces = {APPLICATION_NDJSON_VALUE}
  )
  public Mono<ResponseEntity<Object>> update(@PathVariable(value = "id") String id, @Valid @RequestBody FooDTOV1 dto) {
    dto.setId(id);
    HttpHeaders headers = new HttpHeaders();
    headers.set(CACHE_CONTROL, "no store, private, max-age=0");
    return this.fooServiceV1.update(fooDtoEntityMapperV1.toEntity(dto))
      .map(entity -> ResponseEntity.noContent().headers(headers).build())
      .onErrorMap(ex -> {
          if (ex instanceof DBNotFoundServiceException) {
            return new NotFoundWebException();
          }
          return ex;
        }
      );
  }

  @Operation(
    summary = "Delete a foo by id.",
    description = "Delete a foo by id.",
    parameters = {
      @Parameter(
        name = "accept-version",
        required = true, in =
        ParameterIn.HEADER,
        schema = @Schema(
          name = "accept-version",
          type = "string",
          allowableValues = {VND_FOO_SERVICE_V1}
        )
      )
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "204", description = "OK - The request was successful; the resource was deleted."),
      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
      @ApiResponse(responseCode = "404", description = "Not Found"),
      @ApiResponse(responseCode = "408", description = "Request Timeout"),
      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't delete the resource. Please try again."),
      @ApiResponse(responseCode = "503", description = "Service Unavailable")
    }
  )
  @DeleteMapping(
    path = "/fooes/{id}",
    produces = {APPLICATION_NDJSON_VALUE}
  )
  public Mono<ResponseEntity<Object>> delete(@PathVariable(value = "id") String id) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(CACHE_CONTROL, "no store, private, max-age=0");
    return this.fooServiceV1.delete(id)
      .map(entity -> ResponseEntity.noContent().headers(headers).build())
      .onErrorMap(ex -> {
        if (ex instanceof DBNotFoundServiceException) {
          return new NotFoundWebException();
        } else if (ex instanceof DBConflictServiceException) {
          return new BadRequestWebException();
        } else {
          return ex;
        }
      });
  }
}
