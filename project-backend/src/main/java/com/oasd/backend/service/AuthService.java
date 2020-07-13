package com.oasd.backend.service;

import com.oasd.backend.domain.TravelUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class AuthService {
    @Resource
    private JdbcTemplate jdbcTemplate;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthService() {

    }

    /**
     * @param username username from the front end
     * @param password password from the front end
     * @return return token info to the front end(call generateToken func)
     * return value sent to : AuthController
     */
    public TravelUser login(String username, String password) {
        TravelUser user= findByUsername(username, password);
        return user;
        // Step1 : user can be found or not
//        if(user == null){
//            return null;
//        } else {
//            // Step2: username and password match or not
//            String psw = user.getPassword();
//            if(passwordEncoder.matches(password,psw)){
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                return user;
//            }else{
//                return null;
//            }
//        }
    }

    public TravelUser findByUsername(String username, String password){
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
}