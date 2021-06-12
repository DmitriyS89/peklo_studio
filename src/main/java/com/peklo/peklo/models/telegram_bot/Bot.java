package com.peklo.peklo.models.telegram_bot;

import com.peklo.peklo.models.User.User;
import com.peklo.peklo.models.User.UserService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.Optional;

@Component
@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

//    private final TelegramTokenService telegramTokenService;
//    private final UserService userService;

    @Value("${telegramBot.token}")
    private String BotToken;

    @Value("${telegramBot.name}")
    private String BotName;

    private BotCommand botCommand = new BotCommand("/save_me", "save user");

    @Override
    public String getBotUsername() {
        return BotName;
    }

    @Override
    public String getBotToken() {
        return BotToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        sendMessage(message.getChatId().toString(), message.getText());
//        if (!message.isCommand() || !message.getText().startsWith(botCommand.getCommand())) {
//            return;
//        }
//
//        String chatId = message.getChatId().toString();
//
//        try {
//            String userToken = message.getText().substring(9);
//            Optional<Long> userId = telegramTokenService.closeToken(userToken);
//            if (userId.isPresent()) {
//                if (!userService.changeUserChatId(userId.get(), chatId)) {
//                    sendMessage(chatId, "Ой ой ошибка! Попробуйте заново");
//                } else {
//                    User user = userService.getUser(userId.get());
//                    sendMessage(chatId, String.format("Успешно! %s", user.getEmail()));
//                }
//            } else {
//                sendMessage(chatId, "Токен не найден!");
//            }
//        }catch (StringIndexOutOfBoundsException ex){
//            sendMessage(chatId, "Пример:\n\n\"/save_me токен\"\n\n");
//        }
    }

    public Boolean sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
            return true;
        } catch (TelegramApiException e) {
            return false;
        }
    }

    public Boolean sendFile(Long chatId, File file) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(String.valueOf(chatId));
        sendDocument.setDocument(new InputFile(file));
        try {
            execute(sendDocument);
            return true;
        } catch (TelegramApiException e) {
            return false;
        }
    }
}