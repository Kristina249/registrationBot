package com.example.registrationBot.bot;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserHandlerConfig {

    @Bean
    public Map<UserState, UserResponseHandler> handlers(List<UserResponseHandler> allHandlers) {
        return allHandlers.stream()
            .collect(Collectors.toMap(
                UserResponseHandler::getState,   // Ключ: состояние, возвращаемое из getState()
                Function.identity()              // Значение: сам handler (как он есть)
            ));
    }
}
