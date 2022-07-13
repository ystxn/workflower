FROM openjdk:18-slim-bullseye
WORKDIR /build
COPY ./ext-app/build/libs/*.jar app.jar
RUN jar -xf app.jar && \
    jdeps \
    --ignore-missing-deps \
    --print-module-deps \
    -q \
    --recursive \
    --multi-release 17 \
    --class-path="BOOT-INF/lib/*" \
    --module-path="BOOT-INF/lib/*" \
    ./app.jar > /deps.info

FROM openjdk:17-slim-bullseye
COPY --from=0 /deps.info /deps.info
RUN jlink \
    --verbose \
    --add-modules $(cat /deps.info) \
    --strip-java-debug-attributes \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /jre

FROM debian:bullseye-slim
COPY --from=1 /jre /jre
RUN ln -s /jre/bin/java /bin/java
WORKDIR /data/symphony
COPY ./ext-app/build/libs/*.jar app.jar
COPY ./wdk-bot/workflow-bot-app.jar wdk-bot/workflow-bot-app.jar
COPY ./wdk-bot/application.yaml wdk-bot/application.yaml
ENTRYPOINT [ "java", "-jar", "./app.jar", "--spring.profiles.active=prod" ]
