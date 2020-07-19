package com.oasd.backend.repository;

import com.oasd.backend.domain.City;
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

    public List<City> findCitiesOfCountry(String ISO) {
        String sql  = "select * from geocities where Country_RegionCodeISO = '" +
                ISO +"'";
        return jdbcTemplate.query(sql, new RowMapper<City>() {
            City city = null;
            @Override
            public City mapRow(ResultSet resultSet, int i) throws SQLException {
                city = new City();
                city.setId(resultSet.getInt("GeoNameID"));
                city.setValue(resultSet.getString("AsciiName"));
                city.setName(resultSet.getString("AsciiName"));
                return city;
            }
        });
    }

    public int findIdByName(String cityName) {
        String sql  = "select GeoNameID from geocities where AsciiName = '" +
                cityName + "'";
        List<Integer> list = jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getInt("GeoNameID"));
        return list.get(0);
    }
}
