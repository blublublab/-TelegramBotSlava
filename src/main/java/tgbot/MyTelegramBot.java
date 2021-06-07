package tgbot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import tgbot.db.SQLController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static tgbot.MessageChangeUtils.cropToRequest;
import static tgbot.MessageChangeUtils.transliterate;

public class MyTelegramBot extends TelegramLongPollingBot {
    private String resultURL;
    private static final ArrayList<String> tempIDStorage = new ArrayList<>(1000);
    private String message;
    private static ExecutorService executeImagesThread;
    private SQLController sqlController;
    private String preparedMessage;

    public static final String BOT_USERNAME = "slavahoholBot";
    public static final String BOT_TOKEN = "1613889029:AAFpVn9y3VvETCqluixVrmzmOv1N-cr1UaE";
    public static long CHAT_ID;
    final Object lock = new Object();

    public ArrayList<org.telegram.telegrambots.api.objects.User> user = new ArrayList<org.telegram.telegrambots.api.objects.User>();

    @Override
    public void onUpdateReceived(Update update) {

        CHAT_ID = update.getMessage().getChatId();


        if (update.hasMessage() && update.getMessage().hasText()) {


            String userUserName = update.getMessage().getFrom().getFirstName();
            long userID = update.getMessage().getFrom().getId();
            if (!tempIDStorage.contains(userID)) {
                User userObject = new User(userUserName, userID);
                sqlController = SQLController.getInstance(userID, userObject);
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

            if (message.contains("славик") && message.contains("код") && message.contains("дай")) {
                try {
                    execute(new SendMessage().setChatId(CHAT_ID).setText("```  ```"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (message.contains("славик") && message.contains("выгрузи")) {
                PictureHTTPClient pictureHTTPClient = PictureHTTPClient.getInstance();
                executeImagesThread = Executors.newFixedThreadPool(3);

                Runnable getPreparedMessage = () -> {
                    synchronized (lock) {
                        try {
                            message = cropToRequest(message);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        preparedMessage = transliterate(message);
                    }
                };
                Runnable runHTTPClient = () -> {
                        try {

                            resultURL = pictureHTTPClient.getImageLink(preparedMessage);

                        } catch (IOException e) {
                            e.printStackTrace();

                    }
                };

                Runnable getPhoto = () -> {

                    SendPhoto sendPhotoIm = new SendPhoto();
                    sendPhotoIm.setChatId(MyTelegramBot.CHAT_ID).setPhoto(resultURL);
                    try {
                        sendPhoto(sendPhotoIm);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }


                };

                List<Runnable> runnables = Arrays.asList(
                        getPreparedMessage,
                        runHTTPClient,
                        getPhoto);

                ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 3,
                        0L, TimeUnit.SECONDS, new SynchronousQueue<>());

                for (Runnable runnable : runnables) {
                    threadPoolExecutor.submit(runnable);
                }


               CompletableFuture[] futures = runnables.stream().map(task -> CompletableFuture.runAsync(task, executeImagesThread))
                        .toArray(CompletableFuture[]::new);
                CompletableFuture.allOf(futures).join();


                // int recievedThreads = 0;
                 /*   while (recievedThreads < 3) {
                        Future<String> picture = service.take();
                        String result = picture.get();
                        service.poll(2, TimeUnit.SECONDS);
                        resultURL = result;

                        System.out.println(recievedThreads++);

                    }
                    executeImagesThread.awaitTermination(2, TimeUnit.SECONDS);

                  */


            }

            if(message.contains("славик") && message.contains("хохол")|| message.contains("хохла") &&  message.contains("дня")) {
                try {
                  String responseTopOfTheDay =   sqlController.getTopOfTheDay(userID);
                    execute(new SendMessage().setChatId(CHAT_ID).setText("Хохол дня на сегодня:  " + responseTopOfTheDay ));

                } catch (SQLException | TelegramApiException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

        @Override
        public String getBotUsername () {
            return BOT_USERNAME;
        }

        @Override
        public String getBotToken () {
            return BOT_TOKEN;
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