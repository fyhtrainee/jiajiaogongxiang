package cn.jjgx;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

//@MapperScan("cn.jjgx.mapper")
@SpringBootApplication

public class JjgxApplication {

    public static void main(String[] args) {
        SpringApplication.run(JjgxApplication.class, args);
    }

}
