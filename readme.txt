Hasor首页项目

== 打包构建
    1. website 项目通过 mvn pacakge 产出 war 包。
    2. 无论是线上、预发、日常、开发机。都使用该 war包,无需替换 war 包中任何配置文件。
    3. 一般情况下本地开发测试，通过连接日常开发数据库进行。
    4. 无论连接的是线上数据还是开发库，都通过 docker进行构建。
        docker脚本位于：/conf/work_home

== 环境配置
    1. 本地开发：配置位于 “website-web” 项目中 “env.config” 配置文件中。
    2. 开发环境：位于 /conf/work_home/daily。
    3. 线上环境：位于 /conf/work_home/online。
    4. 使用 openjdk 1.8

== 工程关系
           website-domain       // 模型定义
             ^      ^
             | website-client   // RPC服务接口
             |      ^
           website-core         // 服务类和业务逻辑
             ^  ^   ^
             |  | website-login // OAuth
             |  |   ^
             | website-web      // 处理Web请求和响应
       website-test             // 各类单元测试

== 项目配置
    1. 所有项目中都使用 static-conifg.xml 方式声明模块自身的配置信息。
    2. 主配置文件统一汇聚到 website-web 项目中的 hasor-config.xml。

== 外部依赖
    -- 主要
        net.hasor:hasor-core:jar:3.0.3              基础开发框架
        net.hasor:hasor-rsf:jar:1.2.0               分布式RPC
        org.freemarker:freemarker:jar:2.3.22        渲染引擎
        org.mybatis:mybatis:jar:3.3.0               ORM框架
        org.pegdown:pegdown:jar:1.6.0               Markdown解析
    -- 工具
        c3p0:c3p0:jar:0.9.1.2                       数据库连接池
        com.alibaba:fastjson:jar:1.2.23             JSON序列化
        com.google.guava:guava:jar:21.0             Cache
        org.slf4j:slf4j-api:jar:1.7.21              日志框架
        ch.qos.logback:logback-classic:jar:1.1.3
        junit:junit:jar:4.12                        测试框架

== 线上环境
    1. 地址 : htp://www.hasor.net
    2. conf/tomcat                 为 tomcat 的真实配置。
    3. /conf/work_home/online      为 线上的 WORK_HOME。
    4. 线上运行在 docker 里, Dockerfile 在工程根目录中。

== 发布注意
    1. 分布式部署下，如果模型和逻辑都有变更，请分两次发布，先发模型后发逻辑。

# 代码说明
    01.web启动入口类是：net.hasor.website.web.core.StartModule，定义它的配置文件是：hasor-config.xml
    02.test单元测试的启动入口是：net.hasor.website.core.RootModule
    03.JumpFilter负责处理访问例如：http://xxx.xxx/ 这种目录资源时跳转到对应的 http://xxx.xxx/index.htm
    04.FreemarkerRender 为模板渲染器，负责处理 htm or html 的渲染请求。
    05.文件上传之后是直接保存到“阿里云OSS”不会落盘，文件的访问是通过 CDN 代理 OSS。
    06.负责处理文件上传的类是：UploadToTemp、阿里云OSS初始化代码位于：AliyunModule。
    07.权限功能：目前用户权限功能是保存到：用户表的扩展信息字段中，该字段是json结构。如要加权限请参考片段：
        "userTags":{"newProject":true,"admin":true}
    08.用户登录之后会产生一个快速登录连接，登录过程中通过这个快速登录连接完成用户登录。链接最大有效时间为 1分钟，超时之后必须重新登录。
    09.用户会话信息保存在 Session 中。
    10.目前仅支持 OAuth 登录，已经接入第三方认证有：微博、QQ、Github
    11.可以通过登录（微博、QQ、Github）三个任意一种之后，在个人中心里通过绑定完成混合认证登录。
    12.目前不支持解绑功能。
    13.client工程中保留了三个远程服务接口，消费者可以通过 RSF 或 Hprose 执行远程方法调用。
        端口号是：RSF：2161、Hprose：2162。单元测试模式下远程服务提供者地址为：127.0.0.1位于：env.config配置文件。
    14.RPC 没有连接到任何注册中心，因此不支持服务自动发现。
    15.数据库连接 “env.config” 中已经保留日常开发测试数据库信息，线上数据库的信息通过docker部署时环境变量参数形式传入。
        该功能利用的是 Hasor 模版化配置文件机制实现。
    16.为了保证安全，开发环境没有配置 OAuth 的密钥，因此本地开发中您不可以使用 OAuth 进行登录。

== Bug或缺陷
    1. http://www.hasor.net/my/newVersion.htm?projectID=1  lastVersion 排序错误  0.0.9 居然大于 0.0.10
    2. 各类表单填写验证，必填项目检测。
    3. 版本详情页在登录之后跳转丢失 projectID 参数。

=== Nginx
    1. 安装 BCRE [Mac: brew install pcre]
    2. 站点通过 nginx 代理提供

=== 保留路径
    1. /docs/  -> Nginx 反向代理： http://project.hasor.net/docs