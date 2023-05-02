package cn.jjgx.controller;


import cn.jjgx.Service.TaskService;
import cn.jjgx.Service.UserService;
import cn.jjgx.mapper.TaskMapperPlus;
import cn.jjgx.mapper.UserMapperPlus;
import cn.jjgx.pojo.Task;
import cn.jjgx.pojo.User;
import cn.jjgx.util.JsonStringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/task")
public class TaskController {

    private static final int USER_MAX_TASK = 10;

    @Autowired
    UserMapperPlus userMapperPlus;

    @Autowired
    TaskMapperPlus taskMapperPlus;


    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @RequestMapping("/toCreateTask")
    public String toCreateTask(){
        return "create_task";
    }

    /**
     *  该类用以创建任务并发布
     * @param comment 任务描述
     * @param model 视图
     * @return
     */
    @RequestMapping("/createTask")
    public String createTask(String comment , Model model){

//        System.out.println(comment);

        Long id;

        User user;

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        id = (Long) session.getAttribute("id");
        user = userMapperPlus.selectById(id);

        //设置任务的要求以及发布角色和id
        Task task = new Task();
        task.setCtId(id);
        task.setComment(comment);
        //设定创建任务的创建者id
        task.setCtRole(user.getRole());


        int insert = taskMapperPlus.insert(task);
//        System.out.println(task);

        if (insert == 1){
            model.addAttribute("msg" , "任务创建成功");
            taskService.flashTaskToContext();
        }
        else
            model.addAttribute("msg" , "任务创建失败");
        return "index";
    }


    /**
     * 该类用以承接任务
     *
     * @param id 任务id
     * @param model
     * @return
     */
    @RequestMapping("/takeTask")
    public String takeTask(String id , Model model) throws JsonProcessingException, InstantiationException, IllegalAccessException {


        //System.out.println("============进入takeTask===========");
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Long take_id = (Long)session.getAttribute("id");

        ArrayList<Long> order_id_list = new ArrayList<>();
        ArrayList<Long> user_id_list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        //获取用户当前的take订单数并确认是否订单数超过USER_MAX_TASK
        User user = userMapperPlus.selectById(take_id);
        String orderList = user.getOrderList();

        order_id_list = JsonStringUtil.StringToObject(orderList , ArrayList.class);
        if(Objects.isNull(order_id_list)){
            order_id_list = new ArrayList<>();
        }

        if(order_id_list.size() >= USER_MAX_TASK){
            model.addAttribute("msg" , "个人最多同时接受10个未完成订单");
            return "index";
        }


        //判断是否订单role冲突
        Task task = taskMapperPlus.selectById(Long.valueOf(id));


        //判断是否为自己的订单
        if(task.getCtId().equals(take_id)){
            model.addAttribute("msg" , "不可承接自己的订单");
            return "index";
        }

//        System.out.println(task);
        String ct_role = task.getCtRole();
        String role = user.getRole();
        if(ct_role.equals(role)){
            String msg = "您的身份为" + role + "不可以承接该订单";
            model.addAttribute("msg" , msg);
            return "index";
        }



        //获取该task的申请人数列表
        String state = task.getState();

        user_id_list = JsonStringUtil.StringToObject(state , ArrayList.class);

        //判断是否已经申请过该任务
        boolean b = taskService.queryIfAlreadyTake(task.getState(), take_id);
        if(b){
            model.addAttribute("msg" , "您已申请过该任务，不可重复申请");
            return "index";
        }


        //判断该订单火热程度，如果人数过多，则不承接该订单

        if(user_id_list.size() >= USER_MAX_TASK) {
            model.addAttribute("msg" , "该订单太火啦，请选择其他订单把");
            return "index";
        }
        //将数据库中Json数据读取成ArrayList并添加数据再转回Json数据
        user_id_list.add(take_id);
        boolean b1 = taskService.updateStateById(Long.valueOf(id), user_id_list);
        if(!b1){
            model.addAttribute("msg" , "添加失败，请稍后重试");
            return "index";
        }

        //将task_id添加到user的order_list中
        userService.addOrderForUser(take_id , Long.valueOf(id));


        model.addAttribute("msg" , "申请订单成功，请耐心等待对方确认");
        return "index";
    }

    /**
     *
     * 展示我创建的任务
     *
     * @param model
     * @return
     */
    @RequestMapping("/myTasks")
    public String myTasks(Model model){

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Long id = (Long)session.getAttribute("id");

        QueryWrapper<Task> qw = new QueryWrapper<Task>();
        qw.eq("ct_id" , id);


        List<Task> tasks = taskMapperPlus.selectList(qw);

        tasks.removeIf(task -> Objects.nonNull(task.getTakeId()));

//        System.out.println(tasks);
        model.addAttribute("tasks" , tasks);

        return "choose_task";
    }

    @RequestMapping("/task")
    public String task(@RequestParam("id") String taskId
            , @RequestParam("ctId") String ctId
            , @RequestParam("state") String state
            ,  Model model)
            throws JsonProcessingException, InstantiationException, IllegalAccessException {

//        System.out.println("===========进入task======");

        //获取订单申请人数
        ArrayList<User> userList = new ArrayList<>();


        ArrayList<Long> userIdList = JsonStringUtil.StringToObject(state , ArrayList.class);

        userIdList.forEach(id -> userList.add(userMapperPlus.selectById(id)));

        //将申请用户列表交由前端
        model.addAttribute("userList" , userList);
        model.addAttribute("ct_id" ,ctId);
        model.addAttribute("task_id" , taskId);


        return "verify_task";
    }


    /**
     *
     * 该controller需要完成的业务是结束订单
     * （订单结束与否可直接通过查询take_id查看，理论上不用单独设置字段），
     * 并将订单信息转化为order
     *
     * @param taskId 任务id
     * @param userId 确认任务的承接对象的id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/endTask")
    public void endTask(@RequestParam("task_id") String taskId ,
                          @RequestParam("user_id") String userId ,
                          HttpServletRequest request ,
                          HttpServletResponse response
                          )
            throws ServletException, IOException {

        //获取task并设置对应的承接id
        Long l = Long.valueOf(userId);
        Task task = taskMapperPlus.selectById(Long.parseLong(taskId));
        task.setTakeId(l);
        //更新take_id
        UpdateWrapper<Task> uw = new UpdateWrapper<>();
        uw.set("take_id" , l).eq("id" , task.getId());
        int update = taskMapperPlus.update(new Task(), uw);
        if(update == 0){
            System.out.println("添加take_id失败");
        }
        //将数据转发给order模块创建订单
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/order/createOrder");

        request.setAttribute("task" , task);
        request.setAttribute("user_id" ,l );

        //task中会存在申请user的id，task结束时会去除user的申请
        taskService.deleteInvalidTask(task.getState() , taskId);

        taskService.flashTaskToContext();
        requestDispatcher.forward( request, response);

    }

    @RequestMapping("/searchTaskByUid")
    public String searchTaskByUid(@RequestParam("uid") String uid , Model model){


        List<Task> tasks = taskService.queryTaskByUser_id(uid);
        model.addAttribute("tasks" , tasks);
        return "search_task";
    }

}
