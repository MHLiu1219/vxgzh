package com.vxgzh.maoxiandao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.vxgzh.maoxiandao.mapper")
public class MaoxiandaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaoxiandaoApplication.class, args);
    }

}
