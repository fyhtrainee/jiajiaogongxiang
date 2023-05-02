package cn.jjgx;

import cn.jjgx.mapper.OrderMapperPlus;
import cn.jjgx.mapper.TaskMapperPlus;
import cn.jjgx.mapper.UserMapperPlus;
import cn.jjgx.pojo.Order;
import cn.jjgx.pojo.Task;
import cn.jjgx.pojo.User;
import cn.jjgx.util.JsonStringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@SpringBootTest
class JjgxApplicationTests {

    @Autowired
    UserMapperPlus userMapperPlus;

    @Autowired
    OrderMapperPlus orderMapperPlus;

    @Autowired
    TaskMapperPlus taskMapperPlus;


    @Autowired
    ServletContext servletContext;

    @Test
    void contextLoads() {

        User user = userMapperPlus.selectById(1234);

        System.out.println(user);
    }

    @Test

    public void test01(){

        User user = new User();

        user.setName("张三");

        userMapperPlus.insert(user);
    }


    @Test
    public void test03(){


        IPage iPage = new Page(1,3);
        userMapperPlus.selectPage(iPage,null);


        System.out.println(iPage.getPages());
        System.out.println(iPage.getCurrent());
        System.out.println(iPage.getTotal());

        System.out.println(iPage.getRecords());



    }


    @Test
    public void test04(){

        System.out.println(LocalDateTime.now());

    }

    @Test
    public void test05() {

        Order o = orderMapperPlus.selectById(2);

        System.out.println(o);

    }

    @Test
    public void test06(){

        QueryWrapper<User> qw = new QueryWrapper<>();

        qw.select("id").eq("user_id" , "user2");

        User user = userMapperPlus.selectOne(qw);

        System.out.println(user);

    }

    @Test
    public void test07(){

        String string = new Md5Hash("12345", "1234" + 1234).toString();

        //5decc7b4de107e76994c4c533fc6bd1d
        System.out.println(string);



    }

    @Test
    public void test08() throws JsonProcessingException {



        ObjectMapper objectMapper = new ObjectMapper();

        //将数据库中Json数据读取成ArrayList并添加数据再转回Json数据
//        ArrayList<String> strings = new ArrayList<String>();
//        ArrayList<String> objectReader = objectMapper.readValue(state ,strings.getClass());
//        objectReader.add(take_id);


        ArrayList<String> objectReader = new ArrayList<String>();

        objectReader.add("test");
        objectReader.add("test2");
        objectReader.add("test3");


        String concat_task = objectMapper.writeValueAsString(objectReader);


        System.out.println(concat_task);


        ArrayList arrayList = objectMapper.readValue(concat_task, objectReader.getClass());
        System.out.println(arrayList);


    }    @Test
    public void test09() throws JsonProcessingException, InstantiationException, IllegalAccessException {



        ObjectMapper objectMapper = new ObjectMapper();

        //将数据库中Json数据读取成ArrayList并添加数据再转回Json数据
//        ArrayList<String> strings = new ArrayList<String>();
//        ArrayList<String> objectReader = objectMapper.readValue(state ,strings.getClass());
//        objectReader.add(take_id);


        ArrayList<String> objectReader = new ArrayList<String>();

        objectReader.add("test");
        objectReader.add("test2");
        objectReader.add("test3");


        String concat_task = objectMapper.writeValueAsString(objectReader);


        System.out.println(concat_task);


        ArrayList<String> arrayList = JsonStringUtil.StringToObject(concat_task, ArrayList.class);

        System.out.println(arrayList);

    }
    @Test
    public void test10(){


        List<Task> tasks = taskMapperPlus.selectNewTask(0);

        for(Task task : tasks){
            System.out.println(task);
        }


    }

    @Test
    public void test11(){

        QueryWrapper<Task> qw = new QueryWrapper<Task>();
        qw.isNull("take_id");

        List<Task> tasks = taskMapperPlus.selectList(qw);
        for(Task t : tasks){
            System.out.println(t);
        }
    }

    @Test
    public void test12(){

        String captcha = (String)servletContext.getAttribute("2424169742@qq.com");

        System.out.println(captcha);

        servletContext.removeAttribute("2424169742@qq.com");

    }








}
