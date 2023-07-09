FROM openjdk:17
ADD target/*.jar myapplication
ENTRYPOINT ["java", "-Dserver.port=8300", "-jar","myapplication"]
EXPOSE 8300
