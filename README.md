# ulquiorra 缓存组件

基于caffeine和redis进行封装，简化项目中本地缓存及二级缓存使用。

## 一、使用方式

引入依赖：

```xml
<dependency>
  <groupId>io.github.trunks2008</groupId>
  <artifactId>ulquiorra-cache</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 二、只开启本地缓存模式

在`application.yml`中添加配置：

```yml
caffeine:
  initialCapacity: 128 #起始大小
  maximumSize: 1024    #最大
  expireAfterWrite: 60 #过期时间
```

启动类使用注解：`@EnableMonoLayerCache`

### 1、代码操作模式

直接使用`CaffeineUtil`类操作本地缓存：

```java
CaffeineUtil.put("aaa","bbb");
Object aaa = CaffeineUtil.get("aaa");
```

### 2、注解模式

可使用spring原生注解操作缓存

starter中已包含`@EnableCaching`注解，项目中无需重复添加开启

```java
@GetMapping("test2")
@Cacheable(value = "cacheName",key = "#key")
public Object test2(String key){
    System.out.println("进入方法，未走缓存");
    return "key:"+key;
}
```


## 三、注解同时管理Caffeine+Redis 两级缓存模式

设计思路：

![](https://img-blog.csdnimg.cn/img_convert/e21a4f86f7a4af469dd9a534922d8422.png)

### 一、使用方式

由于同时使用了`Caffeine`和`Redis`，所以需要修改配置文件，`Redis`配置：

```yml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    database: 0
    timeout: 10000ms
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0
```

二级缓存配置，支持分别设置两级缓存的独立过期时间：

```yml
bilayer:
  allowNull: true
  init: 128
  max: 1024
  expireAfterWrite: 30  #Caffeine过期时间
  redisExpire: 60      #Redis缓存过期时间
```

在启动类添加注解启动：

```java
@EnableBiLayerCache
```

启动后，使用Spring缓存注解即可实现缓存管理。

### 二、本地缓存一致性问题

使用Redis消息订阅方式解决

在任何一台主机修改本地缓存后，会异步通知所有其他主机，修改相同key的缓存值