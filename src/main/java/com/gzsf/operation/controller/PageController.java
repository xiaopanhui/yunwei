package com.gzsf.operation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String toLogin(){
        return "";
    }

    @GetMapping("index")
    public String Index() {
        return "index";
    }
}
