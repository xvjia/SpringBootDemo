package com.xvjialing.girl.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

    @ApiOperation(value = "test",notes = "notes")
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String say(){
        return "index";
    }
}
