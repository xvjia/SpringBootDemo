package com.xvjialing.girl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/hello")

public class HelloController {

    @Value("${high}")
    private String high;

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "/say",method = RequestMethod.GET)
    public String say(){
        return girlProperties.getCupsize()+girlProperties.getAge()+high;
    }

    @RequestMapping(value = "/hi/{id}")
    public String hi(@PathVariable("id") int id){
        return "id:"+id;
    }

    //@RequestMapping(value = "/sayhi")
    @GetMapping(value = "/sayhi")
    public String sayhi(@RequestParam(value = "id",defaultValue = "100",required = false) int id){
        return "id:"+id;
    }
}
