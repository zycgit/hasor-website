+++
title = "About"
description = "About Hasor Framework"
keywords = ["FAQ","How do I","questions","what if"]
+++

&emsp;&emsp; 面向生产环境而设计的 Java 应用开发框架。它的核心设计目标是提供一个简单、且必要的环境给开发者。开发者可以在此基础上快速进行软件开发。

&emsp;&emsp; 区别于其它框架的是 Hasor 有着自己一套完整的体系，无论您是一般的Web项目，还是几百人团队中复杂的分布式系统。Hasor都会给予你最有力的支持。

&emsp;&emsp; 支持的功能有(J2EE、WebMVC、Restful、RPC、DataQL、IoC、Aop、Xml Reader、Event、J2EE、Form、JDBC、数据库事务)。

----------
### 特点

- “微内核+插件” 简单、小巧、功能强大、使用简单。
- COC原则的最佳实践，‘零’配置文件。
- 合理的整体架构规划，即是小框架也是大平台。
- 各部分全部独立，按需使用，绝不臃肿。
- 提供 “数据库 + 服务” 整合查询，并提供数据整合能力。
- 体积小，无依赖。

----------
### 架构
![架构](/web/img/content/CC2_403A_3BD5_D581.jpg "架构")

----------
### 总体功能

- Core 一款拥有IoC、Aop的模块插件框架（[详细..](hasor-core/README.md)）
    - 提供一个支持IoC、Aop的Bean容器。
    - 基于 Module + ApiBinder 机制提供统一的插件入口。
    - 特色的 Xml 解析器。让你无需二次开发无需配置，直接读取自定义xml配置文件。
    - 支持模版化配置文件，让您程序打包之后通吃各种环境。
- DB 提供了JDBC操作、事务管理（[详细..](hasor-db/README.md)）
    - 提供 JDBC 操作接口，并提供简单的 Result -> Object 映射(无需任何配置,包括注解)。
    - 与 Spring 一样，提供七种事务传播属性的控制。
    - 支持多种事务控制方式包括：手动事务控制、注解式声明事务、TransactionTemplate模板事务。
    - 支持多数据源，并且支持多数据源下的事务控制（不支持分布式事务）
- DataQL 提供比 GraphQL 更加灵活好用的服务查询引擎（[详细..](hasor-dataql/README.md)）
    - 采用编译执行，拥有飞快的执行速度（内部的几个例子在1W次执行频率下，平均执行时间在1毫秒内）
    - 支持通过 lambda 定义 UDF。
    - 支持查询结果返回一个 UDF。
    - 支持纯 JSON 输入。
    - 支持表达式计算（算数运算、位运算、逻辑运算、比较运算）
    - 支持运算符重载（暂不开放该功能）
    - 支持 if 条件判断。
    - 支持 JSR223
- Web 是一个吸收了百家所长的 Web MVC框架（[详细..](hasor-web/README.md)）
    - 提供 RESTful 风格的 mvc 开发方式。
    - 提供Form表单验证接口、验证支持场景化。
    - 开放的模版渲染接口，支持各种类型的模版引擎。
    - 内置文件上传组件，无需引入任何jar包。
- Land 它类似于 Zookeeper 负责提供分布式一致性的支持（[详细..](hasor-land/README.md)）
    - 开发中...
- RSF 功能堪比淘宝 HSF、dubbo 的分布式 RPC 服务框架（[详细..](hasor-rsf/README.md)）
    - 支持容灾、负载均衡、集群
    - 支持通过服务注册中心，支持分布式服务统一治理
    - 支持服务动态发布、动态卸载
    - 支持服务分组、分版本
    - 多种调用方式（点对点、分布式轮训、泛化调用、同步、异步、回调、接口代理）
    - 跨语言：支持通过 Hprose 调用 RSF 的服务。
    - 支持虚拟机房、隐式传参、服务路由、Telnet 等高级功能。
- Pluins Hasor 套件下的第三方插件项目（[详细..](hasor-plugins/README.md)）
    - Spring 整合插件（2016-02-16）
    - JFinal 整合插件（2016-11-03）
    - MyBatis3 插件
    - JUnit 插件
    - Freemarker 渲染器插件
    - Json 渲染器插件（支持Json引擎顺序为：fastjson -> gson）
    - [支持与 Nutz 集成（2017-02-21）-> nutz-integration-hasor](https://github.com/nutzam/nutzmore/tree/master/nutz-integration-hasor)

----------
### 发展状况

&emsp;&emsp; Hasor起源于2012年。当时尚未开源，并被应用到公司各大项目中。当时基于Guice构建，并且整合了Spring JDBC、Guice等大量三方框架。

&emsp;&emsp; 2013年9月15日，第一个Hasor版本发布。Module化的插件概念被提出，同时依赖大量减少，成为一个开源的java开发框架。

&emsp;&emsp; 2015年7月3日，1.0.0版本发布。这一年Hasor明确的发展路线，确立了“小而美的核心，大而全的生态圈”目标。

&emsp;&emsp; 2016年8月18日，2.4.3版本发布，基于2.4版本Hasor开始孕育全新的子项目 RSF。当时 Hasor 的版图还没有这么大。

&emsp;&emsp; 2017年2月21日，RSF 和 Land 被并入 Hasor 体系，同时首次公开了 Hasor 大版图的想法。同年 DataQL 问世。

----------
### 最低要求
* jdk7
* servlet 2.3