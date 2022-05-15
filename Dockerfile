FROM openjdk:8
VOLUME /tmp
COPY ./target/cloud-disk-0.0.1-SNAPSHOT.jar cloud-disk-0.0.1-SNAPSHOT.jar
EXPOSE 8976
ENTRYPOINT ["java","-jar","-Xmx400m","./cloud-disk-0.0.1-SNAPSHOT.jar","&"]
