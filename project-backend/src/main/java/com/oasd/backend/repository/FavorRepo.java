package com.oasd.backend.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FavorRepo {
    @Resource
    private JdbcTemplate jdbcTemplate;
    public void removeFavorsByImageId(int imageId) {
        String sql = "delete from travelimagefavor where ImageID = '" +
                imageId + "'";
        jdbcTemplate.update(sql);
        System.out.println("Delete favors success");
    }
}
