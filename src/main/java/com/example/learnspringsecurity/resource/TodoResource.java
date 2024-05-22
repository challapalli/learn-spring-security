package com.example.learnspringsecurity.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/todo")
    public List<Todo> todo() {
        return List.of(new Todo("in28minutes","learn aws"),
                new Todo("in28minutes", "get certified"));
    }

    @PostMapping("/user/{username}/todo")
    public void createTodo(@RequestBody Todo todo, @PathVariable String username) {
        logger.info("Create {} for {}", todo, username);
    }
}

record Todo (String userName, String description) {}
