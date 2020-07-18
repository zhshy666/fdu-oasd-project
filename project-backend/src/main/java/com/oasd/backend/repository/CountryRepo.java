package com.oasd.backend.repository;

import com.oasd.backend.domain.Country;
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
        List<String> list = jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getString("Country_RegionName"));
        return list.get(0);
    }

    public List<Country> findCountries() {
        String sql  = "select * from geocountries_regions";
        return jdbcTemplate.query(sql, new RowMapper<Country>() {
            Country country = null;
            @Override
            public Country mapRow(ResultSet resultSet, int i) throws SQLException {
                country = new Country();
                country.setISO(resultSet.getString("ISO"));
                country.setValue(resultSet.getString("Country_RegionName"));
                country.setName(resultSet.getString("Country_RegionName"));
                return country;
            }
        });
    }

    public String findISOByName(String country) {
        String sql  = "select ISO from geocountries_regions where Country_RegionName = '" +
                country + "'";
        List<String> list = jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getString("ISO"));
        return list.get(0);
    }
}
