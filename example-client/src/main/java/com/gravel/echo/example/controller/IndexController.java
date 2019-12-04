package com.gravel.echo.example.controller;

import com.gravel.echo.example.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @ClassName IndexController
 * @Description: TODO
 * @Author gravel
 * @Date 2019/11/28
 * @Version V1.0
 **/
@Controller
public class IndexController {
    @Autowired
    HelloService helloService;

    @RequestMapping("index")
    @ResponseBody
    public List<String> index(){
        List<String> s = new ArrayList<>();
        s.add("234");
        s.add("234");
        s.add("234");
        s.add("234");
        return  helloService.saySomething(s);
    }
}
