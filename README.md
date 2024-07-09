# mnay
Maybe not allowed Yeah~

# 技术栈：

- Java21
- SpringBoot3
- JPA
- Minio
- Redis
- MySQL

# model层规定：

- dbo层：
  - 数据库存储字段的映射（可以包含不在数据库存储的字段，使用注解标注该类字段，一般该类字段拥有特殊用途）
- dao层
  - 是数据库访问中，较为特殊的返回值定义，即：特殊情况下，访问数据库获取的信息
- dto层：
  - 内部接口调用
  - http形式调用
- request层：
  - 只能用于http形式调用的接口入参
- vo层：
  - 仅用于对外展示，不在模块间接口调用使用

# 接口设定：

- repo层：
  - JPA数据库连接层，仓库层，用于访问数据库，不暴露给其他模块，由manager层或service层调用使用
- manager层（interface + impl）：
  - 是多模块间调用的接口，内部调用，不暴露给外部调用，使用dubbo进行调用
- service层（interface + impl）：
  - 是本模块业务层的接口，与当前模块高度耦合，不暴露给其他模块，只由controller和api层调用使用
- facade层（only interface）：
  - 是对接口的基础设定，由一组最通用的接口构成，是api和controller层都需要实现的一组接口
- controller层（only impl）：
  - 实现facade层，是对前端调用暴露的接口，一般用于http形式调用，一般由本系统前端调用
- api层（interface + impl）：
  - 继承facade层，并实现impl，是对外部系统对接使用的接口，比如对外部系统提供数据时使用

# 开发记录：

- 2024-06-06：项目初始化
- 2024-06-07：模块增加workflow模块划分，初步构思微服务架构;增加common模块；
- 2024-06-18：增加多个模块以及初始代码；增加多个技术依赖；项目成功启动；
- 2024-06-20: workflow模块增加一些基础代码；
- 2024-06-21: 项目model层、接口层设定；
- 2024-07-09: docker-compose.yml文件编写；dubbo增加；修复一些bug；
