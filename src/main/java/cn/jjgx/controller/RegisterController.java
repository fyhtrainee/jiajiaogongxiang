package cn.jjgx.controller;


import cn.jjgx.mapper.UserMapperPlus;
import cn.jjgx.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    UserMapperPlus userMapperPlus;


    private User getIdByUid(String uid){
        QueryWrapper<User> qw = new QueryWrapper<>();

        qw.eq("user_id" , uid);

        return userMapperPlus.selectOne(qw);
    }

    @RequestMapping("/toRegister")
    public String toRegister(){
        return "register";
    }


    @RequestMapping("/register")
    public String register(String user_id ,
                           String pwd_check ,
                           String pwd ,
                           String name ,
                           String phone_nb ,
                           String email ,
                           Character gender,
                           String role ,
                           Model model){

//        System.out.println(role);
        User idByUid = getIdByUid(user_id);

        if(!pwd_check.equals(pwd)){
            model.addAttribute("msg" , "两次密码输入不一致，请重新输入");
            return "register";
        }


        if(Objects.nonNull(idByUid)) {
            model.addAttribute("msg" , "用户名已存在，请重新输入");
            return "register";
        }

        if(userMapperPlus.selectCount(new QueryWrapper<User>().eq("email" , email)) > 0){
            model.addAttribute("msg" , "该邮箱已经绑定账号，请先解绑");
        }

        if(!email.contains("@")){
            model.addAttribute("msg" , "邮箱格式不正确，请重新输入");
            return "register";
        }

        User user = new User();

        user.setUserId(user_id);
        user.setName(name);
        user.setPhoneNb(phone_nb);
        user.setEmail(email);
        user.setGender(gender);
        user.setRole(role);


        String algorithmName = "md5";
        //对密码进行加密
        String newPassword = new SimpleHash(
                algorithmName,
                pwd,
                ByteSource.Util.bytes(user_id + name),
                2).toString();
        //获取加密后密码并存储

//        System.out.println(user_id + name);

        user.setPwd(newPassword);
        userMapperPlus.insert(user);
        model.addAttribute("msg" , "注册成功，请重新输入用户名与密码");

        return "UserLogin";


    }

}
