package cn.jjgx.controller;


import cn.jjgx.Service.TaskService;
import cn.jjgx.Service.UserService;
import cn.jjgx.mapper.TaskMapperPlus;
import cn.jjgx.mapper.UserMapperPlus;
import cn.jjgx.pojo.Task;
import cn.jjgx.pojo.User;
import cn.jjgx.util.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Random;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserMapperPlus userMapperPlus;

    @Autowired
    TaskMapperPlus taskMapperPlus;

    @Autowired
    TaskService taskService;

    @Autowired
    ServletContext servletContext;

    @Autowired
    UserService userService;

    @Autowired
    MailUtil mailUtil;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "UserLogin";
    }

    @RequestMapping("/UserLogin")
    public String UserLogin(String user_id , String pwd , @RequestParam(defaultValue = "false") boolean rememberMe , Model model , HttpServletRequest request){

        Subject subject = SecurityUtils.getSubject();


        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user_id,pwd,rememberMe);
        //usernamePasswordToken.setRememberMe(true);

        try {
//            System.out.println(usernamePasswordToken);
            // 交给shiro登录验证
            subject.login(usernamePasswordToken);
            Session session = subject.getSession();

            //通过user_id获取user对象

            User user = userService.selectUserByUid(user_id);

//            System.out.println(user);
//            System.out.println(user1);
            //将当前用户id记录到session当中
            session.setAttribute("id",user.getId());
            //将角色放入session中，以便thymeleaf获取
            HttpSession tomcatSession = request.getSession();
            tomcatSession.setAttribute("user" , user);
            //将任务从servletContext刷新到Session中
            taskService.flashTaskToSessionByNumber(tomcatSession , 0);
            //将tasks放入上下文中，方便index随时获取
//            servletContext.setAttribute("tasks" , taskMapperPlus.selectList(new QueryWrapper<Task>()));
            return "index";
        } catch (UnknownAccountException e) { // 用户名不存在
            e.printStackTrace();
            model.addAttribute("msg","用户名错误");
            return "UserLogin";
        } catch (IncorrectCredentialsException e){// 密码错误
            e.printStackTrace();
            model.addAttribute("msg","密码错误");
            return "UserLogin";
        }
    }

    @RequestMapping("/loginOut")
    public String loginOut(Model model){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        model.addAttribute("msg" , "用户已登出");
        return "UserLogin";
    }

    @RequestMapping("/toEmail")
    public String toEmail(){
        return "email";
    }

    @RequestMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("email") String email , Model model){

        StringBuffer sb = new StringBuffer();

        Random random = new Random(System.currentTimeMillis());

        for(int i = 0 ; i < 4 ; i++) {
            sb.append(random.nextInt(10));
        }
        String captcha = sb.toString();

        //设置十分钟后删除context中的验证码数据
        mailUtil.ScheduledCaptcha(email , captcha);

        String content = "这是家教共享网站的验证码，请妥善保管，不要泄露给他人，如果这不是您的信息，请您忽略";
        //发送目标邮件验证码
        MailUtil.sendCaptchaToEmail( content, email , captcha);

        model.addAttribute("email" , email);

        return "forgetPwd";
    }


//    @RequestMapping("/toForgetPwd")
//    public String toForgetPwd(){
//
//        return "forgetPwd";
//    }

    @RequestMapping("/forgetPwd")
    public String forgetPwd(String email , String captcha , Model model , @RequestParam("pwd") String pwd ,@RequestParam("pwd_check") String pwd_check){
        if(!pwd.equals(pwd_check)){
            model.addAttribute("msg" , "两次密码不正确，请重新输入新密码");
            return "forgetPwd";
        }
        String attribute = (String) servletContext.getAttribute(email);
        if(Objects.isNull(attribute)){
            model.addAttribute("msg" , "验证码过期，请重新发送验证码");
            return "email";
        }else {
            if(captcha.equals(attribute)){
                userService.updatePwdByEmail(email , pwd);
                model.addAttribute("msg" , "密码重置成功，请重新登陆");
                return "UserLogin";
            }else {
                model.addAttribute("msg" , "验证码输入错误，请重新输入");
                return "forgetPwd";
            }
        }

//        return "UserLogin";
    }

}
