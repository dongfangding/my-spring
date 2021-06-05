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
    // 指定配置类启动容器
    final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            CustomizeDemoConfig.class);

    // 测试IOC
    final TaskService taskService = context.getBean(TaskService.class);
    taskService.doTask("ddf", "我要开始装逼了");

    // 测试@Value静态注入对象属性
    final JdbcProperties jdbcProperties = context.getBean(JdbcProperties.class);
    System.out.println("获取到的bean的结果: " + jdbcProperties);

    // 测试通过@Bean和配置类注入有依赖和无依赖的bean
    TestA testA = context.getBean(TestA.class);
    testA.delegate();

    // 测试事务
    // 获取数据源
    final DruidDataSource dataSource = context.getBean(DruidDataSource.class);
    // 获取事务管理器
    final PlatformTransactionManage platformTransactionManage = context.getBean(PlatformTransactionManage.class);
    System.out.println(Thread.currentThread()
            .getName() + "获取连接1: " + platformTransactionManage.getConnection());
    System.out.println(Thread.currentThread()
            .getName() + "获取连接2: " + platformTransactionManage.getConnection());
    System.out.println(Thread.currentThread()
            .getName() + "获取连接3: " + platformTransactionManage.getConnection());

    final ExecutorService service = Executors.newFixedThreadPool(2);

    // 基于接口的jdk代理的事务实现
    final TransactionalService transactionalService = context.getBean(TransactionalService.class);
    // value 的集合大小为偶数测试正常事务提交
    service.execute(() -> {
      transactionalService.transfer("ddf", "chen", 100L);
    });
    // value 的集合大小为奇数测试正常事务回滚
    service.execute(() -> {
      transactionalService.transfer("ddf", "chen", 101L);
    });

    // 测试cglib方式的事务代理，同时测试通过bean name获取Bean
    final CglibTransactionalComponent transaction = (CglibTransactionalComponent) context.getBean("cglibTransaction");
    // value 的集合大小为偶数测试正常事务提交
    service.execute(() -> {
      transaction.transfer("ddf", "chen", 50L);
    });
    // value 的集合大小为奇数测试正常事务回滚
    service.execute(() -> {
      transaction.transfer("ddf", "chen", 51L);
    });
    service.shutdown();
  }
}
```

## 实现功能列表

- IOC容器构建， @ComponentSan包扫描@Service, @Component, 支持一个接口多个实现。
- 依赖注入， 支持@Autowired, 默认按照Class, 如果存在多个Class实现，则使用字段名作为bean name查找bean
- @Value注入实体属性，不过只支持写死，不支持表达式解析。
- 支持配置类配合注解@Bean注入自定义对象， bean method支持无参和有参， 如果方法有参， 则从IOC中获取注入
- 支持事务注解@Transactional, 会在生成IOC单例对象后判断是否存在事务注解，如果存在， 则对当前bean生成代理对象后再次放入单例池， 支持jdk和cglib两种方式


## 实现思路
- 定义`BeanDefinition`用来描述bean 定义元数据， 定义成接口， 一个bean的最基本的属性必然包含bean name和bean class
  - 定义抽象实现， 用来处理bean name 和bean class
  - 实现一、定义最基本的IOC单例对象bean定义， 即需要框架层面反射调用无参构造去注册bean的，这个是bean实现的最简单的一种方式
  - 实现二、定义基于FactoryMethod来让用户自定义bean的创建过程的bean定义对象， 即一个bean的创建是基于某个方法， 方法中可能会有入参，入参依赖的是其它IOC对象。如事务管理器， 需要用户先注入数据源， 然后注入事务管理器时需要依赖数据源
  - 实现三、 基于有参构造方法来决定bean的定义流程， 比如注册事务代理工厂， 代理的方法逻辑需要事务管理器支持，但是框架层面并不知道事务管理器的实现，所以会在事务代理工厂中定义接收事务管理器的构造方法， 然后定义成Bean定义， 再注入的时候再获取用户自己注入的事务管理器的实现，从而完成事务代理工厂的创建
  
- 定义`BeanFactory`顶级接口，用来定义获取Bean的最基本的方法， 定义`ApplicationContext`接口用来处理bean的具体创建流程，通过不同的实现来确定如果获取`BeanDefinition`, 然后再父类中`AbstractApplicationContext`去处理`BeanDefintion`的具体流程
  - 使用Map存储bean name到`BeanDefinition`的映射， 用来处理依赖注入时， 如果bean未创建， 则获取`BeanDefinition`对象，然后执行创建流程。
  - 使用Map存储bean class到`BeanDefinition`列表的映射， 用来处理一个bean class 存在多个`BeanDefinition`的问题。同时也需要一个Map用来存储class到bean name的列表映射， 即class的实现有哪几个bean name。
  - 使用Map存储bean name到bean class 的映射， 用来存储实例化好的bean对象， 即单例池。 

- bean的创建流程， 首先是定义一些注解类用来标识一个希望被IOC托管的类， 如`@Service`、`@Component`等，通过配置类传递过的属性来决定包扫描路径，
然后根据扫描到的类定义成`BeanDefinition`对象，然后添加到缓存中。根据不同的`BeanDefinition`实现去处理不同的bean的创建流程，具体看上面定义有介绍。
  - 如果创建bean的过程中依赖其它bean， 则先去获取是否已经创建，未创建，则查询是否存在bean定义， 不存在则抛出异常。存在，则获取bean定义，执行依赖的bean的创建流程， 如果依赖的bean再依赖其它的流程，以此传递，然后调用堆栈再一步步回到最上层。
  - bean创建完成后处理依赖注入， 获取bean的字段是否存在`@Autowired`注解， 如果存在，则找到对应的bean， 这个流程和上一步类似。区别是这里需要判断注入字段的类型对应是否只有一个bean定义， 如果只有一个则注解获取，如果有两个， 则近一步获取到baen name, 这里没有另外额外注解支持， 而是使用field name作为bean name, 不存在则抛出异常
  - bean创建完成后， 处理后置流程， 如事务代理， 如果一个类里包含有`@Transactional`注解的方法，则这个类需要事务控制， 那么则生成代理类，根据是否有接口决定代理实现方式。所以一个bean如果需要事务支持，放入单例池时就已经是一个代理对象了，但是一个类中不是所有方法都需要事务代理，这个就是在代理逻辑中判断当前方法是否有`@Transaction`注解
  
- 事务管理器的注入
  - 定义顶层接口事务管理器相关的方法， 再定义一个实现，基于数据库的事务实现。然后定义自己的事务代理器工厂， 这里都需要使用方自己注入数据源才能运行， 因此需要框架在完成用户的`BeanDefinition`扫描之后，还需要添加一些自己需要用到的定义， 一般都是通过有参的构造方法，然后实例化的时候获取有参依赖的IOC中的对象，完成框架自身支持类的实例化
  - 使用`ThreadLocal`来管理同一个线程公用一个数据源
