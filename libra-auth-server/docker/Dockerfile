FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ENV TZ=Asia/Shanghai
ADD libra-auth-server.jar app.jar
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar  /app.jar ${RUN_ARGS}
