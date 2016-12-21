Hasor首页项目

== 打包
    1. website 项目通过 mvn pacakge 产出 war 包。
    2. 无论是线上、预发、日常、开发机。都使用该 war包,无需替换 war 包中配置文件。
    3. 不同的环境配置通过 env.config 属性文件传递给程序。
        /conf/work_home 目录下有两个环境

== 工程关系
           website-domain       // 各类模型定义
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

== 主要依赖
    net.hasor:hasor-core:jar:2.6.0                  基础开发框架
    net.hasor:hasor-rsf:jar:1.1.1                   分布式RPC
    org.freemarker:freemarker:jar:2.3.22            渲染引擎
    org.mybatis:mybatis:jar:3.3.0                   数据库ORM框架

    c3p0:c3p0:jar:0.9.1.2                           数据库连接池
    com.alibaba:fastjson:jar:1.2.15                 JSON序列化
    org.slf4j:slf4j-api:jar:1.7.21                  日志框架
    ch.qos.logback:logback-classic:jar:1.1.3
    junit:junit:jar:4.12                            测试框架
    com.atlassian.commonmark:commonmark:jar:0.6.0   Markdown工具箱

== 线上环境
    1. 地址 : htp://www.hasor.net
    2. conf/tomcat                 为 tomcat 的真实配置。
    3. /conf/work_home/online      为 线上的 WORK_HOME。

== 线上运行环境
    1. 线上运行在 docker 里, Dockerfile 在工程根目录中。
