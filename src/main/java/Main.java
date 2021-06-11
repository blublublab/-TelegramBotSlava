import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tgbot.MyTelegramBot;


public class Main {

   // private static final Logger log = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        try {
            // PropertyConfigurator.configure(System.getProperty("user.dir") + "/log4j.properties");
            TelegramLongPollingBot telegramLongPollingBot = new MyTelegramBot();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramLongPollingBot);


            System.out.println("Bot started");

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
};

