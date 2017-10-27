# SpringBoot

## 启动方式

1. IDE启动

只需要点击RUN按钮

2. 命令行启动

```
mvn spring-boot:run
```

3. 将工程打包成jar包
```
mvn install //将工程打包成jar包

cd target //进入target目录找到jar包

java -jar girl-0.0.1-SNAPSHOT.jar //运行jar文件
```

## 设置访问连接

```
@RestController
public class HelloController {

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String say(){
        return "hello springboot";
    }
}
```

## 配置文件

1. 配置属性

在application.yml中配置端口访问路径的配置

```
server:
  port: 8080
  context-path: /girl
```

2. 在配置文件中设置自定义属性

在在application.yml中

```
high: 20

girl:
  cupsize: A
  age: 18
```

在Java文件中

```
@RestController
public class HelloController {

    @Value("${high}")
    private String high;

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String say(){
        return girlProperties.getCupsize()+girlProperties.getAge()+high;
    }
}
```

3. 设置开发与生产环境两种配置文件

新建application-dev.yml和application-release.yml两个文件
分别在两个配置文件中写相应配置，并在application.yml中设置使用哪个配置文件

```
spring:
  profiles:
    active: release  
```

