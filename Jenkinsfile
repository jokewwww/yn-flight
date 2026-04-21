//定义gitlab的凭证
 def git_auth="bd8bf773-b12d-4806-81cf-830a4415d5f8"
 //定义gitlab的URL路径
 def git_url="http://dgtsc.cn:9090/cnaf/zhoil.git"

//镜像标签
def tag="latest"
//harbor的url地址
def harbor_url="registry.cn-chengdu.aliyuncs.com"
//镜像仓库名
def harbor_name="myitsite"
//harbor凭证
def harbor_auth="aliyun-container"

 node {
     //选择的微服务项目名称
     def selectedProjectNames="${project_name}".split(",")
     echo "${selectedProjectNames}"
     //获取当前选择服务器
//      def selectedServers="${publish_server}".split(",")

    stage("checkout mvn") {
        sh "${MAVEN_HOME}/bin/mvn --version"
    }
    stage("check java") {
        sh "${JAVA_HOME}/bin/java -version"
    }

     stage('pull code') {
        checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], extensions: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
     }
     stage('check code') {
         //循环检查
     }
     //添加公共子工程
     stage('make install public sub project') {
            sh "mvn -f zhoil_common clean install -DskipTests"
     }
     //编译打包微服务，制作镜像
     stage('make package') {
        for (int i=0;i<selectedProjectNames.length;i++){
            //项目名称tensquare_eureka_server@10086
            def projectInfo=selectedProjectNames[i]

            //当前项目名称
            def currentProjectName=projectInfo.split("@")[0]
            //当前端口
            def currentProjectPort=projectInfo.split("@")[1]

            sh "mvn -f ${currentProjectName} clean package dockerfile:build -DskipTests"
            //定义镜像名称
            def imageName="${currentProjectName}:${tag}"
            //对镜像打标签
            sh "docker tag ${imageName} ${harbor_url}/${harbor_name}/${imageName}"
            //镜像推送到harbor
            withCredentials([usernamePassword(credentialsId: "${harbor_auth}", passwordVariable: 'Password', usernameVariable: 'Username')]) {
                //登录harbor
                sh "docker login --username=${Username} --password='${Password}' ${harbor_url}"
                //镜像上传
                sh "docker push ${harbor_url}/${harbor_name}/${imageName}"
                sh "echo 镜像上传成功啦"
            }
            //删除本地镜像 （根据镜像名称删除镜像和打标签的镜像，因为原本的镜像和打包后镜像是同一个镜像id,无法通过镜像id删除镜像）
            sh "docker rmi -f ${imageName}"
            sh "docker stop ${currentProjectName} || true"
            sh "docker rm ${currentProjectName} || true"
            sh "docker run -d -p  ${currentProjectPort}:${currentProjectPort} --name ${currentProjectName} --restart=always ${harbor_url}/${harbor_name}/${imageName}"
            sh "echo 容器启动成功"
        }
    }
}
