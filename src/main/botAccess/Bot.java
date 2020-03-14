package botAccess;


import WordGame.WordGameConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static WordGame.WordGameConnector.playWordGame;


public class Bot extends TelegramLongPollingBot {

    private boolean enableGame;
    private String word;

    public Bot() {
    }


    /**
     * Метод для приема сообщений.
     *
     * @param update Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            sendMsg(update.getMessage().getChatId(), update.getMessage().getText());
        } else {
            sendMsg(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getData());
        }
    }

    /**
     * Метод для настройки сообщения и его отправки.
     *
     * @param chatId id чата
     * @param s Строка, которую необходимот отправить в качестве сообщения.
     */

    private static Logger log = LoggerFactory.getLogger(Bot.class);

    private void sendMsg(Long chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);

        //   setButtons(sendMessage);
        switch (s) {
            case "/start":
                sendMessage.setText("Hello! \nI am wordbot. To play wordgame write length for generated words and word itself in one message \n" +
                        " For example: 5 apple");
                break;
            case "I would like to play wordgame":
                sendMessage.setText("Alright! ");
                break;
            default:
                int length = Integer.parseInt(s.substring(0, 1));
                String word = s.substring(2);

                String text = playWordGame(word.toLowerCase(), length);

                if (text.length() <= 400) {
                    log.info(text);
                    sendMessage.setText(text);
                } else {
                    log.debug("lenght " + text.length());
                    int numberOftimesWeNeedDivideText = text.length() / 400;
                    log.debug("numberOftimesWeNeedDivideText " + numberOftimesWeNeedDivideText);
                    int m = text.length() / numberOftimesWeNeedDivideText;
                    log.debug("int m " + m);
                    sendMessage.setText(text.substring(0, m));
                    sendMessage.setText(text.substring(m));
                }
                break;
        }
        try {
            execute(sendMessage);
            log.info(s);
        } catch (TelegramApiException e) {
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

    private void setButtons(SendMessage sendMessage) {
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
        keyboardFirstRow.add(new KeyboardButton("I would like to play wordgame"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    private void setInline(SendMessage sendMessage) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Update message text").setCallbackData("16"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);
    }
}