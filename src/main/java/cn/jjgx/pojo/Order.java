package cn.jjgx.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor


@Accessors(chain = true)
// 由于order是关键字，所以需要加``修饰
@TableName(value = "`order`" , autoResultMap = true )
//@Results(
//        @Result (property = "id" , column = "id") ,)
public class Order {

    @TableId
    private Long id;
    private Long thId;
    private Long stId;
    private Timestamp ctTime;
    private Timestamp stTime;
    private Timestamp edTime;
    private String captcha;
    private Long ctId;

}
