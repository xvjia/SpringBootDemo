package com.xvjialing.girl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GirlController {

    @Autowired
    private GirlRepository girlRepository;

    @Autowired
    private GirlService girlService;

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

    @GetMapping(value = "/girls/addTwo")
    public void addTwoGirl(){
        girlService.insertTwo();
    }
}
