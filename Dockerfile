FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY Algoritmo/src/App.java .

RUN javac App.java

CMD ["java", "App"]