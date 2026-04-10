
package com.SmartClassOllama.SmartClass.Scheduler.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ai")
public class AiController {

    private final ChatClient chatClient;

    @Autowired
    public AiController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/ask")
    public String askAI(@RequestParam String prompt) {
        return chatClient.prompt(prompt).call().content();
    }
}

