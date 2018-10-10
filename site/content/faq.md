+++
title = "FAQ"
description = "Frequently asked questions"
keywords = ["FAQ","How do I","questions","what if"]
+++

## 1. 优秀项目千千万，为何要选择重复造轮子？

Hasor 的初衷是 “学习、总结、分享”，因此拿来主义并不是 Hasor 的发展策略。

## 2. Hasor每个模块项目都很庞大，为什么没有分项目？

项目的分分合合做过很多次。目前最优的形态就是放到一个代码库中统一代码版本管理，同时各个项目保持相互独立。

## 3. Hasor用到了哪些外部依赖？

slf4j、asm、JavaCC、netty4、groovy。其中各子项目中必不可少的依赖如下：

* hasor-commons 依赖：无
* hasor-core 依赖：slf4j，hasor-commons
* hasor-dataql 依赖：hasor-commons
* hasor-db 依赖：hasor-core
* hasor-web 依赖：hasor-core
* hasor-rsf 依赖：hasor-core，groovy，netty4
* hasor-registry 依赖：hasor-rsf
* hasor-land 依赖：hasor-rsf
* hasor-plugins 插件集项目，大量外部依赖

## 4. 准备造自己的小闭环么？

Hasor是开放的，它的核心只有 “net.hasor.core” 一个包，共计 117 个类文件。不足整体代码的 10%，其它 90% 以上的代码都是扩展。

## 5. Hasor 功能是很好，但是我想和其它框架合用可以么？

可以的，目前 Hasor 已经内置了 Spring、JFinal、Nutz 三款框架的整合。您也可以自己的实际情况进行整合。启动 Hasor 只需要一行代码，相信整合不会耗费您太多精力。

## 6. 我只想使用某一个小功能，Hasor可以拆分独立使用么？

可以的。

## 7. 如何构建 Hasor 手册？

首先安装 JDK8，然后进入 document 目录执行 “mvn clean site” 即可生成一个全新的文档手册。
