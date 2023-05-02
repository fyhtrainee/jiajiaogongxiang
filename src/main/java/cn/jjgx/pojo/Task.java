package cn.jjgx.pojo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Results;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor

//@Results()
@TableName(value = "task" , autoResultMap = true)
public class Task {

    @TableId()
    private Long id;
    private String comment;
    private Long ctId;
    private Long takeId;
    private Timestamp ctTime;
    private String state;
    private String ctRole;
}
