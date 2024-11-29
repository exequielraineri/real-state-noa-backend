FROM openjdk:17
ARG JAR_FILE=target/api-inmobiliaria.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 9595

