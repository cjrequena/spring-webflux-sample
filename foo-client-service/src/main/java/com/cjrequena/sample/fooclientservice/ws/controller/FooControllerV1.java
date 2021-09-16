package com.cjrequena.sample.fooclientservice.ws.controller;

import com.cjrequena.sample.fooclientservice.common.Constant;
import com.cjrequena.sample.fooclientservice.dto.FooDTOV1;
import com.cjrequena.sample.fooclientservice.exception.web.ConflictWebException;
import com.cjrequena.sample.fooclientservice.service.FooServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

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
@SuppressWarnings("unchecked")
@Log4j2
@RestController
@RequestMapping(value = "/foo-client-service")
public class FooControllerV1 {
  private static final String FOO_SERVICE = "foo-service";
  public static final String CACHE_CONTROL = "Cache-Control";
  public static final String ACCEPT_VERSION_VALUE = "Accept-Version=vnd.foo-service.v1";
  public static final String APPLICATION_JSON_PATCH_VALUE = "application/json-patch+json";
  public static final String APPLICATION_JSON_MERGE_PATCH_VALUE = "application/merge-patch+json";

  @Autowired
  private FooServiceV1 fooServiceV1;

  @Autowired
  @Qualifier("fooServerWebClient")
  private WebClient fooServerWebClient;

  @Autowired
  @Qualifier("lbFooServerWebClient")
  private WebClient lbFooServerWebClient;

  @Operation(
    summary = "Create a new foo.",
    description = "Create a new foo.",
    parameters = {
      @Parameter(
        name = "accept-version",
        required = true,
        in = ParameterIn.HEADER,
        schema = @Schema(
          name = "Accept-Version",
          type = "string",
          allowableValues = {Constant.VND_FOO_SERVICE_V1}
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
  public Mono<ResponseEntity> create(@Valid @RequestBody FooDTOV1 dto, ServerHttpRequest request, UriComponentsBuilder ucBuilder) {
    return fooServiceV1.create(dto).flatMap(response -> {
      HttpHeaders headers = response.headers().asHttpHeaders();
      final URI location = ucBuilder.path(new StringBuilder().append(request.getPath()).append("/{id}").toString()).buildAndExpand(headers.get("id")).toUri();
      switch (response.statusCode()) {
        case CREATED:
          return Mono.just(ResponseEntity.created(location).build());
        case CONFLICT:
          return Mono.error(new ConflictWebException());
        default:
          return Mono.error(new RuntimeException());
      }
    });
  }

  //  @Operation(
  //    summary = "Get a foo by id.",
  //    description = "Get a foo by id.",
  //    parameters = {
  //      @Parameter(
  //        name = "accept-version",
  //        required = true,
  //        in = ParameterIn.HEADER,
  //        schema = @Schema(
  //          name = "accept-version",
  //          type = "string", allowableValues = {Constant.VND_FOO_SERVICE_V1}
  //        )
  //      )
  //    }
  //  )
  //  @ApiResponses(
  //    value = {
  //      @ApiResponse(responseCode = "200", description = "OK - The request was successful and the response body contains the representation requested."),
  //      @ApiResponse(responseCode = "400", description = "Bad Request - The data given in the GET failed validation. Inspect the response body for details."),
  //      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
  //      @ApiResponse(responseCode = "404", description = "Not Found"),
  //      @ApiResponse(responseCode = "408", description = "Request Timeout"),
  //      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
  //      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't return the representation due to an internal server error."),
  //      @ApiResponse(responseCode = "503", description = "Service Unavailable - We are temporarily unable to return the representation. Please wait for a bit and try again."),
  //    }
  //  )
  //  @GetMapping(
  //    path = "/fooes/{id}",
  //    produces = {APPLICATION_JSON_VALUE}
  //  )
  //  public ResponseEntity<FooDTOV1> retrieveById(
  //    @PathVariable(value = "id") Long id) throws NotFoundWebException {
  //    //--
  //    try {
  //      //Headers
  //      HttpHeaders responseHeaders = new HttpHeaders();
  //      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
  //      return this.fooServiceV1.retrieveById(id);
  //    } catch (FeignNotFoundServiceException ex) {
  //      throw new NotFoundWebException(ex.getMessage());
  //    }
  //    //---
  //  }

  //  @Operation(
  //    summary = "Get a list of fooes.",
  //    description = "Get a list of fooes.",
  //    parameters = {
  //      @Parameter(name = "accept-version",
  //        required = true,
  //        in = ParameterIn.HEADER,
  //        schema = @Schema(
  //          name = "accept-version",
  //          type = "string",
  //          allowableValues = {Constant.VND_FOO_SERVICE_V1}
  //        )
  //      )
  //    }
  //  )
  //  @ApiResponses(
  //    value = {
  //      @ApiResponse(responseCode = "200", description = "OK - The request was successful and the response body contains the representation requested."),
  //      @ApiResponse(responseCode = "400", description = "Bad Request - The data given in the GET failed validation. Inspect the response body for details."),
  //      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
  //      @ApiResponse(responseCode = "404", description = "Not Found"),
  //      @ApiResponse(responseCode = "408", description = "Request Timeout"),
  //      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
  //      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't return the representation due to an internal server error."),
  //      @ApiResponse(responseCode = "503", description = "Service Unavailable - We are temporarily unable to return the representation. Please wait for a bit and try again."),
  //    }
  //  )
  //  @GetMapping(
  //    path = "/fooes",
  //    produces = {APPLICATION_JSON_VALUE}
  //  )
  //  public ResponseEntity<List<FooDTOV1>> retrieve(
  //    @RequestParam(value = "fields", required = false) String fields,
  //    @RequestParam(value = "filters", required = false) String filters,
  //    @RequestParam(value = "sort", required = false) String sort,
  //    @RequestParam(value = "offset", required = false) Integer offset,
  //    @RequestParam(value = "limit", required = false) Integer limit
  //  ) throws BadRequestWebException {
  //    //--
  //    try {
  //      log.debug("fields: {} ", fields);
  //      log.debug("filters: {} ", filters);
  //      log.debug("sort: {} ", sort);
  //      log.debug("offset: {} ", offset);
  //      log.debug("limit: {} ", limit);
  //      return this.fooServiceV1.retrieve(fields, filters, sort, offset, limit);
  //    } catch (FeignBadRequestServiceException ex) {
  //      throw new BadRequestWebException(ex.getMessage());
  //    }
  //    //--
  //  }

  //  @Operation(
  //    summary = "Update a foo by id.",
  //    description = "Update a foo by id.",
  //    parameters = {
  //      @Parameter(
  //        name = "accept-version",
  //        required = true, in = ParameterIn.HEADER,
  //        schema = @Schema(
  //          name = "accept-version",
  //          type = "string",
  //          allowableValues = {Constant.VND_FOO_SERVICE_V1}
  //        )
  //      )
  //    },
  //    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FooDTOV1.class)))
  //  )
  //  @ApiResponses(
  //    value = {
  //      @ApiResponse(responseCode = "200", description = "OK - The request was successful, we updated the resource and the response body contains the representation."),
  //      @ApiResponse(responseCode = "204", description = "No Content - The request was successful, we created a new resource and the response body does not contains the representation."),
  //      @ApiResponse(responseCode = "400", description = "Bad Request - The data given in the PUT failed validation. Inspect the response body for details."),
  //      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
  //      @ApiResponse(responseCode = "408", description = "Request Timeout"),
  //      @ApiResponse(responseCode = "409", description = "Conflict - The request could not be processed because of conflict in the request"),
  //      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
  //      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't create the resource. Please try again."),
  //      @ApiResponse(responseCode = "503", description = "Service Unavailable - We are temporarily unable. Please wait for a bit and try again. ")
  //    }
  //  )
  //  @PutMapping(
  //    path = "/fooes/{id}",
  //    produces = {APPLICATION_JSON_VALUE}
  //  )
  //  public ResponseEntity<Void> update(
  //    @PathVariable(value = "id") Long id,
  //    @Valid @RequestBody FooDTOV1 dto,
  //    BindingResult bindingResult) throws NotFoundWebException {
  //    //--
  //    try {
  //      this.fooServiceV1.update(id, dto);
  //      //Headers
  //      HttpHeaders responseHeaders = new HttpHeaders();
  //      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
  //      return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
  //    } catch (FeignNotFoundServiceException ex) {
  //      throw new NotFoundWebException(ex.getMessage());
  //    }
  //    //---
  //  }

  //  @Operation(
  //    summary = "Patch a foo by id.",
  //    description = "Patch a foo by id.",
  //    parameters = {
  //      @Parameter(
  //        name = "accept-version",
  //        required = true,
  //        in = ParameterIn.HEADER,
  //        schema = @Schema(
  //          name = "accept-version",
  //          type = "string",
  //          allowableValues = {Constant.VND_FOO_SERVICE_V1}
  //        )
  //      )
  //    },
  //    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON_PATCH_VALUE, schema = @Schema(implementation = JsonPatch.class)))
  //  )
  //  @ApiResponses(
  //    value = {
  //      @ApiResponse(responseCode = "200", description = "OK - The request was successful, we updated the resource and the response body contains the representation."),
  //      @ApiResponse(responseCode = "204", description = "No Content - The request was successful, we created a new resource and the response body does not contains the representation."),
  //      @ApiResponse(responseCode = "400", description = "Bad Request - The data given in the PATCH failed validation. Inspect the response body for details."),
  //      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
  //      @ApiResponse(responseCode = "404", description = "Not Found"),
  //      @ApiResponse(responseCode = "408", description = "Request Timeout"),
  //      @ApiResponse(responseCode = "409", description = "Conflict - The request could not be processed because of conflict in the request"),
  //      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
  //      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't create the resource. Please try again."),
  //      @ApiResponse(responseCode = "503", description = "Service Unavailable - We are temporarily unable. Please wait for a bit and try again. ")
  //    }
  //  )
  //  @PatchMapping(
  //    path = "/fooes/{id}",
  //    produces = {APPLICATION_JSON_VALUE},
  //    consumes = {APPLICATION_JSON_PATCH_VALUE}
  //  )
  //  public ResponseEntity<Void> patch(
  //    @PathVariable(value = "id") Long id,
  //    @RequestBody JsonPatch patchDocument,
  //    UriComponentsBuilder ucBuilder) throws NotFoundWebException {
  //    //--
  //    try {
  //
  //      this.fooServiceV1.patch(id, patchDocument);
  //      //Headers
  //      HttpHeaders responseHeaders = new HttpHeaders();
  //      responseHeaders.setLocation(ucBuilder.path("/fooes/{id}").buildAndExpand(id).toUri());
  //      return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
  //    } catch (FeignNotFoundServiceException ex) {
  //      throw new NotFoundWebException(ex.getMessage());
  //    }
  //    //---
  //  }

  //  @Operation(
  //    summary = "Merge a foo by id.",
  //    description = "Merge a foo by id.",
  //    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON_MERGE_PATCH_VALUE, schema = @Schema(implementation = JsonMergePatch.class)))
  //  )
  //  @ApiResponses(
  //    value = {
  //      @ApiResponse(responseCode = "200", description = "OK - The request was successful, we updated the resource and the response body contains the representation."),
  //      @ApiResponse(responseCode = "204", description = "No Content - The request was successful, we created a new resource and the response body does not contains the representation."),
  //      @ApiResponse(responseCode = "400", description = "Bad Request - The data given in the PATCH failed validation. Inspect the response body for details."),
  //      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
  //      @ApiResponse(responseCode = "408", description = "Request Timeout"),
  //      @ApiResponse(responseCode = "409", description = "Conflict - The request could not be processed because of conflict in the request"),
  //      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
  //      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't create the resource. Please try again."),
  //      @ApiResponse(responseCode = "503", description = "Service Unavailable - We are temporarily unable. Please wait for a bit and try again. ")
  //    }
  //  )
  //  @PatchMapping(
  //    path = "/fooes/{id}",
  //    produces = {APPLICATION_JSON_VALUE},
  //    consumes = {APPLICATION_JSON_MERGE_PATCH_VALUE}
  //  )
  //  public ResponseEntity<Void> patch(
  //    @PathVariable(value = "id") Long id,
  //    @RequestBody JsonMergePatch mergePatchDocument,
  //    UriComponentsBuilder ucBuilder) throws NotFoundWebException {
  //    //--
  //    try {
  //      this.fooServiceV1.merge(id, mergePatchDocument);
  //      //Headers
  //      HttpHeaders responseHeaders = new HttpHeaders();
  //      responseHeaders.setLocation(ucBuilder.path("/fooes/{id}").buildAndExpand(id).toUri());
  //      return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
  //    } catch (FeignNotFoundServiceException ex) {
  //      throw new NotFoundWebException(ex.getMessage());
  //    }
  //    //---
  //  }

  //  @Operation(
  //    summary = "Delete a foo by id.",
  //    description = "Delete a foo by id.",
  //    parameters = {
  //      @Parameter(
  //        name = "accept-version",
  //        required = true, in = ParameterIn.HEADER,
  //        schema = @Schema(
  //          name = "accept-version",
  //          type = "string",
  //          allowableValues = {Constant.VND_FOO_SERVICE_V1}
  //        )
  //      )
  //    }
  //  )
  //  @ApiResponses(
  //    value = {
  //      @ApiResponse(responseCode = "204", description = "OK - The request was successful; the resource was deleted."),
  //      @ApiResponse(responseCode = "401", description = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
  //      @ApiResponse(responseCode = "404", description = "Not Found"),
  //      @ApiResponse(responseCode = "408", description = "Request Timeout"),
  //      @ApiResponse(responseCode = "429", description = "Too Many Requests - Your application is sending too many simultaneous requests."),
  //      @ApiResponse(responseCode = "500", description = "Internal Server Error - We couldn't delete the resource. Please try again."),
  //      @ApiResponse(responseCode = "503", description = "Service Unavailable")
  //    }
  //  )
  //  @DeleteMapping(
  //    path = "/fooes/{id}",
  //    produces = {APPLICATION_JSON_VALUE}
  //  )
  //  public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) throws NotFoundWebException {
  //    //--
  //    try {
  //      //Headers
  //      HttpHeaders responseHeaders = new HttpHeaders();
  //      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
  //      this.fooServiceV1.delete(id);
  //      return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
  //    } catch (FeignNotFoundServiceException ex) {
  //      throw new NotFoundWebException(ex.getMessage());
  //    }
  //    //---
  //  }

}
