# my-spring
简单实现spring核心功能， 不过写的肯定非常粗糙， 只是自己的练习理解。尤其是`BeanFactory`接口下的一堆体系，实在是太复杂太爆炸了。

## 测试
入口在`com.ddf.framework.customize.spring.beans.demo.ApplicationRunner`， 与测试相关的都放在这个包下了

测试时，启动容器方法如下：
```java
public static void main(String[] args) {
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            "com.ddf.framework.customize.spring.beans.demo");
    final TaskService taskService = context.getBean(TaskService.class);
    taskService.doTask("ddf", "我要开始装逼了");

    final JdbcProperties jdbcProperties = context.getBean(JdbcProperties.class);
    System.out.println("获取到的bean的结果: " + jdbcProperties);
}
```

## 实现功能列表

- IOC容器构建， @ComponentSan包扫描@Service, @Component, 支持一个接口多个实现。
- 依赖注入， 支持@Autowired, 默认按照Class, 如果存在多个Class实现，则使用字段名作为bean name查找bean
- @Value注入实体属性，不过只支持写死，不支持表达式解析。
