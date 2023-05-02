package cn.jjgx.mapper;


import cn.jjgx.pojo.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskMapperPlus extends BaseMapper<Task> {

    @Select("select * from task  where take_id is null order by ct_time desc limit #{index} , 10")
    public List<Task> selectNewTask(Integer index);

}
