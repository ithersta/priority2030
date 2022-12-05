FROM eclipse-temurin:18-jre

USER 1000

ENTRYPOINT ["/priority2030-1.0-SNAPSHOT/bin/priority2030"]

ADD ./build/distributions/priority2030-1.0-SNAPSHOT.tar /

USER root
RUN mkdir -p /app \
    && chown -R 1000 /app
USER 1000
WORKDIR /app
