package com.gjstr.bankService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * Сервис, обрабатывающий Telegram-команды и отправляющий рекомендации пользователям.
 */
@Component
public class TelegramBotService extends TelegramLongPollingBot {

    private final RecommendationService recommendationService;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    public TelegramBotService(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (message.startsWith("/recommend")) {
                String[] parts = message.split(" ");
                if (parts.length == 2) {
                    String username = parts[1];
                    String response = recommendationService.getRecommendationTextForTelegram(username);
                    sendMessage(chatId, response);
                } else {
                    sendMessage(chatId, "⚠ Используйте формат: /recommend username");
                }
            } else {
                sendMessage(chatId, "👋 Добро пожаловать! Используйте команду /recommend <username> для получения рекомендаций.");
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        try {
            SendMessage message = new SendMessage(String.valueOf(chatId), text);
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
