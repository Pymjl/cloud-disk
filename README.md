# 一个简单的云盘

## 1.需求分析

- 用户的基本管理，增删改查
- 管理员可以设置管理员，删除用户
- 每个用户可以对文件的增删改查

![企业云盘](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/%E4%BC%81%E4%B8%9A%E4%BA%91%E7%9B%98.png)

## 2.技术栈与架构

- 架构

前后端分离

![未命名文件](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/%E6%9C%AA%E5%91%BD%E5%90%8D%E6%96%87%E4%BB%B6.png)

- 技术栈

|  Web框架   | Spring Boot 2.3.9.RELEASE |
| :--------: | :-----------------------: |
|   数据库   |         MySql 5.6         |
|   连接池   |           Druid           |
|    驱动    |           8.0+            |
|    缓存    |           Redis           |
|  权限认证  |            Jwt            |
| dao层框架  |       MyBatis Plus        |
|  文件服务  |         阿里云OSS         |
|   CI/CD    |       Docker+GitLab       |
|  版本管理  |            git            |
| 工具类框架 |          Hutool           |

本系统后端采用的分布式架构，用户服务和文件服务是两个不同的服务器，文件服务采用的阿里云OSS，对于文件的绝大部分操作不需要通过用户服务，而是直接访问阿里云OSS。

## 3.快速开始

所有的配置文件全在src下的resource目录下，核心配置在application.yml文件内

```yml
#-----------------------------------核心配置------------------------------------------
datasource: 
    type: com.alibaba.druid.pool.DruidDataSource #数据源，这里用的Druid
    driver-class-name: com.mysql.cj.jdbc.Driver #驱动
    url: jdbc:mysql://localhost:3306/cloud_disk?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true
    username:  #数据库用户名
    password:  #密码
    
redis:
    host:  #redis主机
    port:  #redis端口
    password: #密码
    database: 1
    lettuce:
      pool:
        max-active: 100
        max-wait: -1ms # 连接池最大阻塞等待时间，负值表示没有限制
        max-idle: 8
        
#阿里云OSS配置
aliyun:
  oss:
    file:
      #地域节点参数
      endpoint: 
      #自己的AccessKey id和密钥参数
      keyId: 
      keySecret: 
      #bucket参数 可以在控制台创建，也可以使用java代码创建
      bucketName: 
```

mail.setting

```yml
# 发件人（必须正确，否则发送失败），“小磊”可以任意变更，<>内的地址必须唯一，以下方式也对
# from = hutool@yeah.net
from=Pymjl<你的邮箱账号> #发邮件的QQ账户
# 密码（注意，某些邮箱需要为SMTP服务单独设置密码，详情查看相关帮助）
# QQ邮箱中SMTP密码是单独生成的授权码，而非你的QQ密码，至于怎么生成，见腾讯的帮助说明：http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256
pass=你的授权码
# 使用SSL安全连接
sslEnable=true
```

## 4.目录结构

```txt
卷 系统 的文件夹 PATH 列表
卷序列号为 D5EC-3327
C:.
├─.idea #idea的配置文件
│  ├─inspectionProfiles
│  └─libraries
├─logs #日志文件
├─src #Java源程序
│  ├─main
│  │  ├─java
│  │  │  └─cuit
│  │  │      └─pymjl
│  │  │          ├─config #核心配置，这个包下面是一些配置类
│  │  │          ├─constant #常量包
│  │  │          ├─controller
│  │  │          ├─entity #实体对象
│  │  │          │  ├─dto
│  │  │          │  ├─pojo
│  │  │          │  └─vo
│  │  │          ├─exception
│  │  │          ├─handler
│  │  │          ├─interceptor
│  │  │          ├─mapper
│  │  │          │  └─xml
│  │  │          ├─result
│  │  │          ├─service
│  │  │          │  └─impl
│  │  │          └─util
│  │  └─resources #资源路径
│  └─test #单元测试
│      ├─java
│      │  └─cuit
│      │      └─pymjl
│      └─resources
└─target #输出目录
    ├─classes
    │  └─cuit
    │      └─pymjl
    │          ├─config
    │          ├─constant
    │          ├─controller
    │          ├─entity
    │          │  ├─dto
    │          │  ├─pojo
    │          │  └─vo
    │          ├─exception
    │          ├─handler
    │          ├─interceptor
    │          ├─mapper
    │          ├─result
    │          ├─service
    │          │  └─impl
    │          └─util
    ├─generated-sources
    │  └─annotations
    ├─generated-test-sources
    │  └─test-annotations
    └─test-classes
        └─cuit
            └─pymjl
```

`.giylab-ci.yml` 和`Dockefile` 是CI/CD的配置文件，如果不了解CI/CD的概念请自行百度

如果想要将服务部署到自己的服务器请参考这篇我写的博客 http://pymjl.com/articles/67

![image-20220527112135884](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/image-20220527112135884.png)

