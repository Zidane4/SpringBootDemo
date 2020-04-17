package com.zidane.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

/**
 * 启动配置类
 *
 * @author Zidane
 * @since 2018-08-12
 */
@SpringBootApplication
@MapperScan("com.zidane.spring.dao.mapper")
@ImportResource({"classpath:transaction.xml",
        "classpath:blackList/blackListRefresh.service.xml"
})
public class SpringBootStartApplication  extends SpringBootServletInitializer {
    public static void main(String args[]) {
        SpringApplication.run(SpringBootStartApplication .class, args);
    }
}