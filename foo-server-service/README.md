# spring-webflux-sample

````bash
docker build -t cjrequena/foo-server-service .
````

````bash
docker run \
--name foo-server-service \
--net=host \
--restart=always \
-e SERVER_PORT=8080 \
-e SPRING_PROFILES_ACTIVE=local \
-e SPRING_CLOUD_CONSUL_HOST=localhost \
-e SPRING_CLOUD_CONSUL_PORT=8500 \
-e SPRING_CLOUD_CONSUL_ENABLED=true \
-e SPRING_CLOUD_CONSUL_DISCOVERY_REGISTER=true \
-e SPRING_CLOUD_CONSUL_DISCOVERY_REGISTERHEALTHCHECK=true \
-e SPRING_CLOUD_CONSUL_DISCOVERY_HEALTHCHECKINTERVAL=15s \
-e SPRING_CLOUD_CONSUL_DISCOVERY_TAGS=tag1,tag2 \
-e SPRING_CLOUD_CONFIG_ENABLED=false \
-e SPRING_CLOUD_CONFIG_DISCOVERY_ENABLED=false \
-e SPRING_CLOUD_CONFIG_DISCOVERY_SERVICEID=config-server \
-e JVM_OPTS="" \
-v /var/log:/var/log \
-p 8080:8080 \
cjrequena/foo-server-service
````
