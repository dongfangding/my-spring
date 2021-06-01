# my-spring
简单实现spring核心功能， 不过写的肯定非常粗糙， 只是自己的练习理解。尤其是`BeanFactory`接口下的一堆体系，实在是太复杂太爆炸了。

## 测试
入口在`com.ddf.framework.customize.spring.beans.demo.ApplicationRunner`， 与测试相关的都放在这个包下了

测试时，启动容器方法如下:

配置类：
```java
@Configuration
@ComponentScan(basePackages = {"com.ddf.framework.customize.spring.beans.demo"})
public class CustomizeDemoConfig {

    @Bean
    public TestA testA(TestB testB) {
        final TestA testA = new TestA();
        testA.setTestB(testB);
        return testA;
    }

    @Bean
    public TestB testB() {
        return new TestB();
    }
}
```

容器测试入口：
```java
public class ApplicationRunner {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomizeDemoConfig.class);

        // 测试IOC
        final TaskService taskService = context.getBean(TaskService.class);
        taskService.doTask("ddf", "我要开始装逼了");

        final JdbcProperties jdbcProperties = context.getBean(JdbcProperties.class);
        System.out.println("获取到的bean的结果: " + jdbcProperties);

        // 测试通过@Bean和配置类注入有依赖和无依赖的bean， TestA中注入TestB对象
        TestA testA = context.getBean(TestA.class);
        testA.delegate();
    }
}
```

## 实现功能列表

- IOC容器构建， @ComponentSan包扫描@Service, @Component, 支持一个接口多个实现。
- 依赖注入， 支持@Autowired, 默认按照Class, 如果存在多个Class实现，则使用字段名作为bean name查找bean
- @Value注入实体属性，不过只支持写死，不支持表达式解析。
- 支持配置类配合注解@Bean注入自定义对象， bean method支持无参和有参， 如果方法有参， 则从IOC中获取注入
    ```java

    ```
