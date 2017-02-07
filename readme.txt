Hasor首页项目

== 打包
    1. website 项目通过 mvn pacakge 产出 war 包。
    2. 无论是线上、预发、日常、开发机。都使用该 war包,无需替换 war 包中配置文件。
    3. 不同的环境配置通过 env.config 属性文件传递给 war。
        /conf/work_home 目录下有两个环境

== 工程关系
           website-domain       // 模型定义
                 ^
                 |
           website-login        // OAuth
                 ^
                 |
           website-core         // 服务类和业务逻辑
             ^      ^
             |      |
       website-web  |           // 处理Web请求和响应
              website-test      // 各类单元测试

== 项目配置
    1. 所有项目中都使用 static-conifg.xml 方式声明模块自身的配置信息。
    2. 主配置文件统一汇聚到 website-web 项目中的 hasor-config.xml。


== 主要依赖
    net.hasor:hasor-core:jar:3.0.3              基础开发框架
    net.hasor:hasor-rsf:jar:1.2.0               分布式RPC
    org.freemarker:freemarker:jar:2.3.22        渲染引擎
    org.mybatis:mybatis:jar:3.3.0               数据库ORM框架
    org.pegdown:pegdown:jar:1.6.0               Markdown工具箱
    c3p0:c3p0:jar:0.9.1.2                       数据库连接池
    com.alibaba:fastjson:jar:1.2.15             JSON序列化
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

== Bug
    1. http://www.hasor.net/my/newVersion.htm?projectID=1  lastVersion 排序错误  0.0.9 居然大于 0.0.10
    2. 各类表单填写验证，必填项目检测。
    3. 版本详情页在登录之后跳转丢失 projectID 参数。
    4. 版本列表排序，当遇到同一天发布两个版本时，出现错误顺序。应该设置第二排序字段。