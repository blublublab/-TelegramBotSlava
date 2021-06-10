import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tgbot.MyTelegramBot;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramLongPollingBot telegramLongPollingBot = new MyTelegramBot();
            LongPollingBot longPollingBot = telegramLongPollingBot;
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(longPollingBot);


            System.out.println("Bot started");

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
};

