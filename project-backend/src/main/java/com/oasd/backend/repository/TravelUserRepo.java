package com.oasd.backend.repository;

import com.oasd.backend.domain.TravelUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class TravelUserRepo {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public TravelUser findUserByUsernameAndPass(String username, String password){
        String sql = "select * from traveluser where UserName='" +
                username + "'" +
                "and Pass='" +
                password + "'";
        List<TravelUser> userList = jdbcTemplate.query(sql, new RowMapper<TravelUser>(){
            TravelUser user = null;
            @Override
            public TravelUser mapRow(ResultSet resultSet, int i) throws SQLException {
                user = new TravelUser();
                user.setId(resultSet.getInt("UID"));
                user.setPassword(resultSet.getString("Pass"));
                user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("UserName"));
                user.setState(resultSet.getString("State"));
                user.setDateJoined(resultSet.getString("DateJoined"));
                user.setDateLastModified(resultSet.getString("DateLastModified"));
                return user;
            }
        });
        if(userList.isEmpty())
            return null;
        return userList.get(0);
    }

    public boolean findUserByUsername(String username) {
        String sql = "select * from traveluser where UserName='" + username + "'";
        List<TravelUser> userList = jdbcTemplate.query(sql, new RowMapper<TravelUser>(){
            TravelUser user = null;
            @Override
            public TravelUser mapRow(ResultSet resultSet, int i) throws SQLException {
                user = new TravelUser();
                user.setId(resultSet.getInt("UID"));
                user.setPassword(resultSet.getString("Pass"));
                user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("UserName"));
                user.setState(resultSet.getString("State"));
                user.setDateJoined(resultSet.getString("DateJoined"));
                user.setDateLastModified(resultSet.getString("DateLastModified"));
                return user;
            }
        });
        return !userList.isEmpty();
    }

    public void insertUser(String username, String password, String email) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String dateJoined = formatter.format(date);
        String state = "1";
        String sql = "insert into traveluser values (null, '" + email +
                "', '" + username +
                "', '" + password +
                "', '" + state +
                "', '" + dateJoined +
                "', '" + dateJoined +
                "')";
        jdbcTemplate.update(sql);
        System.out.println("Register success");
    }

}
