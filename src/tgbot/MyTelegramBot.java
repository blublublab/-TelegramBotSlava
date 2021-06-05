package tgbot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import tgbot.db.MySQLController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.*;

public class MyTelegramBot extends TelegramLongPollingBot {
    private String resultURL;
    private static final  ArrayList<String> tempIDStorage =new ArrayList<>(1000);
    private String message;
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    private static MySQLController mySQLController;

    public static final String BOT_USERNAME = "slavahoholBot";
    public static final String BOT_TOKEN =  "1613889029:AAFpVn9y3VvETCqluixVrmzmOv1N-cr1UaE";
    public static long CHAT_ID;
    public ArrayList<org.telegram.telegrambots.api.objects.User> user = new ArrayList<org.telegram.telegrambots.api.objects.User>();
    private String sendString;

    @Override
    public void onUpdateReceived(Update update) {

        CHAT_ID = update.getMessage().getChatId();


        if (update.hasMessage() && update.getMessage().hasText()) {


            String userUserName = update.getMessage().getFrom().getFirstName();
            long userID = update.getMessage().getFrom().getId();
           if( ! tempIDStorage.contains(userID)) {
               User userObject = new User(userUserName, userID);
               mySQLController = new MySQLController(CHAT_ID, userObject);
               message = update.getMessage().getText().toLowerCase();
               try {

                   mySQLController.setUserToDB(mySQLController.databaseConnect());
                   if (!mySQLController.idExist(userID)) {
                       tempIDStorage.add(String.valueOf(userID));
                   }

               } catch (IOException | SQLException e) {
                   e.printStackTrace();
               }
           }
            try {
                mySQLController.addMessage(userID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(message.contains("славик") && message.contains("кто в топе")){
                try {
                    String user = mySQLController.getTopUsers(mySQLController.databaseConnect());
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


                Callable<String> runHTTPClient = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        String inputMessage = prepareMessage(message);
                        return pictureHTTPClient.getImageLink(inputMessage);

                    }
                };

                Future<String> future = null;
                try {
                    future = executor.submit(runHTTPClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    resultURL = Objects.requireNonNull(future).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (Objects.requireNonNull(future).isDone()) {
                }
                if(resultURL != null && resultURL.contains("imgur")) {
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

    public String prepareMessage(String message) throws UnsupportedEncodingException {
        ArrayList<String> wordsArrayList = new ArrayList<>(Arrays.asList(message.split(" ")));
        wordsArrayList.remove("славик");
        wordsArrayList.remove("выгрузи");
        StringBuilder stringBuilderRequest = new StringBuilder();
        for (int i  = 0  , j = wordsArrayList.size(); i < wordsArrayList.size(); i++) {
            //TODO: Must append whitespaces according to number of elements
            stringBuilderRequest.append(wordsArrayList.get(i));

            if(wordsArrayList.size() > (j-(i+1))){
                j--;
                stringBuilderRequest.append(" ");
            }

        }
        sendString = stringBuilderRequest.toString().replaceAll("[^([A-Z])\\w+\\s]", "");
        return sendString;

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


