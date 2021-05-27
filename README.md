# my-spring
简单实现spring核心功能


## 实现功能列表

- IOC容器构建， @ComponentSan包扫描@Service, @Component, 支持一个接口多个实现。
- 依赖注入， 支持@Autowired, 默认按照Class, 如果存在多个Class实现，则使用字段名作为bean name查找bean
- @Value注入实体属性，不过只支持写死，不支持表达式解析。
