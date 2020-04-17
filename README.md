# 系统整体介绍
本工程是基于springboot框架实现的，并整合使用了Redis + Quartz + Mybatis + MySQL + Thymeleaf等相关主流技术。

## 业务简介
本系统的业务场景为一个简单的用户注册和管理系统，整体框架为springboot，前端的实现依赖springmvc框架。
使用redis实现了缓存黑名单功能，应用quartz开源框架实现定时任务的调度，持久层依赖mybatis，数据库为mysql。

具体的使用方式：
* 首先安装mysql数据库，具体方法见下文讲解。
* 执行启动类SpringBootStartApplication，启动应用。
* 直接通过浏览器访问链接：http://localhost:10501/register
可以直接在页面进行用户注册。
* 本系统的用户管理模型包含两种用户，分别为超级管理员和普通用户（超级管理员的数据需要预置，见下文）
如果用超级管理员（账号：admin密码：admin）登录系统（登录链接：http://localhost:10501/login/doLogin），
则可以对各个普通账号进行锁定和解锁操作。
* 被锁定的用户默认进入黑名单，被锁定用户是无法登录系统的。
系统执行定时任务BlackListRefreshTask，每分钟更新一次redis中的黑名单数据。用户登录时系统会将用户信息与redis中的黑名单数据进行比对。

## 数据库
* 使用前首先要在本地安装数据库，本系统使用的是mysql，具体的安装可参考链接：
https://blog.csdn.net/weixin_40396510/article/details/79277731
* 安装完成后，在配置文件application.properties中设置对应的数据库信息，并执行sql文件create_db，创建数据库表、
预置超级管理用户的数据。

## 工程模块
```
├── README.md
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com.zidane.spring
    │   │       │
    │   │       ├── web--对应三层结构的接口层(依赖SpringMVC框架进行前后台交互)
    │   │       │
    │   │       ├── service--对应三层结构的业务层
    │   │       │
    │   │       ├── dao--对应三层结构的持久层
    │   │       │
    │   │       └── domain--对应持久层的pojo
    │   │
    │   ├── resources
    │   │       ├── blackList
    │   │       │      └── blackListRefresh.service.xml--定时任务配置文件
    │   │       ├── dbscripts
    │   │       │      └── create_db.sql--数据库脚本 
    │   │       ├── mybatis
    │   │       │      └── mapping--mybatis的Mapper.xml配置文件
    │   │       │
    │   │       ├── webapp--前端jsp文件
    │   │       ├── application.properties--springboot系统配置文件
    │   │       ├── log4j.xml
    │   │       └── transaction.xml--事务的配置文件
    │   │
    │   │
    └── test
        └── java
