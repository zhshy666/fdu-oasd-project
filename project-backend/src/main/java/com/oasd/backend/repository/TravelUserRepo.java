package com.oasd.backend.repository;

import com.oasd.backend.domain.TravelUser;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

    private List<TravelUser> findUsers(String sql){
        return jdbcTemplate.query(sql, new RowMapper<TravelUser>(){
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
    }

    public TravelUser findUserByUsernameAndPass(String username, String password){
        String sql = "select * from traveluser where UserName='" +
                username + "'" +
                "and Pass='" +
                password + "'";
        List<TravelUser> userList = findUsers(sql);
        if(userList.isEmpty())
            return null;
        return userList.get(0);
    }

    public TravelUser findUserByUsername(String username) {
        String sql = "select * from traveluser where UserName='" + username + "'";
        List<TravelUser> userList = findUsers(sql);
        return userList.isEmpty() ? null : userList.get(0);
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
    }

    public boolean findUserByEmail(String email) {
        String sql = "select * from traveluser where Email='" + email + "'";
        List<TravelUser> userList = findUsers(sql);
        return !userList.isEmpty();
    }

    public TravelUser findUserByEmailAndPass(String email, String password) {
        String sql = "select * from traveluser where Email='" +
                email + "'" +
                "and Pass='" +
                password + "'";
        List<TravelUser> userList = findUsers(sql);
        if(userList.isEmpty())
            return null;
        return userList.get(0);
    }

    public List<TravelUser> findUserLikeUsername(String username) {
        String sql = "select * from traveluser where UserName like '%" + username + "%'";
        return findUsers(sql);
    }

    public TravelUser findUserById(int id) {
        String sql = "select * from traveluser where UID='" + id + "'";
        List<TravelUser> userList = findUsers(sql);
        return userList.isEmpty()? null : userList.get(0);
    }
}
