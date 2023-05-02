package cn.jjgx.Service;


import cn.jjgx.mapper.UserMapperPlus;
import cn.jjgx.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    UserMapperPlus userMapperPlus;


    public void addOrderForUser(Long id , Long taskId) throws JsonProcessingException {
        //将order_list数据中添加上task_id
        ObjectMapper objectMapper = new ObjectMapper();

        QueryWrapper<User> qw = new QueryWrapper<User>();
        qw.select("order_list").eq("id" , id);

        User user1 = userMapperPlus.selectOne(qw);
        String orderList1 = user1.getOrderList();
        ArrayList<Long> arrayList = new ArrayList<>();
        if(!orderList1.equals("")) {
            arrayList = objectMapper.readValue(orderList1, ArrayList.class);
        }
        arrayList.add(taskId);
        String string = objectMapper.writeValueAsString(arrayList);

        UpdateWrapper<User> uw = new UpdateWrapper<User>();
        uw.set("order_list" , string).eq("id" , id);
        userMapperPlus.update(new User() , uw);

    }

    public User selectIdRoleByUid(String uid){
        QueryWrapper<User> uqw = new QueryWrapper<>();
        uqw.select("id" , "role").eq("user_id" , uid);
        return userMapperPlus.selectOne(uqw);
    }

    public User selectUserByUid(String uid){

        QueryWrapper<User> uqw = new QueryWrapper<>();
        uqw.eq("user_id" , uid);
        return userMapperPlus.selectOne(uqw);
    }

    public Long selectIdByuId(String uid){

        QueryWrapper<User> qw = new QueryWrapper<User>();
        qw.eq("user_id" , uid).select("id");

        User user = userMapperPlus.selectOne(qw);
        return user.getId();
    }

    public User selectNameAndUserIdByEmail(String email){

        QueryWrapper<User> uqw = new QueryWrapper<>();
        uqw.select("user_id" , "name").eq("email" , email);
        return userMapperPlus.selectOne(uqw);
    }

    public void updatePwdByEmail(String email , String pwd){

        String algorithmName = "md5";
        User user = selectNameAndUserIdByEmail(email);

        String newPassword = new SimpleHash(
                algorithmName ,
                pwd ,
                ByteSource.Util.bytes(user.getUserId() + user.getName()),
                2
        ).toString();

        UpdateWrapper<User> uuw = new UpdateWrapper<>();
        uuw.eq("email" , email).set("pwd" , newPassword);
        userMapperPlus.update(null,  uuw);

    }

}
