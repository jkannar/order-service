FROM amazoncorretto:8
EXPOSE 9002
ARG JAR_FILE=target/order-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
COPY ./wait-for-config.sh /wait-for-config.sh
RUN chmod +x /wait-for-config.sh
ENTRYPOINT ["sh", "/wait-for-config.sh"]