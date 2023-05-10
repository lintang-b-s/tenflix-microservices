FROM openjdk:17
ARG PROJECT_VERSION=0.1.0
RUN mkdir -p /home/app
WORKDIR /home/app
ENV SPRING_PROFILES_ACTIVE dev

COPY target/*.jar app.jar
COPY . .
EXPOSE 8100

ENTRYPOINT ["java","-jar","/home/app/app.jar"]