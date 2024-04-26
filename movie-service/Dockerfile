FROM amazoncorretto:17-alpine3.15
ADD target/*.jar myapplication
ADD src/main/resources/application.yml  application.yml
ADD  src/main/resources/bootstrap.yml bootstrap.yml

# fix kafka consumer error
RUN apk update && \
  apk add --no-cache libc6-compat && \
  ln -s /lib/libc.musl-x86_64.so.1 /lib/ld-linux-x86-64.so.2 
EXPOSE 8200

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom --spring.config.location=classpath:file:/application.yml,classpath:file:/bootstrap.yml ", "-jar","myapplication"]
