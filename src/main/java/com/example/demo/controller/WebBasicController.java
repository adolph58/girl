package com.example.demo.controller;

import com.example.demo.domain.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jaunty on 2018/7/15.
 */
@RestController()
@RequestMapping("/web")
public class WebBasicController {

    @RequestMapping("/test")
    public Result<String> test(HttpServletRequest request){
        String str = request.getParameter("name");
        return new Result<>(0, "获取成功",str);
    }
}
