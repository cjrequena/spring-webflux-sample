package com.cjrequena.sample.fooserverservice.exception;

import com.cjrequena.sample.fooserverservice.dto.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 */
@Data
@JsonPropertyOrder(value = {
  "date",
  "status",
  "error_code",
  "message",
  "more_info"
})
@JsonTypeName("error")
@XmlRootElement
public class ErrorDTO implements DTO, Serializable {

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
