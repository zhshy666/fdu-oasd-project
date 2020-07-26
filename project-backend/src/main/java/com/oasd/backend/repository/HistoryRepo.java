package com.oasd.backend.repository;

import com.oasd.backend.domain.History;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HistoryRepo {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private List<History> findHistory(String sql){
        return jdbcTemplate.query(sql, new RowMapper<History>(){
            History history = null;
            @Override
            public History mapRow(ResultSet resultSet, int i) throws SQLException {
                history = new History();
                history.setId(resultSet.getInt("id"));
                history.setImageId(resultSet.getInt("imageId"));
                history.setUserId(resultSet.getInt("userId"));
                return history;
            }
        });
    }

    public List<History> findHistoryByUserID(int userId) {
        String sql = "select * from history where userId ='" + userId +"' order by time desc";
        return findHistory(sql);
    }

    public void insertHistory(int userId, int imageId, String time) {
        String sql = "insert into history values (null, ?, ?, ?)";
        jdbcTemplate.update(sql, userId, imageId, time);
    }

    public void updateHistory(int id, String time) {
        String sql = "update history set time = ? where id = ? ";
        jdbcTemplate.update(sql, time, id);
    }

    public void deleteLastHistory(int id) {
        String sql = "delete from history where id = '" +
                id + "'";
        jdbcTemplate.update(sql);
    }

    public List<Integer> findImageByUserID(int userId) {
        String sql  = "select imageId from history where userId = '" +
                userId + "' order by time desc";
        return jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getInt("imageId"));
    }

    public void deleteHistoryByImageId(int imageId) {
        String sql = "delete from history where ImageID = '" +
                imageId + "'";
        jdbcTemplate.update(sql);
    }
}
