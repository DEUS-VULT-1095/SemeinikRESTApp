FROM openjdk:18

WORKDIR /app

COPY ./target/SemeinikRESTApp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]