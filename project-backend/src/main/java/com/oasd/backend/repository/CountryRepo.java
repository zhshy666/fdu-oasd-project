package com.oasd.backend.repository;

import com.oasd.backend.domain.Country;
import com.oasd.backend.domain.TravelImage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CountryRepo {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public String findNameByISO(String iso) {
        String sql  = "select Country_RegionName from geocountries_regions where ISO = '" +
                iso + "'";
        List<String> list = jdbcTemplate.query(sql, new RowMapper<String>() {
            String result = null;
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("Country_RegionName");
            }
        });
        return list.get(0);
    }
}
