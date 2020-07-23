package com.oasd.backend.repository;

import com.oasd.backend.domain.Comment;
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
public class CommentRepo {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private List<Comment> findComments(String sql){
        return jdbcTemplate.query(sql, new RowMapper<Comment>(){
            Comment comment = null;
            @Override
            public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
                comment = new Comment();
                comment.setCommentId(resultSet.getInt("commentId"));
                comment.setContent(resultSet.getString("content"));
                comment.setHeat(resultSet.getInt("heat"));
                comment.setImageId(resultSet.getInt("imageId"));
                comment.setUsername(resultSet.getString("username"));
                comment.setTime(resultSet.getString("time"));
                return comment;
            }
        });
    }

    public List<Comment> findCommentsByImageId(int imageId) {
        String sql = "select * from comment where imageId = '" + imageId +"' order by heat desc";
        return findComments(sql);
    }

    public void insertComment(Comment comment) {
        String sql = "insert into comment values (null, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, comment.getUsername(), comment.getImageId(), comment.getContent(), comment.getHeat(), comment.getTime());
    }

    public void updateHeatByCommentId(int commentId, int step) {
        String sql = "select * from comment where commentId = '"+commentId+"'";
        List<Comment> comments = findComments(sql);
        assert !comments.isEmpty();
        int heat = comments.get(0).getHeat();
        heat+=step;
        String sql2 = "update comment set Heat = ? where commentId = ?";
        jdbcTemplate.update(sql2, heat, commentId);
    }
}
