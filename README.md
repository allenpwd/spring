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