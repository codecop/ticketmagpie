FROM frolvlad/alpine-oraclejdk8:slim
ADD target/ticketmagpie.jar ticketmagpie.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ticketmagpie.jar"]
