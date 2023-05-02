package cn.jjgx.Service;


import cn.jjgx.mapper.OrderMapperPlus;
import cn.jjgx.pojo.Order;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    OrderMapperPlus orderMapperPlus;

    public Order selectOrderStTimeById(Long id){

        QueryWrapper<Order> oqw = new QueryWrapper<Order>();
        oqw.select("st_time").eq("id", id);
//        Order order = new Order();
//        order.setId(id);
        return orderMapperPlus.selectOne(oqw);
    }

    public Order selectOrderEdTimeById(Long id){
        QueryWrapper<Order> oqw = new QueryWrapper<>();
        oqw.select("ed_time").eq("id" , id);

        return  orderMapperPlus.selectOne(oqw);
    }


    public int endOrder(Long id){

        UpdateWrapper<Order> ouw = new UpdateWrapper<>();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        System.out.println(timestamp);
        ouw.set("ed_time",timestamp).eq("id" , id);
//        ouw.eq("id" , id);
        return orderMapperPlus.update(null , ouw);
    }

    public int deleteOrderById(Long id){

        return orderMapperPlus.deleteById(id);
    }


}
