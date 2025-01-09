FROM openjdk:25-jdk-bullseye
ADD target/springboot3.jar springboot3.jar
ENTRYPOINT ["java" , "-jar" , "/springboot3.jar"]