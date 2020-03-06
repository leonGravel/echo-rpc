package com.gravel.echo.example.controller;

import com.gravel.echo.example.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
@RestController
public class IndexController {

    @Resource
    HelloService helloService;

    @RequestMapping("index")
    public List<String> index(@RequestParam(required = false) String str){
        List<String> s = new ArrayList<>();
        s.add(str);
        return  helloService.saySomething(s);
    }
}
