package cn.jjgx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class HelloWorld {

    @RequestMapping( {"/hello","/map" })
    public String hello(Model model){



        return "helloWorld";
    }

    @RequestMapping("/test")
    public String test(){
        return "cart";
    }

}
