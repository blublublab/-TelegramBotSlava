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
    private static final ArrayList<String> tempIDStorage = new ArrayList<>(1000); //TODO: REWORK HASHMAP OR OBJECTS
    private String message;
    private static ExecutorService executeImagesThread;
    private SQLController sqlController;
    private String preparedMessage;

    public static final String BOT_USERNAME = "slavahoholBot";
    public static final String BOT_TOKEN = "1613889029:AAFpVn9y3VvETCqluixVrmzmOv1N-cr1UaE";
    public static long CHAT_ID;

    final Object lock = new Object();

    @Override
    public void onUpdateReceived(Update update) {

        CHAT_ID = update.getMessage().getChatId();
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String userName = update.getMessage().getFrom().getFirstName();
                long userID = update.getMessage().getFrom().getId();
                if (!tempIDStorage.contains(userID)) {
                    User userObject = new User(userName, userID);
                    sqlController = SQLController.getInstance(userID, userObject);
                    sqlController.databaseConnect();
                    message = update.getMessage().getText().toLowerCase();
                    sqlController.setUserToDB(sqlController.databaseConnect());

                    if (sqlController.idExist(userID)) {
                        tempIDStorage.add(String.valueOf(userID));
                    }
                }
                    sqlController.addMessageCount(userID);

                if (message.contains("славик") && message.contains("кто в топе")) {
                        String user = sqlController.getTopUsers();
                        execute(new SendMessage().setChatId(CHAT_ID).setParseMode("HTML").setText(user));
                }

                if (message.contains("славик") && message.contains("код") && message.contains("дай")) {
                        execute(new SendMessage().setChatId(CHAT_ID).setText("```  ```"));

                }
                if (message.contains("славик") && message.contains("хохол") || message.contains("хохла") && message.contains("дня")) {
                    String responseTopOfTheDay = sqlController.getTopOfTheDay(userID); //TODO: Get all IDS and then getTop
                    execute(new SendMessage().setChatId(CHAT_ID).setText("Хохол дня на сегодня:  " + responseTopOfTheDay));

                }

                if (message.contains("славик") && message.contains("выгрузи")) {
                    PictureHTTPClient pictureHTTPClient = PictureHTTPClient.getInstance();
                    executeImagesThread = Executors.newFixedThreadPool(3);

                    Runnable getPreparedMessage = () -> {
                        synchronized (lock) {

                            try {
                                message = cropToRequest(message);
                            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                                unsupportedEncodingException.printStackTrace();
                            }

                            preparedMessage = transliterate(message);
                        }
                    };
                    Runnable runHTTPClient = () -> {

                        try {
                            resultURL = pictureHTTPClient.getImageLink(preparedMessage);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    };
                    Runnable getPhoto = () -> {

                        SendPhoto sendPhotoIm = new SendPhoto();
                        sendPhotoIm.setChatId(MyTelegramBot.CHAT_ID).setPhoto(resultURL);

                        try {
                            sendPhoto(sendPhotoIm);
                        } catch (TelegramApiException telegramApiException) {
                            telegramApiException.printStackTrace();
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


            }
        } catch(IOException | SQLException | TelegramApiException e) {
            e.printStackTrace();
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