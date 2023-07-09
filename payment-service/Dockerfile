FROM openjdk:17
ADD target/*.jar myapplication
ADD src/main/resources/application.yml  application.yml
ADD  src/main/resources/bootstrap.yml bootstrap.yml
EXPOSE 8800
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom --spring.config.location=classpath:file:/application.yml,classpath:file:/bootstrap.yml ", "-jar","myapplication"]

