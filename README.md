### echo-rpc
基于netty+spring+kryo构建的rpc框架

### 目录结构
```shell
├── echo-client                              // rpc客户端.     
├── echo-common                              // 公用组件.                
├── echo-server                              // rpc服务端.        
├── example-client                           // 客户端示例代码，用springboot编写.     
├── example-server                           // 服务端示例代码，用springboot编写.        
├── .gitignore                               // git忽略项.             
├── README.md               
```
### 现有功能
* 基本的rpc远程调用
* 服务端重连
* 心跳检测

### 计划
- [ ] 基于注解和SPI实现代理接口
- [ ] 统一配置注入
- [ ] 干掉目前存在大量硬编码
- [X] 抽象注册中心工具类
- [ ] 抽象netty编码解码器
### end
>此项目仅作为学习netty的记录，不具有实际生产使用价值。