package com.oasd.backend.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
public class FriendRepo {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public boolean areFriends(int id1, int id2) {
        String sql = "select id from friends where userA = '" + id1 + "' and userB = '" + id2 + "' union " +
                "select id from friends where userA = '" + id2 + "' and userB = '" + id1 + "'";
        List<Integer> list = jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getInt("id"));
        return !list.isEmpty();
    }

    public void addFriends(int userA, int userB) {
        String sql = "insert into friends values (null, ?, ?)";
        jdbcTemplate.update(sql, userA, userB);
    }

    public Set<Integer> findFriendsById(int id) {
        String sql = "select userB from friends where userA = '" + id +"'";
        String sql2 ="select userA from friends where userB = '" + id +"'";
        List<Integer> listA = jdbcTemplate.query(sql2, (resultSet, i) -> resultSet.getInt("userA"));
        List<Integer> listB = jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getInt("userB"));
        Set<Integer> result = new LinkedHashSet<>();
        result.addAll(listA);
        result.addAll(listB);
        return result;
    }
}