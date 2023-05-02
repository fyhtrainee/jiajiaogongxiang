package cn.jjgx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class JsonStringUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     *
     * @param s 需要转换的字符串
     * @param c 转换的类
     * @param <T> 类的泛型
     * @return 如果转换成功返回对象，不成功返回null
     */

    public static <T> T StringToObject(String s , Class<T> c) {

        try {
            Constructor<T> constructor = c.getConstructor();
            T t = constructor.newInstance();
//        T t = c.newInstance();

            if(!s.equals(""))
                t = objectMapper.readValue(s, c);
            return t;
        } catch (JsonProcessingException | InstantiationException | IllegalAccessException | NoSuchMethodException
                 | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
