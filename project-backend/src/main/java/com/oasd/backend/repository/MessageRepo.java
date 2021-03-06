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
                message.setFromId(resultSet.getInt("fromId"));
                message.setStatus(resultSet.getInt("status"));
                message.setTitle(resultSet.getString("title"));
                message.setContent(resultSet.getString("content"));
                message.setSentTime(resultSet.getString("sentTime"));
                message.setUsername(resultSet.getString("username"));
                return message;
            }
        });
    }

    public void storeMessage(Message message) {
        String sql = "insert into messages values (null, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, message.getUserId(), message.getTitle(), message.getContent(), message.getStatus(),message.getSentTime(), message.getFromId(), message.getUsername());
    }

    public List<Message> findMessageOfUser(int to) {
        String sql = "select * from messages where userId = '" + to +"' order by sentTime desc";
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

    public List<Message> findMessageUnread(int id) {
        String sql = "select * from messages where fromId = '" + id +"' and title = 'Friend Request' " +
                "and status = -1 union "+
                "select * from messages where userId = '" + id + "' and title = 'Friend Request' "+
                "and status = -1 ";
        return findMessages(sql);
    }

    public List<Message> findMessageSent(int id) {
        String sql = "select * from messages where title = 'Friend Request' and fromId = '" + id +"' order by sentTime desc";
        return findMessages(sql);
    }
}
