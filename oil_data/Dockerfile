FROM cargo.caicloudprivatetest.com/release/java:8u111
MAINTAINER zcx 739348834@qq.com
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY target/oil-data-exchange.jar /oil-data-exchange.jar

RUN echo 10.18.100.9 sinopo-web.incnaf.com >> /etc/hosts
RUN echo 10.18.100.9 zuul-sinopo.incnaf.com >> /etc/hosts

ENTRYPOINT ["java", "-jar", "/oil-data-exchange.jar"]