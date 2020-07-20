package com.oasd.backend.repository;

import com.oasd.backend.domain.Favor;
import com.oasd.backend.domain.TravelImage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    private List<Favor> findFavors(String sql){
        return jdbcTemplate.query(sql, new RowMapper<Favor>(){
            Favor favor = null;
            @Override
            public Favor mapRow(ResultSet resultSet, int i) throws SQLException {
                favor = new Favor();
                favor.setImageId(resultSet.getInt("imageId"));
                favor.setUID(resultSet.getInt("UID"));
                return favor;
            }
        });
    }

    public boolean findIsFavorImage(int imageId, int userId) {
        String sql = "select * from travelimagefavor where UID = '" +
                userId + "' and ImageID = '" +
                imageId + "'";
        List<Favor> favors = findFavors(sql);
        return !favors.isEmpty();
    }

    public String deleteFavorByUIDAndImageID(int userId, int imageId) {
        String sql = "delete from travelimagefavor where ImageID = '" +
                imageId + "' and UID = '" +
                userId + "'";
        jdbcTemplate.update(sql);
        return "success";
    }

    public String insertFavor(int userId, int imageId) {
        String sql = "insert into travelimagefavor values (null, ?, ?)";
        jdbcTemplate.update(sql, userId, imageId);
        return "success";
    }

    public List<Integer> findImageByUID(int userId) {
        String sql = "select ImageID from travelimagefavor where UID = '" + userId + "'";
        return jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getInt("ImageID"));
    }
}
