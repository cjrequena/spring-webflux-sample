package com.cjrequena.sample.fooclientservice.exception;

import com.cjrequena.sample.fooclientservice.dto.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
@Data
@JsonPropertyOrder(value = {
  "date",
  "status",
  "errorCode",
  "message",
  "more_info"
})
@JsonTypeName("error")
@XmlRootElement
public class ErrorDTO implements DTO {

  @JsonProperty(value = "date")
  @Getter(onMethod = @__({@JsonProperty("date")}))
  private String date;

  @JsonProperty(value = "status")
  @Getter(onMethod = @__({@JsonProperty("status")}))
  private int status;

  @JsonProperty(value = "error_code")
  @Getter(onMethod = @__({@JsonProperty("error_code")}))
  private String errorCode;

  @JsonProperty(value = "message")
  @Getter(onMethod = @__({@JsonProperty("message")}))
  private String message;

  @JsonProperty(value = "more_info")
  @Getter(onMethod = @__({@JsonProperty("more_info")}))
  private String moreInfo;

}
