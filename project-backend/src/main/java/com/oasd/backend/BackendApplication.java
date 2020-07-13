package com.oasd.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BackendApplication.class, args);
//        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
//        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM demo");
//        System.out.println(result);
    }

}
