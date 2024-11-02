package com.example.firstproject.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController // REST API 구현시 사용하는 어노테이션(컨트롤러)
public class FirstApiController {
    @GetMapping("/api/hello")
    public String hello(){

        return "hello world!";
    }
}
