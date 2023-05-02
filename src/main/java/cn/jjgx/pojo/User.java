package cn.jjgx.pojo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.springframework.validation.annotation.Validated;


@Data
@AllArgsConstructor
@NoArgsConstructor


@TableName(value = "user" , autoResultMap = true)
//@Results(@Result ( property = "user_id" , column = ""))

public class User {

    private String name;
    private String pwd;
    private String phoneNb;
    private Integer locked;
    @TableId
    private Long id;
    private String userId;
    private Character gender;
    private String role;

    //目前申请的任务列表
    private String orderList;
    private String email;
}
