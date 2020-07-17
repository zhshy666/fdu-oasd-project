package com.oasd.backend.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CityRepo {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public String findNameById(int id) {
        String sql  = "select AsciiName from geocities where GeoNameID = '" +
                id + "'";
        List<String> list = jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getString("AsciiName"));
        return list.get(0);
    }
}
