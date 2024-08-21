package com.autodash.AutodashBackend.Chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("java/api/chat/")
public class ChatController {
    @Autowired
    JdbcTemplate template;

    @PostMapping("getHistory")
    public List<ChatHistory> getHistory(@RequestBody Page page) {
        int limit = 10;
        return template.queryForStream("select * from chat_history order by timestamp limit ? offset ?", new ChatHistoryMapper(), limit, limit * page.page).toList();
    }

    @PostMapping("addHistory")
    public void addHistory(@RequestBody ChatHistory history) {
        history.insert(template);
    }
}

@Getter
@NoArgsConstructor
class Page {
    int page;
}