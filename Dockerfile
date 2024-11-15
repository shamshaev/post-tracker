FROM gradle:8.8.0-jdk21

WORKDIR /app

COPY . /app

RUN gradle bootWar

EXPOSE 8080

CMD ["java", "-jar", "build/libs/post-tracker-0.0.1-SNAPSHOT.war"]
