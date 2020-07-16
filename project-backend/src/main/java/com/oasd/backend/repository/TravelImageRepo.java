package com.oasd.backend.repository;

import com.oasd.backend.domain.TravelImage;
import com.oasd.backend.domain.TravelUser;
import org.omg.CORBA.IMP_LIMIT;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;

@Repository
public class TravelImageRepo {
    @Resource
    private JdbcTemplate jdbcTemplate;
    public List<TravelImage> findImageByHeat() {
        String sql = "select * from travelimage order by Heat desc limit 5";
        return findImages(sql);
    }

    public List<TravelImage> findImageByTime() {
        String sql = "select * from travelimage order by ReleasedTime desc limit 6";
        return findImages(sql);
    }

    private List<TravelImage> findImages(String sql){
        return jdbcTemplate.query(sql, new RowMapper<TravelImage>(){
            TravelImage image = null;
            @Override
            public TravelImage mapRow(ResultSet resultSet, int i) throws SQLException {
                image = new TravelImage();
                image.setImageId(resultSet.getInt("ImageId"));
                image.setTitle(resultSet.getString("Title"));
                image.setDescription(resultSet.getString("Description"));
                image.setLatitude(resultSet.getDouble("Latitude"));
                image.setLongitude(resultSet.getDouble("Longitude"));
                image.setCityCode(resultSet.getInt("CityCode"));
                image.setCountry_RegionCodeISO(resultSet.getString("Country_RegionCodeISO"));
                image.setPATH(resultSet.getString("Path"));
                image.setUsername(resultSet.getString("Username"));
                image.setContent(resultSet.getString("Content"));
                image.setHeat(resultSet.getInt("Heat"));
                image.setReleasedTime(resultSet.getString("ReleasedTime"));
                return image;
            }
        });
    }
}