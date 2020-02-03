### bean生命周期的几个回调方式的顺序（在所有必要依赖配置完成后）
##### 初始化
- 注解@PostConstruct标注的方法
- 接口InitializingBean的实现方法afterPropertiesSet（不推荐，因为代码与接口耦合）
- init-method属性指定的方法
    - xml配置方式：<bean init-method="">
    - 注解方式：@Bean(initMethod="")
- 接口SmartInitializingSingleton的实现方法afterSingletonsInstantiated
##### 销毁
- 注解@PreDestroy标注的方法
- 接口DisposableBean的实现方法destroy（不推荐，因为代码与接口耦合）

### profile
#### 配置要激活的profile
- ctx.getEnvironment().setActiveProfiles()
- spring.profiles.active属性，多个用逗号分隔
#### 设置默认的profile，即没有设置profile时的值
- ctx.getEnvironment().setDefaultProfiles()
- spring.profiles.default属性，多个用逗号分隔

### spring事件与监听
#### 原理
spring中是通过ApplicationListener及ApplicationEventMulticaster来进行事件驱动开发的，即实现观察者设计模式或发布-订阅模式。
- ApplicationListener监听容器中发布的事件，只要事件发生，就触发监听器的回调，来完成事件驱动开发。属于观察者设计模式中的Observer对象。
- ApplicationEventMulticaster用来通知所有的观察者对象，属于观察者设计模式中的Subject对象。
#### Spring提供的标准事件
- Contextrefreshedevent:当 Applicationcontext被初始化或刷新时,触发该事件。
- Context Closedevent:当 Application Context被关闭时,触发该事件。容器被关闭时,其管理的所有单例Bean都被销毁。
- Requesthandleevent:在Web应用中,当一个HTP请求结東时触发该事件。
- Contextstartedevent:当容器调用 start()方法时,触发该事件。
- Contextstopevent:当容器调用stop)方法时,触发该事件。

### 问题
#### Controller加上@ResponseBody请求返回406状态码
原因：
- 要支持json解析，需要引入jackson相关依赖
- 需开启springmvc对json的读写支持；xml配置时加上<mvc:annotation-driven/>即可
#### 文件上传问题
##### （1）no multi-part configuration has been provided
原因：需要配置MultipartResolver比如CommonsMultipartResolver，此外还要引入commons-upload.jar依赖