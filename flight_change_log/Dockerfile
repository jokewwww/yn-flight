FROM cargo.caicloudprivatetest.com/release/java:8u111
MAINTAINER zcx 739348834@qq.com
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY target/flight_change_log.jar /flight_change_log.jar

ENTRYPOINT java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 /flight_change_log.jar
