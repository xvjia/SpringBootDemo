SpringBoot

## 启动方式

* IDE启动

    只需要点击RUN按钮

* 命令行启动

    ```
    mvn spring-boot:run
    ```

* 将工程打包成jar包
    ```
    mvn install //将工程打包成jar包

    cd target //进入target目录找到jar包

    java -jar girl-0.0.1-SNAPSHOT.jar //运行jar文件
    ```

## 设置访问连接

* 设置restful链接

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

* 配置属性

    在application.yml中配置端口访问路径的配置

    ```
    server:
      port: 8080
      context-path: /girl
    ```

* 在配置文件中设置自定义属性

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

*  设置开发与生产环境两种配置文件

    新建application-dev.yml和application-release.yml两个文件
    分别在两个配置文件中写相应配置，并在application.yml中设置使用哪个配置文件

    ```
    spring:
      profiles:
        active: release  
    ```

    在命令行中启动不同配置

    ```
    java -jar girl-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
    ```

## Controller的使用

| 注解符             |                    作用                    |
| --------------- | :--------------------------------------: |
| @Controller     |                 处理http请求                 |
| @RestController | Spring4之后新加的注解，原来返回json需要@ReponseBody配合@Controller |
| @RequestMapping |                 配置url映射                  |
| @PathVariable   |                 获取url中的值                 |
| @RequestParam   |                 获取请求参数的值                 |
| @GetMapping     |                   组合注解                   |

* PathVariable注解符

  访问示例：`http://localhost:8080/hello/hi/1`

  ```
  @RequestMapping(value = "/hi/{id}")
      public String hi(@PathVariable("id") int id){
          return "id:"+id;
      }
  ```

* RequestParam注解符

  访问示例：`http://localhost:8080/hello/sayhi?id=10000`

  ```
  @RequestMapping(value = "/sayhi")
    public String sayhi(@RequestParam(value = "id",defaultValue = "100",required = false) int id){
        return "id:"+id;
    }
  ```

* GetMapping注解符

  ```
  //@RequestMapping(value = "/sayhi")
    @GetMapping(value = "/sayhi") //就是@RequestMapping上get方法的简写
    public String sayhi(@RequestParam(value = "id",defaultValue = "100",required = false) int id){
        return "id:"+id;
    }
  ```

## 数据库操作



* 配置数据库参数

  ```
  spring:
    profiles:
      active: relese
    datasource:
      driver-class-name: com.mysql.jdbc.Driver
      url: jdbc:mysql://192.168.0.114:3306/
  ```

* pom文件配置

  ```
  <dependency>
  			<groupId>org.springframework.boot</groupId>
  			<artifactId>spring-boot-starter-data-jpa</artifactId>
  		</dependency>

  		<dependency>
  			<groupId>mysql</groupId>
  			<artifactId>mysql-connector-java</artifactId>
  		</dependency>
  ```

* 设置JpaRepository接口

  ```
  package com.xvjialing.girl;

  import org.springframework.data.jpa.repository.JpaRepository;

  import java.util.List;

  public interface GirlRepository extends JpaRepository<Girl,Integer>{

      //通过年龄查询
      public List<Girl> findByAge(int age);
  }
  ```

* 增删改查具体实现

  ```
  package com.xvjialing.girl;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.web.bind.annotation.*;

  import java.util.List;

  @RestController
  public class GirlController {

      @Autowired
      private GirlRepository girlRepository;

      /**
       * 获取女生列表
       * @return
       */
      @GetMapping("/girls")
      public List<Girl> girlList(){
          return girlRepository.findAll();
      }

      /**
       * 添加女生
       * @param age 年龄
       * @param name 姓名
       * @return
       */
      @PostMapping("/girls")
      public Girl addGirl(@RequestParam("age") int age,
                          @RequestParam("name") String name){
          Girl girl = new Girl();
          girl.setAge(age);
          girl.setName(name);

          return girlRepository.save(girl);
      }

      /**
       * 查找女生
       * @param id
       * @return
       */
      @GetMapping(value = "/girls/{id}")
      public Girl findOneGirl(@PathVariable("id") int id){
          return girlRepository.findOne(id);
      }

      /**
       * 更新girl
       * @param id
       * @param age
       * @param name
       * @return
       */
      @PutMapping(value = "/girls/{id}")
      public Girl updateGirl(@PathVariable("id") int id,
                             @RequestParam("age") int age,
                             @RequestParam("name") String name){
          Girl girl = new Girl();
          girl.setId(id);
          girl.setAge(age);
          girl.setName(name);
          return girlRepository.save(girl);
      }

      /**
       * 删除girl
       * @param id
       */
      @DeleteMapping(value = "/girls/{id}")
      public void deleteGirl(@PathVariable("id") int id){
          girlRepository.delete(id);
      }

      /**
       * 通过女生年龄查询
       * @param age
       * @return
       */
      @GetMapping(value = "/girls/age/{age}")
      public List<Girl> girlListByAge(@PathVariable("age") int age){
          return girlRepository.findByAge(age);
      }
  }
  ```

  ​


## 事务管理

确保一个事务内的多个操作同时实现或都不实现

* 创建一个Service

  ```
  package com.xvjialing.girl;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;

  @Service
  public class GirlService {

      @Autowired
      private GirlRepository girlRepository;

      @Transactional
      public void insertTwo(){
          Girl girlA=new Girl();
          girlA.setAge(11);
          girlA.setName("dwdw");
          girlRepository.save(girlA);

          Girl girlB=new Girl();
          girlB.setAge(11);
          girlB.setName("dwdw");
          girlRepository.save(girlB);
      }
  }
  ```

  千万别忘记加上@Transactional，该注解符用来确保操作同时实现或同时失败

* 在Controller中实现

  ```
  @Autowired
      private GirlService girlService;

  @GetMapping(value = "/girls/addTwo")
      public void addTwoGirl(){
          girlService.insertTwo();
      }
  ```

## 使用Swagger2构建RESTful API

* #### 添加Swagger2依赖

  在`pom.xml`中加入Swagger2的依赖

  ```
  <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger2</artifactId>
      <version>2.2.2</version>
  </dependency>
  <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger-ui</artifactId>
      <version>2.2.2</version>
  </dependency>
  ```

* #### 创建Swagger2配置类

  在`Application.java`同级创建Swagger2的配置类`Swagger2`。

  ```
  @Configuration
  @EnableSwagger2
  public class Swagger2 {
      @Bean
      public Docket createRestApi() {
          return new Docket(DocumentationType.SWAGGER_2)
                  .apiInfo(apiInfo())
                  .select()
                  .apis(RequestHandlerSelectors.basePackage("com.didispace.web"))
                  .paths(PathSelectors.any())
                  .build();
      }
      private ApiInfo apiInfo() {
          return new ApiInfoBuilder()
                  .title("Spring Boot中使用Swagger2构建RESTful APIs")
                  .description("更多Spring Boot相关文章请关注：http://blog.didispace.com/")
                  .termsOfServiceUrl("http://blog.didispace.com/")
                  .contact("程序猿DD")
                  .version("1.0")
                  .build();
      }
  }
  ```

  ​	如上代码所示，通过`@Configuration`注解，让Spring来加载该类配置。再通过`@EnableSwagger2`注解来启用Swagger2。

  ​	再通过`createRestApi`函数创建`Docket`的Bean之后，`apiInfo()`用来创建该Api的基本信息（这些基本信息会展现在文档页面中）。`select()`函数返回一个`ApiSelectorBuilder`实例用来控制哪些接口暴露给Swagger来展现，本例采用指定扫描的包路径来定义，Swagger会扫描该包下所有Controller定义的API，并产生文档内容（除了被`@ApiIgnore`指定的请求）。

* #### 添加文档内容

  ​	在完成了上述配置后，其实已经可以生产文档内容，但是这样的文档主要针对请求本身，而描述主要来源于函数等命名产生，对用户并不友好，我们通常需要自己增加一些说明来丰富文档内容。如下所示，我们通过`@ApiOperation`注解来给API增加说明、通过`@ApiImplicitParams`、`@ApiImplicitParam`注解来给参数增加说明

  ```
  @RestController
  @RequestMapping(value="/users")     // 通过这里配置使下面的映射都在/users下，可去除
  public class UserController {
      static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());
      @ApiOperation(value="获取用户列表", notes="")
      @RequestMapping(value={""}, method=RequestMethod.GET)
      public List<User> getUserList() {
          List<User> r = new ArrayList<User>(users.values());
          return r;
      }
      @ApiOperation(value="创建用户", notes="根据User对象创建用户")
      @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
      @RequestMapping(value="", method=RequestMethod.POST)
      public String postUser(@RequestBody User user) {
          users.put(user.getId(), user);
          return "success";
      }
      @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
      @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
      @RequestMapping(value="/{id}", method=RequestMethod.GET)
      public User getUser(@PathVariable Long id) {
          return users.get(id);
      }
      @ApiOperation(value="更新用户详细信息", notes="根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
      @ApiImplicitParams({
              @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long"),
              @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
      })
      @RequestMapping(value="/{id}", method=RequestMethod.PUT)
      public String putUser(@PathVariable Long id, @RequestBody User user) {
          User u = users.get(id);
          u.setName(user.getName());
          u.setAge(user.getAge());
          users.put(id, u);
          return "success";
      }
      @ApiOperation(value="删除用户", notes="根据url的id来指定删除对象")
      @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
      @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
      public String deleteUser(@PathVariable Long id) {
          users.remove(id);
          return "success";
      }
  }
  ```

  ​	完成上述代码添加上，启动Spring Boot程序，访问：<http://localhost:8080/swagger-ui.html>
  。就能看到前文所展示的RESTful API的页面。我们可以再点开具体的API请求，以POST类型的/users请求为例，可找到上述代码中我们配置的Notes信息以及参数user的描述信息

  ## Hibernate Validator数据校验

  | 注解符                                      | 作用                                       |
  | ---------------------------------------- | ---------------------------------------- |
  | @NotNull                                 | 只不能为空                                    |
  | @NULL                                    | 值必须为空                                    |
  | @Pattern(Regex=)                         | 字符串必须匹配正则表达式                             |
  | @Size(min=,max=)                         | 集合的元素数量必须在min和max之间                      |
  | @CreditCardNumber(igonreNonDigitCharacters=) | 字符串必须是信用卡号（按美国标准）                        |
  | @Email                                   | 字符串必须是Email地址                            |
  | @Length(min=,max=)                       | 检查字符串的长度                                 |
  | @NotBlank                                | 字符串必须有字符                                 |
  | @NotEmpty                                | 字符串不能为NUll，集合有元素                         |
  | @Range(min=,max=)                        | 数据必须小于等于max,大于等于min                      |
  | @SafeHtml                                | 字符串是安全的html                              |
  | @URL                                     | 字符串是合法的URL                               |
  | @AssertFalse                             | 值必须是false                                |
  | @AssertTure                              | 值必须是                                     |
  | @DecimalMax(value=,inclusive=)           | 值必须小于等于(inclusive=true)/小于(inclusive=false)value指定的值，可以注解在字符串类型的属性上 |
  | @Digits(integer=,fraction=)              | 数字格式检查。integer指整数部分的最大长度，fraction指小数部分的最大长度。 |
  | @Futrue                                  | 值必须是未来的日期                                |
  | @Past                                    | 值必须是过去的日期                                |
  | @Max(value=)                             | 值必须小于value指定的值。不能注解在字符串类型上               |
  | @Min(value=)                             | 值必须大于value指定的值。不能注解在字符串类型上               |

  ​