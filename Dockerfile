FROM openjdk:12
VOLUME /tmp
EXPOSE 8082
ADD ./target/springboot-service-item-0.0.1-SNAPSHOT.jar service-items.jar
ENTRYPOINT ["java","-jar","/service-items.jar"]