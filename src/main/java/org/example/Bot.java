package org.example;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static java.util.logging.Level.*;


public class Bot extends TelegramLongPollingBot {

    /**
     * Метод для приема сообщений.
     * @param update Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        sendMsg(update.getMessage().getChatId().toString(), message);

    }

    /**
     * Метод для настройки сообщения и его отправки.
     * @param chatId id чата
     * @param s Строка, которую необходимот отправить в качестве сообщения.
     */

    private static Logger log = LoggerFactory.getLogger(Bot.class);
    public synchronized void sendMsg(String chatId, String s) {



        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        setButtons(sendMessage);
        try {
            switch(s) {
                case "/start":
                    sendMessage.setText("С 8 марта тебя, дорогая пуза.. \n" +
                            "                            Самая лучшая и прекрасная...\n");
                    SendPhoto message = new SendPhoto();
                    message.setChatId(chatId);
                    message.setPhoto("text",new FileInputStream(new File("image.jpg")));
                    execute(message);
                    break;
                case "Пуза пуза!":
                    sendMessage.setText("Карапуза!");
                    break;
                case "Карапуза!":
                    sendMessage.setText("Пуза пуза!");
                    break;
                case "Карабу!":
                    sendMessage.setText("Бу!");
                    break;
                case "Бу!":
                    sendMessage.setText("Карабу!");
                    break;
                default:
                    sendMessage.setText("Буууууууу!");
            }
            execute(sendMessage);
            System.out.println(s);
        } catch (TelegramApiException | FileNotFoundException e) {
            log.error(String.valueOf(e));
        }
    }

    @Override
    public String getBotUsername() {
        return "WordsGeneratorBot";
    }

    @Override
    public String getBotToken() {
        return "1071585481:AAGweM7B5cfNBRXZ93bkmFl_wRmkeuQGcfY";
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton( "Карапуза!"));
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Пуза пуза!"));
        // Первая строчка клавиатуры
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton( "Карабу!"));
        // Первая строчка клавиатуры
        KeyboardRow keyboardFourthRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton( "Бу!"));
        // Вторая строчка клавиатуры


        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

}