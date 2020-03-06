<h1 align="center">
  <br>
  echo-rpc
  <h4 align="center">
echo-rpc 是基于netty+spring+kryo构建的rpc框架。
  </h4>
  <h5 align="center">
<a href="#Environment">Environment</a>&nbsp;&nbsp;
<a href="#quick-start">Quick Start</a>&nbsp;&nbsp;
<a href="#Features">Features</a>&nbsp;&nbsp;
<a href="#Structure">Structure</a>&nbsp;&nbsp;
<a href="#Thanks">Thanks</a>&nbsp;&nbsp;
<a href="#License">License</a>
</h5>
  <br>
</h1>

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/leonGravel/echo-rpc/build)![GitHub](https://img.shields.io/github/license/leonGravel/echo-rpc)![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/leonGravel/echo-rpc)

## Environment

* zookeeper 3.6.0
* Java 8 +
* Maven 3.0 +

## Quick Start

```
git clone https://github.com/leonGravel/echo-rpc.git
cd echo-rpc
mvn -Dmaven.test.skip=true clean package

// 启动两个测试用例，客户端和服务端
java -jar example-client/target/client-example-0.0.1-SNAPSHOT.jar
java -jar example-client/target/shortcut-0.0.1-SNAPSHOT.jar
```

调用 `curl -d 'str=hello world!' http://127.0.0.1:9002/index` ，可以看到以下返回：

```
["rpc--调用成功！hello world!"]                                                                                                                                                                       
```
表示调用成功。

## Features
- [X] 基本的rpc远程调用
- [X] 服务端重连
- [X] 心跳检测
- [X] 抽象注册中心工具类
- [ ] 基于注解和SPI实现序列化信息
- [ ] 统一配置注入
- [ ] 干掉目前存在大量硬编码
- [ ] 抽象netty编码解码器

## Structure

```shell
├── echo-client                              // rpc客户端.     
├── echo-common                              // 公用组件.                
├── echo-server                              // rpc服务端.        
├── example-client                           // 客户端示例代码，用springboot编写.     
├── example-server                           // 服务端示例代码，用springboot编写.        
├── .gitignore                               // git忽略项.             
├── README.md               
```

### RPC 原理
实现 RPC 主要分为以下几个部分。
#### 建立通信
服务端和客户端需要建立TCP连接进行通信，在这一层，我使用 netty 来完成。
#### 服务发现
使用zookeeper来做服务发现，通过临时节点的注册与监听，在节点上储存真实的服务端的ip和地址，然后通过 netty 建立连接，传输信息。
#### 序列化
实现 netty 的编解码 handler，使用kyro来做序列化，以及网络传输。
#### 服务调用
客户端通过 netty 将序列化的请求信息发送出来，RPC调用层，通过轮询，选择已注册的服务地址进行传输。
netty 传输到服务端之后，将信息反序列化，通过解析请求信息的服务名全连接，反射调用本地服务。获取到本地服务返回信息之后，将信息再通过 netty 传回给客户端。

### 性能测试
// TODO

## Thanks

* [spring](https://spring.io/)
* [netty](https://netty.io/)
* [zookeeper](https://zookeeper.apache.org/)
* [JMH](http://openjdk.java.net/projects/code-tools/jmh/)

## License

Code released under the [MIT License](https://github.com/leonGravel/echo-rpc/blob/master/LICENSE).