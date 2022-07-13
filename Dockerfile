FROM ghcr.io/graalvm/graalvm-ce:22
WORKDIR /data/symphony
COPY ./ext-app/build/libs/*.jar app.jar
COPY ./wdk-bot/workflow-bot-app.jar wdk-bot/workflow-bot-app.jar
COPY ./wdk-bot/application.yaml wdk-bot/application.yaml
ENTRYPOINT [ "java", "-jar", "./app.jar", "--spring.profiles.active=prod" ]
