FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /build
COPY --chmod=0755 mvnw ./mvnw
COPY .mvn/ .mvn/
COPY pom.xml ./
RUN ./mvnw -B -ntp dependency:go-offline -DskipTests
COPY src/ src/
RUN ./mvnw -B -ntp package -DskipTests

FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app
RUN groupadd --gid 10001 app && useradd --uid 10001 --gid app --no-create-home --shell /usr/sbin/nologin app
COPY --from=build --chown=app:app /build/target/*.jar ./app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
