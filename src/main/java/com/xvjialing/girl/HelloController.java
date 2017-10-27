package com.xvjialing.girl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "hello")

public class HelloController {

    @Value("${high}")
    private String high;

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "say",method = RequestMethod.GET)
    public String say(){
        return girlProperties.getCupsize()+girlProperties.getAge()+high;
    }
}
