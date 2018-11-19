################################################
#                                              #
# 1. Stage: Build from sources                 #
#                                              #
################################################
FROM mozilla/sbt:8u171_1.2.3 as builder

COPY . /app/
WORKDIR /app
RUN sbt clean compile test package

################################################
#                                              #
# 2. Stage: Create Apache Flink Image with jar #
#                                              #
################################################
FROM library/flink:1.6.2
COPY --from=builder /app/target/scala-2.11/flink-case_2.11-0.1-SNAPSHOT.jar /job.jar
COPY docker/start-server.sh /
RUN chmod +x /start-server.sh

# Enable table api support
RUN ln -sf /opt/flink/opt/flink-table_2.11-1.6.2.jar /opt/flink/lib/

ENTRYPOINT ["/start-server.sh"]