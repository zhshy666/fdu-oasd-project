package com.oasd.backend.service;

import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.repository.TravelUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.List;

/**
 * @author LBW
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private TravelUserRepo travelUserRepo;

    @Autowired
    public JwtUserDetailsService(TravelUserRepo travelUserRepo) {
        this.travelUserRepo = travelUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        TravelUser user = travelUserRepo.findUserByUsername(username);
        return user;
    }
}
