package com.autodash.AutodashBackend.Chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;

@Getter
@Setter
public class ChatHistory {
    int id;
    Timestamp timestamp;
    String request;
    String response;

    public ChatHistory() {
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public void insert(JdbcTemplate template) {
        template.update("insert into chat_history (timestamp,request,response) values (?,?,?)", timestamp, request, response);
    }
}
