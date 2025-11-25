# ===== Stage 1: Build =====
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY ./pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline
COPY ./src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests clean package

# ===== Stage 2: Runtime =====
FROM eclipse-temurin:21-jre

ENV SERVER_PORT=8080
EXPOSE 8080
RUN useradd -ms /bin/sh spring
USER spring
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENV JAVA_OPTS="\
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75 \
-Djava.security.egd=file:/dev/./urandom \
-Duser.timezone=UTC \
"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar --server.port=${SERVER_PORT}"]
