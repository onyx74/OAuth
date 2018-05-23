FROM  openjdk:8u171-jdk

COPY service-registry-0.0.6.jar /usr/src/
WORKDIR /usr/src/

EXPOSE 8761

CMD ["java", "-jar", "service-registry-0.0.6.jar"]
