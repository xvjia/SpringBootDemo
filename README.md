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

| 注解符 | 作用 |
| --------- | :--------------: |
| @Controller      | 处理http请求 |
| @RestController  | Spring4之后新加的注解，原来返回json需要@ReponseBody配合@Controller|
| @RequestMapping  | 配置url映射  |
| @PathVariable    | 获取url中的值 |
| @RequestParam    | 获取请求参数的值 |
| @GetMapping      | 组合注解  |

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

  ​