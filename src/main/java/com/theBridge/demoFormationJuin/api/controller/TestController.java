package com.theBridge.demoFormationJuin.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping
    public String test() {
        return "hey test";
    }

    @GetMapping("/admin")
    public String adminApi() {
        return "API accessible seulement par ADMIN";
    }

    @GetMapping("/user")
    public String userApi() {
        return "API accessible seulement par USER";
    }
}
