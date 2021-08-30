package com.cjrequena.sample.fooclientservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Configuration
public class WebClientConfiguration {
  private final ObjectMapper objectMapper;

  @Value("${services.foo-server-service.url}")
  private String fooServerServiceUrl;
  @Value("${services.foo-server-service.lbUrl}")
  private String lbFooServerServiceUrl;

  @Bean
  public WebClient.Builder webClientBuilder() {
    ClientHttpConnector connector = getClientHttpConnector();
    final ExchangeStrategies exchangeStrategies = getExchangeStrategies();
    return WebClient
      .builder()
      .clientConnector(connector)
      .exchangeStrategies(exchangeStrategies);
  }

  @Bean("lbWebClientBuilder")
  @LoadBalanced
  public WebClient.Builder lbWebClientBuilder() {
    ClientHttpConnector connector = getClientHttpConnector();
    final ExchangeStrategies exchangeStrategies = getExchangeStrategies();
    return WebClient
      .builder()
      .clientConnector(connector)
      .exchangeStrategies(exchangeStrategies);
  }

  @Bean("fooServerWebClient")
  public WebClient fooServerWebClient() {
    final ClientHttpConnector connector = getClientHttpConnector();
    final ExchangeStrategies exchangeStrategies = getExchangeStrategies();
    return WebClient
      .builder()
      .baseUrl(fooServerServiceUrl)
      .clientConnector(connector)
      .exchangeStrategies(exchangeStrategies)
      .build();
  }

  @Bean("lbFooServerWebClient")
  @LoadBalanced
  public WebClient lbFooServerWebClient() {
    final ClientHttpConnector connector = getClientHttpConnector();
    final ExchangeStrategies exchangeStrategies = getExchangeStrategies();
    return WebClient
      .builder()
      .baseUrl(lbFooServerServiceUrl)
      .clientConnector(connector)
      .exchangeStrategies(exchangeStrategies)
      .build();
  }

  private ClientHttpConnector getClientHttpConnector() {
    HttpClient httpClient = HttpClient.create()
      .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
        .doOnConnected(connection -> connection
          .addHandlerLast(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
          .addHandlerLast(new WriteTimeoutHandler(10000, TimeUnit.MILLISECONDS))))
      .doOnRequest((request, connection) -> connection.addHandlerFirst(new CustomNettyLogger(HttpClient.class)));
    return new ReactorClientHttpConnector(httpClient);
  }

  private ExchangeStrategies getExchangeStrategies() {
    return ExchangeStrategies.builder()
      .codecs(clientDefaultCodecsConfigurer -> {
        clientDefaultCodecsConfigurer
          .defaultCodecs()
          .jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_NDJSON));
        clientDefaultCodecsConfigurer
          .defaultCodecs()
          .jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_NDJSON));
      }).build();
  }
}
