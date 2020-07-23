package com.oasd.backend.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CommentFavorRepo {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public boolean findCommentFavor(int userId, int commentId) {
        String sql  = "select id from commentfavor where userId = '" +
                userId + "' and commentId = '" +
                commentId + "'";
        List<Integer> list = jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getInt("id"));
        return !list.isEmpty();
    }

    public String deleteFavorByUserIdAndCommentId(int userId, int commentId) {
        String sql = "delete from commentfavor where userId = ? and commentId = ?";
        jdbcTemplate.update(sql, userId, commentId);
        return "success";
    }

    public String addFavorByUserIdAndCommentId(int userId, int commentId) {
        String sql = "insert into commentfavor values (null, ?, ?)";
        jdbcTemplate.update(sql, commentId, userId);
        return "success";
    }
}
