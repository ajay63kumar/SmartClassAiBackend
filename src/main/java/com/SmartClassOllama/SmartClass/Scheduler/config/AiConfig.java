package com.SmartClassOllama.SmartClass.Scheduler.config;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.model.ChatModel;

@Configuration
public class AiConfig {

    @Bean
    @Primary
    public ChatModel primaryChatModel(ChatModel openAiChatModel) {
        return openAiChatModel;
    }
}