FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ENV TZ=Asia/Shanghai
RUN sh -c 'touch /app.jar' && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ADD libra-auth-server.jar app.jar
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
