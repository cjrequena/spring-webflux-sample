package com.cjrequena.sample.fooserverservice.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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
public class StartUpService {

  @PostConstruct
  public void init() {
    log.info("Starting up service ... ");
  }
}
