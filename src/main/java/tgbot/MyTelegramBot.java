package tgbot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import tgbot.db.SQLController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.*;

import static tgbot.MessageChangeUtils.cropToRequest;
import static tgbot.MessageChangeUtils.transliterate;

public class MyTelegramBot extends TelegramLongPollingBot {
    private String resultURL;
    private static final ArrayList<String> tempIDStorage = new ArrayList<>(1000);
    private String message;
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    private SQLController sqlController;


    public static final String BOT_USERNAME = "slavahoholBot";
    public static final String BOT_TOKEN = "1613889029:AAFpVn9y3VvETCqluixVrmzmOv1N-cr1UaE";
    public static long CHAT_ID;


    public ArrayList<org.telegram.telegrambots.api.objects.User> user = new ArrayList<org.telegram.telegrambots.api.objects.User>();

    @Override
    public void onUpdateReceived(Update update) {

        CHAT_ID = update.getMessage().getChatId();


        if (update.hasMessage() && update.getMessage().hasText()) {


            String userUserName = update.getMessage().getFrom().getFirstName();
            long userID = update.getMessage().getFrom().getId();
            if (!tempIDStorage.contains(userID)) {
                User userObject = new User(userUserName, userID);
                sqlController = new SQLController(CHAT_ID, userObject);
                message = update.getMessage().getText().toLowerCase();
                try {

                    sqlController.setUserToDB(sqlController.databaseConnect());
                    if (!sqlController.idExist(userID)) {
                        tempIDStorage.add(String.valueOf(userID));
                    }

                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                sqlController.addMessage(userID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (message.contains("славик") && message.contains("кто в топе")) {
                try {
                    String user = sqlController.getTopUsers(sqlController.databaseConnect());
                    execute(new SendMessage().setChatId(CHAT_ID).setParseMode("HTML").setText(user));
                } catch (SQLException | TelegramApiException | IOException throwables) {
                    throwables.printStackTrace();
                }
            }

            if (message.contains("славик") && message.contains("код дай")) {
                try {
                    execute(new SendMessage().setChatId(CHAT_ID).setText("```  ```"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (message.contains("славик") && message.contains("выгрузи")) {
                PictureHTTPClient pictureHTTPClient = new PictureHTTPClient();

                 Callable<String> getPreparedMessage = () -> {
                     message = cropToRequest(message);
                     message = transliterate(message);
                     return message;
                 };
                 Callable<String> runHTTPClient = () -> pictureHTTPClient.getImageLink(message);
                Future<String> future0 = executor.submit(getPreparedMessage);
                Future<String> future1 = executor.submit(runHTTPClient);


                try {
                    future0.get();
                    resultURL = Objects.requireNonNull(future1).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                if (resultURL != null) {
                    SendPhoto sendPhotoIm = new SendPhoto();
                    sendPhotoIm.setChatId(MyTelegramBot.CHAT_ID).setPhoto(resultURL);
                    try {
                        sendPhoto(sendPhotoIm);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }






    @Override
    public String getBotUsername() {
        return  BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return  BOT_TOKEN;
    }
/*
    private void log(String username,String user_id, String txt) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + username +  ". (id = " + user_id + ") \n Text - " + txt);
    }

     */
}

