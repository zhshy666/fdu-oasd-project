package com.oasd.backend.repository;

import com.oasd.backend.domain.Message;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MessageRepo {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private List<Message> findMessages(String sql){
        return jdbcTemplate.query(sql, new RowMapper<Message>(){
            Message message = null;
            @Override
            public Message mapRow(ResultSet resultSet, int i) throws SQLException {
                message = new Message();
                message.setMessageId(resultSet.getInt("messageId"));
                message.setUserId(resultSet.getInt("userId"));
                message.setFrom(resultSet.getInt("from"));
                message.setStatus(resultSet.getInt("status"));
                message.setTitle(resultSet.getString("title"));
                message.setContent(resultSet.getString("content"));
                message.setSentTime(resultSet.getString("sentTime"));
                return message;
            }
        });
    }

    public void storeMessage(Message message) {
        String sql = "insert into messages values (null, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, message.getUserId(), message.getTitle(), message.getContent(), message.getStatus(),message.getSentTime(), message.getFrom());
    }

    public List<Message> findMessageOfUser(int to) {
        String sql = "select * from messages where userId = '" + to +"'";
        return findMessages(sql);
    }

    public void acceptOrRejectMessage(int i, int messageId) {
        String sql = "update messages set " +
                "status = ? where messageId = ?";
        jdbcTemplate.update(sql, i, messageId);
        System.out.println("Update message successfully");
    }

    public void markMessageAsRead(int imageId) {
        String sql = "update messages set " +
                "status = ? where messageId = ?";
        jdbcTemplate.update(sql, 1, imageId);
    }
}
