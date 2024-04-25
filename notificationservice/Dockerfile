FROM openjdk:17-alpine
ADD target/*.jar myapplication
ENTRYPOINT ["java", "-Dserver.port=8300", "-jar","myapplication"]
EXPOSE 8300
