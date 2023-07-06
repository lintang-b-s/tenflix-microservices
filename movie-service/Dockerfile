FROM openjdk:17
ADD .env .env
ADD target/*.jar myapplication
ENTRYPOINT ["java", "-jar","myapplication"]
EXPOSE 8200

