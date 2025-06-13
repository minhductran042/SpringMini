package com.minhductran.tutorial.minhductran;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {
    @GetMapping("hello")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("json")
    public JSONTEST getJson() {
        JSONTEST response = new JSONTEST();
        response.setMessage("TEST API");
        response.setCode("200");
        return response;
    }
}

class JSONTEST {
    private String message;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

