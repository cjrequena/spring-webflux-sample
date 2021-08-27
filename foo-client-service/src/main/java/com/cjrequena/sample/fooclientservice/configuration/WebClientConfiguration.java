package com.cjrequena.sample.fooclientservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
  @Value("${services.foo-server-service.read-timeout}")
  private int fooServerServiceReadTimeout;
  @Value("${services.foo-server-service.connection-timeout}")
  private int fooServerServiceConnectionTimeout;

  @Bean
  //@LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder() {
    ClientHttpConnector connector = getClientHttpConnector(fooServerServiceConnectionTimeout, fooServerServiceReadTimeout);
    final ExchangeStrategies exchangeStrategies = getExchangeStrategies();
    return WebClient
      .builder()
      .clientConnector(connector)
      .exchangeStrategies(exchangeStrategies);
  }

  @Bean("fooServerWebClient")
  //@LoadBalanced
  public WebClient fooServerWebClient() {
    final ClientHttpConnector connector = getClientHttpConnector(fooServerServiceConnectionTimeout, fooServerServiceReadTimeout);
    final ExchangeStrategies exchangeStrategies = getExchangeStrategies();
    return WebClient
      .builder()
      .baseUrl(fooServerServiceUrl)
      .clientConnector(connector)
      .exchangeStrategies(exchangeStrategies)
      .build();
  }

  private ClientHttpConnector getClientHttpConnector(int connectionTimeout, int readTimeout) {
    HttpClient httpClient = HttpClient.create()
      .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
        .doOnConnected(connection -> connection
          .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
          .addHandlerLast(new WriteTimeoutHandler(10))))
      .doOnRequest((request, connection) -> connection.addHandlerFirst(new CustomNettyLogger(HttpClient.class)));
    return new ReactorClientHttpConnector(httpClient);
  }

  private ExchangeStrategies getExchangeStrategies() {
    return ExchangeStrategies.builder()
      .codecs(clientDefaultCodecsConfigurer -> {
        clientDefaultCodecsConfigurer
          .defaultCodecs()
          .jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_STREAM_JSON));
        clientDefaultCodecsConfigurer
          .defaultCodecs()
          .jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_STREAM_JSON));
      }).build();
  }
}
