package cn.jjgx.controller;


import cn.jjgx.Service.OrderService;
import cn.jjgx.Service.UserService;
import cn.jjgx.mapper.OrderMapperPlus;
import cn.jjgx.mapper.UserMapperPlus;
import cn.jjgx.pojo.Order;
import cn.jjgx.pojo.User;
import cn.jjgx.util.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Random;


@Controller
@RequestMapping("order")
public class OrderController {


    @Autowired
    OrderMapperPlus orderMapperPlus;

    @Autowired
    UserMapperPlus userMapperPlus;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    //通过user_id获取id和role
    private User getId_RoleByUid(String uid){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("user_id" , uid).select("id" , "role");
        return userMapperPlus.selectOne(qw);
    }


    /**
     * 订单结束后设置ed_time并结束订单
     * @return
     */
    @RequestMapping("/endOrder")
    public String endOrder(@RequestParam(value = "id") String order_id , Model model){

        //获取shiro管理的session
//        Subject subject = SecurityUtils.getSubject();
//        Session session = subject.getSession();
//        Long id = (Long)session.getAttribute("id");

        Long aLong = Long.valueOf(order_id);


        //判断订单是否已经结束，或者尚未开始

            Order order = orderService.selectOrderEdTimeById(aLong);
            System.out.println(order);
            if(Objects.nonNull(order)){
                model.addAttribute("msg" , "订单已结束");
                return "index";
            }


        if(Objects.isNull(orderService.selectOrderStTimeById(aLong))){
            model.addAttribute("msg" , "点单未开始，请先开始订单");
            return "index";
        }

        if(orderService.endOrder(aLong) != 1){
            model.addAttribute("msg" , "订单结束失败");
            return "index";
        }

        return "index";

    }

    @RequestMapping("/toCreatOrder")
    public String toCreatOrder(){

        return "create_order";
    }


    /**
     * 创建订单
     * 根据task或者直接创建，创建会生成一个4位数的上课码，输入此码才可以开始订单
     * 创建订单的角色没有限制，系统会自适应角色，但是承接订单和发布订单的用户角色不可以相同
     * @param uid
     * @param model
     * @return
     */

    @RequestMapping("/createOrder")
    public String createOrder(String uid ,
                              Model model ,
                              HttpServletRequest request ) {

        Long thId = null;
        Long stId = null;

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Long id = (Long)session.getAttribute("id");

        User user = null ;
        Long take_id = null;

        // take_id : 承接任务的用户id

        //判断来自task 还是直接创建
        if(Objects.nonNull(uid)){
            user = userService.selectIdRoleByUid(uid);
            if(user.getRole().equals("student")){
                model.addAttribute("msg" , "目标用户不是老师，请确认用户名");
                return "index";
            }

            take_id = user.getId();
        }else {
            take_id = (Long)request.getAttribute("user_id");
            user = userMapperPlus.selectById(take_id);
        }


        //检查是否存在该用户
        if(Objects.isNull(take_id)) {
            model.addAttribute("msg","无法找到该用户，请确认用户的用户名是否有效");
            return "index";
            //检查该用户是否为老师
        }

        //自动适应角色
        if(user.getRole().equals("teacher")) {
            thId = take_id;
            stId = id;
        }else if(user.getRole().equals("student")){
            thId = id;
            stId = take_id;
        }else {
            model.addAttribute("msg" , "该用户暂无身份");
            return "index";
        }

        if(thId.equals(stId)){
            model.addAttribute("msg" , "不能创建自己的订单");
            return "index";
        }

        StringBuilder sb = new StringBuilder();

        // 获取四位随机验证码，该验证码只有学生知晓
        Random random = new Random(System.currentTimeMillis());
        for(int i = 0 ; i < 4 ; i++){
            sb.append( random.nextInt(10));
        }

        String sCaptcha = new String(sb);

        Order order = new Order();

        //添加订单数据
        order.setStId(stId);
        order.setThId(thId);
        order.setCtTime(Timestamp.valueOf(LocalDateTime.now()));
        order.setStTime(null);
        order.setEdTime(null);
        order.setCtId(id);
        order.setCaptcha(sCaptcha);


        System.out.println(order);
        orderMapperPlus.insert(order);

        model.addAttribute("msg","订单创建成功，老师的上课码为" + sCaptcha +
                "已将该验证码发往您的邮箱，请牢记该码，并在上课开始前出示给老师");
        
        String content = "这是您的上课码，请在老师上课时出示给老师，请您妥善保管";
        MailUtil.sendCaptchaToEmail(content , user.getEmail() , sCaptcha );
        return "order";
    }


    @RequestMapping("/deleteOrder")
    public String deleteOrder(String id , Model model){

        int i = orderMapperPlus.deleteById(id);
        if(i > 0){
            model.addAttribute("msg" , "删除失败" );
        }else {
            model.addAttribute("msg" , "删除成功");
        }

        return "index";
    }

    @RequestMapping("toCaptcha")
    public String toCaptcha(){
        return "captcha";
    }


    /**
     * 获取老师的订单列表
     *
     * @param model
     * @return
     */

    @RequestMapping("/toOrderStart")
    public String toOrderStart(Model model){

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Long id = (Long)session.getAttribute("id");
        QueryWrapper<Order> qw = new QueryWrapper();
        qw.eq("th_id" , id);
        List<Order> orders = orderMapperPlus.selectList(qw);

        if(orders != null) {
            if(orders.size() == 1){
                return "captcha";
            }else{
                return "choose_order";
            }
        }
        else {
            model.addAttribute("order_msg" , "您目前没有订单");
            return "index";
        }
    }



    @RequestMapping("/choose_OrderStart")
    public String choose_OrderStart(String id , Model model){

        Order order = orderService.selectOrderStTimeById(Long.valueOf(id));

        //如果查询不到stTime则会直接返回空值，值得注意的是，在数据库中未开始的时间设为0，查询到的映射值为null
        if(Objects.nonNull(order)){
            model.addAttribute("msg" , "订单已开始，不可重复开始");
            return "index";
        }

        model.addAttribute("order_id" , id);
        return "captcha";

    }



    @RequestMapping("/captcha_OrderStart")
    public String captcha_OrderStart(Model model , String captcha , String order_id){

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Long id = (Long)session.getAttribute("id");

        UpdateWrapper<Order> orderUpdateWrapper = new UpdateWrapper<>();

        //获取具体的订单信息
        Long aLong = Long.valueOf(order_id);
        Order order = orderMapperPlus.selectById(aLong);



        //判断验证码是否正确
        if(!order.getCaptcha().equals(captcha)) {
            model.addAttribute("msg", "验证码错误，请重新输入");
            model.addAttribute("order_id", order_id);
            return "captcha";
        }

        //开始订单
        orderUpdateWrapper.set("st_time" ,
                        Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)))
                        .eq("id" , aLong);
        orderMapperPlus.update(new Order(),orderUpdateWrapper);
        model.addAttribute("reason" , "订单已开始");

        return "order";
    }


    @RequestMapping("toChoose_order")
    public String toChoose_order(Model model){

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Long id = (Long)session.getAttribute("id");

        Order order = new Order();
        QueryWrapper<Order> qw = new QueryWrapper<>(order);
        qw.eq("th_id" , id);

        List<Order> orders = orderMapperPlus.selectList(qw);

        orders.removeIf(o -> !Objects.isNull(o.getEdTime()));

        model.addAttribute("orders" , orders);
        return "choose_order";
    }


    }
