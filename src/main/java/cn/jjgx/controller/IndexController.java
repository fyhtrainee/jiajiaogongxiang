package cn.jjgx.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @RequestMapping("/toIndex")
    public String toIndex(){

        return "index";
    }

}
