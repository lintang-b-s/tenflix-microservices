FROM openjdk:17
ARG PROJECT_VERSION=0.1.0
RUN mkdir -p /home/app
WORKDIR /home/app
ENV SPRING_PROFILES_ACTIVE dev
COPY . .
COPY target/*.jar app.jar
EXPOSE 8300


ENTRYPOINT ["java","-jar","/home/app/app.jar"]