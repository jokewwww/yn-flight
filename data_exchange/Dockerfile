FROM cargo.caicloudprivatetest.com/release/java:8u111
MAINTAINER zcx 739348834@qq.com
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY target/exchangeData.jar /exchangeData.jar

ENTRYPOINT ["java", "-jar", "/exchangeData.jar"]