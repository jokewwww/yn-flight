FROM cargo.caicloudprivatetest.com/release/java:8u111
MAINTAINER zcx 739348834@qq.com
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY target/statistical.jar /statistical.jar

ENTRYPOINT java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 /statistical.jar