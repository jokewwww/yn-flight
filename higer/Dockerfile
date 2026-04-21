#FROM cargo.caicloudprivatetest.com/release/java:8u111
#MAINTAINER zcx 739348834@qq.com
#ENV TZ=Asia/Shanghai
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#COPY target/higer-service.war /higer-service.war
#
#ENTRYPOINT java -jar higer-service.war

FROM registry.cn-chengdu.aliyuncs.com/myitsite/java:8

#作者信息
MAINTAINER myitsite<898435128@qq.com>

# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 输入参数，改参数为打包后的jar包，例如 target/com-zh-flight-0.0.1-SNAPSHOT.jar
ARG JAR_FILE

#指定环境变量 工作目录
ENV WORKDIR "/work"

ENV JAR_FILE "target/higer-service.war"

#指定工作目录
WORKDIR ${WORKDIR}

#暴露端口
EXPOSE 30105

EXPOSE 12303

# 添加jar到镜像并命名为app.jar
ADD ${JAR_FILE} ${WORKDIR}/app.war

#启动 如果启动时想要添加额外的配置可以通过docker run -e PARAM="-Dserver.port=8000 -Dspring.profiles.active-prod"
#ENTRYPOINT ["/bin/bash","-c","java -Dfile.encoding=utf8 ${PARAM} -jar ${WORKDIR}/app.jar --ACTIVE='dev' \
#                                                                                         --SERVER_PORT=30105 \
#                                                                                         --REDIS_ROOT_IP='192.168.1.82' \
#                                                                                         --REDIS_ROOT_PASSWORD='brysjhhrhl' \
#                                                                                         --MYSQL_ROOT_IP='192.168.1.82' \
#                                                                                         --MYSQL_USERNAME='root' \
#                                                                                         --MYSQL_ROOT_PASSWORD='brysjhhrhl' \
#                                                                                         --REDIS_ROOT_PORT=6379 \
#                                                                                         --ZK_IP_PORT='192.168.1.82:9092' \
#                                                                                         --REMOTE_LOGIN_IP='192.168.1.82' \
#                                                                                         --REMOTE_LOGIN_PORT=30101 \
#                                                                                         --REMOTE_FLIGHT_IP='192.168.1.82' \
#                                                                                         --REMOTE_FLIGHT_PORT=30100 \
#                                                                                         --REMOTE_JOB_IP='192.168.1.82' \
#                                                                                         --REMOTE_JOB_PORT=30104 \
#"]
ENTRYPOINT ["/bin/bash","-c","java -Dfile.encoding=utf8 ${PARAM} -jar ${WORKDIR}/app.war --ACTIVE='dev' \
                                                                                         --SERVER_PORT=30105 \
"]
