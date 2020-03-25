package botAccess;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static WordGame.WordGameConnector.playWordGame;


public class Bot extends TelegramLongPollingBot {
    private String word;

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
        try {
            if ("/start".equals(s)) {
                sendMessage.setText("Hello! \nI am wordbot. For playing in Wordgame write word(max 14 letters)  \n" +
                        " For example: liberation");
                execute(sendMessage);
            } else if (s.contains("%")) {
                sendMessage.setText("working on it...");
                execute(sendMessage);
                int length = Integer.parseInt(s.substring(0, 1));
                executeSearch(chatId, word, length);
            } else if (s.length() < 15) {
                word = s;
                setInline(sendMessage);
                sendMessage.setText(s.toLowerCase());
                execute(sendMessage);
                if (s.matches("^[a-zA-Z0-9]+$")) {
                    word = s;
                    setInline(sendMessage);
                    sendMessage.setText(s.toLowerCase());
                    execute(sendMessage);
                } else {
                    sendMessage.setText("Sorry!I cannot understand you.For playing in Wordgame write word(max 14 letters) \n" +
                            " For example: liberation");
                    execute(sendMessage);
                }
            } else {
                sendMessage.setText("Your word is too long");
                execute(sendMessage);
            }
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
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            rowInline1.add(new InlineKeyboardButton().setText(String.valueOf(i)).setCallbackData(i + "%"));
        }
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        for (int i = 6; i < 11; i++) {
            rowInline2.add(new InlineKeyboardButton().setText(String.valueOf(i)).setCallbackData(String.valueOf(i) + "%"));
        }
        // Set the keyboard to the markup
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);
    }

    private void executeSearch(Long chatId, String word, int length) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        long start = System.currentTimeMillis();


        String result;
        result = playWordGame(word.toLowerCase(), length);

        String[] results = result.split("@");

        log.info("Размер " + Array.getLength(results));


        sendMessage.setText("I found " + StringUtils.countMatches(result, ";") + " words");
        execute(sendMessage);

        for (int i = 0; i < Array.getLength(results); i++) {
            log.info("Длина элемента " + i + " - " + results[i].length());
            sendMessage.setText(results[i]);
            execute(sendMessage);
        }


        long finish = System.currentTimeMillis();
        double timeConsumedMillis = finish - start;
        log.info("Time consumed: " + timeConsumedMillis / 1000 + " sec");
        sendMessage.setText("Time consumed: " + timeConsumedMillis / 1000 + " sec");
        execute(sendMessage);
    }
}