import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot( new Bot() );  // регистрируем бота
        } catch (TelegramApiRequestException e) {  // печать при исключении
            e.printStackTrace();
        }
    }
     // что бот возвращает на сообщения
    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown( true );                      //возможность разметки
        sendMessage.setChatId( message.getChatId().toString() ); // ответ на сообщения
        sendMessage.setReplyToMessageId( message.getMessageId() );
        sendMessage.setText( text );
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

        // прием сообщений
    public void onUpdateReceived(Update update) {
        Model model = new Model(); // для хранения данных
        Message message = update.getMessage();  // сообщение
        // проверяем
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg( message, " какой-то текст" );
                    break;
                case "/setting":
                    sendMsg( message, " какой-то текст" );
                    break;
                default:
                    try {
                        this.sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        this.sendMsg(message, "какой то город");
                    }
            }
        }

    }
         // клавиатура под текстовой панелью
    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); // создаем клавиатуру
        sendMessage.setReplyMarkup( replyKeyboardMarkup ); // разметка
        replyKeyboardMarkup.setSelective( true ); // вывод определенного параметра
        replyKeyboardMarkup.setResizeKeyboard( true );
        replyKeyboardMarkup.setOneTimeKeyboard( false ); // скрываем клавиатуру

        List<KeyboardRow> keyboardRowList = new ArrayList<>(); // создаем кнопку
        KeyboardRow keyboardFirstRow = new KeyboardRow();
         // помещаем служебные кнопки
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));
          // добавляем строчки(все) в список
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

  // вернуть имя бота указанного при регестрации
    public String getBotUsername() {
        return " какой-то текст "; // название бота
    }
    // токен
    public String getBotToken() {
        return " какой-то текст "; //указать токен
    }
}
