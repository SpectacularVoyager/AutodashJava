package com.autodash.AutodashBackend.Chat;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatHistoryMapper implements RowMapper<ChatHistory> {
    @Override
    public ChatHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChatHistory history = new ChatHistory();
        history.setId(rs.getInt("id"));
        history.setResponse(rs.getString("response"));
        history.setRequest(rs.getString("request"));
        history.setTimestamp(rs.getTimestamp("timestamp"));
        return history;
    }
}
