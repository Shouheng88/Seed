# SpringBooster

基于 Springboot 的后端快速开放框架，虽然开源的快速开发框架很多，但是这是唯一一个能为你节省大量开发时间，能够将你从样板代码中解救出来的开发框架。

## 项目初衷

- 帮助开发者快速构建 APP 后端
- 标准化后端开发，优雅设计和整合利用 Springboot
- Booster 有推动的含义，该项目意在 Springboot 基础之上进一步简化 Springboot 开发

## 功能概述

该框架设计了代码生成规则，能够帮你生成大量的样板代码，此外，作为一个后端框架，它包含了开发一个后端程序需要的近乎所有必备的功能。功能列表如下，

- 自动化生成应用所需的 Service 以及 Service 实现、DAO 以及 DAO 实现、Mapper 文件、SQL、业务对象 VO、业务查询对象 SO 以及单元测试。详情参考：[CodeGenerator.java](seed/seed-data/src/main/java/com/seed/data/CodeGenerator.java)
- 自动化生成其他端所需的数据结构，对于 Android 端，可以生成基于 Retrofit 的 Servie 文件，进行 Restful 请求的 Repo 文件等。
- 自动化生成 Mybatis 所需的枚举类型的 TypeHandler
- 安全配置，基于 SpringSecurity 对 Swagger、Acturator、Druid State 等进行了安全配置
- 数据源配置，基于 Alibaba Druid、Mybatis 进行 MySQL 数据访问
- Redis 数据源配置、应用示例
- 基于 Swagger 自动生成 API 接口在线文档
- 统一参数校验，基于 Swagger 进行参数校验，参考 [RestfulApiAspect.java](seed/seed-portal/src/main/java/com/seed/portal/config/RestfulApiAspect.java)
- 统一分页校验，注解配置分页信息，参考 [RestfulApiAspect.java](seed/seed-portal/src/main/java/com/seed/portal/config/RestfulApiAspect.java)
- 统一授权校验，注解配置，设计了基于 token 的请求机制，参考 [RestfulApiAspect.java](seed/seed-portal/src/main/java/com/seed/portal/config/RestfulApiAspect.java)
- 规范了数据交互格式，规范了应用内部的数据交互格式 [PackVo](seed/seed-base/src/main/java/com/seed/base/model/PackVo.java) 以及对外暴露的数据交互格式 [BusinessRequest](seed/seed-base/src/main/java/com/seed/base/model/business/BusinessRequest.java) 以及 [BusinessResponse](seed/seed-base/src/main/java/com/seed/base/model/business/BusinessResponse.java)
- 统一异常处理：既包含了基于 Springboot 的基础的异常处理，又包含了基于 SpringAOP 的 Restful 请求异常处理
- 事务机制
- 规范了输出日志
  - 日志生成文件频率、大小等和输出格式等基础配置
  - 添加了注解 [@HideLog](seed/seed-base/src/main/java/com/seed/base/annotation/HideLog.java)，灵活配置，用于避免输出较大内容到日志文件中
  - 全局的日志输出格式，包含了 WHEN WHERE & WHAT HAPPEND，标准化，便于排查问题
- 单元测试
  - 单元测试 Service 接口基础用例生成
  - 提供了 Mock 工具，用来生成单元测试所需的对象，支持多种数据类型包括枚举等
- Dozer 工具 [DozerBeanUtil](seed/seed-base/src/main/java/com/seed/base/utils/DozerBeanUtil.java) 做优雅的数据转换，支持不同数据类型之间字段映射（以后用来做 ES VO PO 之间数据类型转换）
- 可通过注解自定义限流，基于 Redis 设计了限流机制
- 基于 Lombock 简化数据 Bean 定义
- 全新的，基于 OkHttp+Retrofit 的三方 API 请求机制，异步任务集成示范 [AiServiceImpl.java](seed/seed-portal/src/main/java/com/seed/portal/service/impl/AiServiceImpl.java)
- 邮件发送功能封装 [MailUtils.java](seed/seed-base/src/main/java/com/seed/base/utils/MailUtils.java)，简单配置即可直接使用

**该项目追求标准化和优雅设计，代码干净整洁，就算没有后端开发经验，也能轻松上手为自己的 APP 构建应用后台，快速获取后端开发经验。**

