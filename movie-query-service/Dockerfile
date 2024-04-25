FROM amazoncorretto:17-alpine3.15
ADD target/*.jar myapplication
ENTRYPOINT ["java", "-Dserver.port=8100", "-jar","myapplication"]
EXPOSE 8100

