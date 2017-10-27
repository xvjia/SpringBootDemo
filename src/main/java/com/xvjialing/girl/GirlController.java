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
}
