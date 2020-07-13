package com.oasd.backend.service;

import com.oasd.backend.domain.TravelUser;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.List;

/**
 * @author LBW
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    public JwtUserDetailsService() {
        
    }
    private JdbcTemplate jdbcTemplate;
    @Override
    public UserDetails loadUserByUsername(String username){
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
        if(userList.isEmpty())
            return null;
        return userList.get(0);
    }

}
