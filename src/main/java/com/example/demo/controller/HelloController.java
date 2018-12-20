package com.example.demo.controller;

import com.example.demo.domain.Demo;
import com.example.demo.properties.GirlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
//@Controller
//@RequestMapping("/hello")
@RestController
public class HelloController {

//    @Value("${cupSize}")
//    private String cupSize;
//
//    @Value("${age}")
//    private Integer age;
//
//    @Value("${content}")
//    private String content;

    @Autowired
    private GirlProperties girlProperties;

    /**
     * value = {"/hello", "hi"} 集合，两个 url 都可以访问
     * @RequestParam(value = "id", required = false, defaultValue = "0") Integer id 获取?id=100 的100
     * @PathVariable (value = "id") Integer id 获取/say/100 中的100
     * @return
     */

    @RequestMapping(value = {"/hello"}, method = RequestMethod.GET)
    public String sayHello() {
        return "hello ";
    }

    @GetMapping(value = "/say/{id}")
    public String say(@PathVariable (value = "id") Integer id) {
        //return cupSize;
        //return "index";
        //return content;
        //return girlProperties.getCupSize();
        return "id: " + id;
    }

    @RequestMapping("/getDemo")
    public Demo getDemo() {
        Demo demo = new Demo();
        demo.setId(1);
        demo.setName("张三");
        demo.setCreateTime(new Date());
        demo.setRemarks("这是备注信息");
        return demo;
    }

}