package cn.jjgx.Service;


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
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class TaskService {

    @Autowired
    TaskMapperPlus taskMapperPlus;

    @Autowired
    UserMapperPlus userMapperPlus;

    @Autowired
    UserService userService;

    @Autowired
    ServletContext servletContext;

    /**
     * 依据id更新state
     * @param id taskId
     * @param state taskState
     * @return 是否更新成功
     */
    public boolean updateStateById(Long id , String state){

        UpdateWrapper<Task> tuw = new UpdateWrapper<Task>();
        tuw.set("state" , state).eq("id" , id);
        int update = taskMapperPlus.update(new Task(), tuw);
        return update > 0;
    }

    public boolean updateStateById(Long id , ArrayList<Long> state) throws JsonProcessingException {
        String s = JsonStringUtil.getObjectMapper().writeValueAsString(state);
        return updateStateById(id , s);
    }


    /**
     * 该service用来当task被确认时的用户申请记录的消除
     *
     * @param state 数据库中user的id的json数据，以ArrayList形式读取
     * @param taskId 为订单id
     *
     */
    public void deleteInvalidTask(String state , String taskId) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        //获取到申请的用户id
        ArrayList<Long> takeUserIds = objectMapper.readValue(state , ArrayList.class);
        UpdateWrapper<User> uw = new UpdateWrapper<User>();
        QueryWrapper<User> qw = new QueryWrapper<>();


        //按照每个用户查询到用户数据，去除掉当前task的任务，并且更新user的order_list

        for(Long l : takeUserIds){

            //按照用户id获取order_list，并通过json形式转化为ArrayList对象进行删除对应taskId
            qw.select("order_list").eq("id" , l);
            User user1 = userMapperPlus.selectOne(qw);

            System.out.println(user1.getOrderList());
            ArrayList<Long> order_list = new ArrayList<>();

            String orderList = user1.getOrderList();
            if(!orderList.equals("")){
                order_list = objectMapper.readValue(orderList , ArrayList.class);
            }
            order_list.removeIf( v -> v.equals(Long.valueOf(taskId)));

            //将对象转化为json形式存入数据库
            String jsonOrder_list = objectMapper.writeValueAsString(order_list);
            uw.eq("id" , l ).set("order_list" , jsonOrder_list);
            userMapperPlus.update(new User() , uw );

        }
    }

    /**
     *
     * 检查用户是否已经申请过该任务,如果已经申请过，则不更新task.state
     *
     * @param state task的用户id的申请列表
     * @param id 申请的用户id
     * @return 返回是否申请过该任务，true为是，false为否
     */
    public boolean queryIfAlreadyTake(String state , Long id){
        //将该state转化为ArrayList<Long>对象
        ArrayList<Long> takeIdList = new ArrayList<>();
        try {
            if(!state.equals(""))
                takeIdList = JsonStringUtil.getObjectMapper().readValue(state , ArrayList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //判断是否已经申请过该task
        boolean res = false;
        for(Long l : takeIdList){
            if (l.equals(id)) {
                res = true;
                break;
            }
        }

        return res;
    }

    /**
     * 根据用户名获取该用户发布的任务
     * @param user_id
     * @return
     */
    public List<Task> queryTaskByUser_id(String user_id){


        QueryWrapper<Task> qw = new QueryWrapper<Task>();
        Long id = userService.selectIdByuId(user_id);
        qw.eq("ct_id" , id);

        return taskMapperPlus.selectList(qw);
    }

    /**
     * 查询未结束的任务,以创建时间降序排列
     */
    public void flashTaskToContext(){

        QueryWrapper<Task> qw = new QueryWrapper<Task>();
        qw.isNull("take_id").orderByDesc("ct_time");
        List<Task> tasks = taskMapperPlus.selectList(qw);

        servletContext.setAttribute("tasks" , tasks);
    }

    /**
     * 将最新的10个未结束任务刷新到servletContext中去
     * @param pageNumber 页码
     */
    public void flashTaskToContext(Integer pageNumber ){

        if(Objects.isNull(pageNumber)){
            pageNumber = 0;
        }
        //代表一页十个
        Integer index = pageNumber * 10;

        List<Task> tasks = taskMapperPlus.selectNewTask(index);

        servletContext.setAttribute("tasks" , tasks);
    }

    /**
     * 将servletContext中的tasks按照页码刷新到session中
     * @param httpSession
     * @param pageNumber
     */
    public void flashTaskToSessionByNumber(HttpSession httpSession , int pageNumber){
        ArrayList<Task> tasks = (ArrayList<Task>) servletContext.getAttribute("tasks");
        tasks = Objects.isNull(tasks) ? new ArrayList<Task>() : tasks;
        ArrayList<Task> res = new ArrayList<>();
        int start = 10 * pageNumber;


        for(int i = start ; i < Math.min(start + 10 , tasks.size()) ; i++ ){
            res.add(tasks.get(i));
        }

        httpSession.setAttribute("tasks" , res);


    }



}
