FROM openjdk:17

COPY consistenthash-0.0.1-SNAPSHOT.jar /consistenthash-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "consistenthash-0.0.1-SNAPSHOT.jar"]

# docker buildx build --push -t ojt90902/consistenthash-test:latest .
# docker buildx build --push --no-cache -t ojt90902/consistenthash-test:2 .