package com.cjrequena.sample.fooserverservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 */
@Getter
@Setter
@Document
@AllArgsConstructor
@ToString
public class FooEntityV1 {

  @Id
  private String id;
  private String name;
  private String description;
  private LocalDate creationDate;


}
