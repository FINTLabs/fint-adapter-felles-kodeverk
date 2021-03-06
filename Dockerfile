FROM gradle:4.10.2-jdk8-alpine as builder
USER root
COPY . .
ARG apiVersion
RUN gradle --no-daemon -PapiVersion=${apiVersion} build

FROM gcr.io/distroless/java
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
#COPY --from=builder /home/gradle/build/deps/external/*.jar /data/
#COPY --from=builder /home/gradle/build/deps/fint/*.jar /data/
COPY --from=builder /home/gradle/build/libs/fint-adapter-felles-kodeverk-*.jar /data/fint-adapter-felles-kodeverk.jar
CMD ["/data/fint-adapter-felles-kodeverk.jar"]
