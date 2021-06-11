package tgbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import tgbot.db.ResultConstructor;
import tgbot.db.SQLController;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static tgbot.MessageChangeUtils.cropToRequest;
import static tgbot.MessageChangeUtils.transliterate;

public class MyTelegramBot extends TelegramLongPollingBot {


    private static final ArrayList<Long> tempIDStorage = new ArrayList<>(1000); //TODO: REWORK HASHMAP OR OBJECTS
    private String message;
    private static ExecutorService executeImagesThread;
    private SQLController sqlController;
    private String preparedMessage;

    private static final String BOT_USERNAME = System.getenv("TELEGRAM_BOT_NAME");

    public static String getChatId() {
        return CHAT_ID;
    }
    private static final String BOT_TOKEN = System.getenv("TELEGRAM_TOKEN");
    private static String CHAT_ID;



    public static ArrayList<Long> getTempIDStorage() {
        return tempIDStorage;
    }

    @Override
    public void onUpdateReceived(Update update) {

        CHAT_ID = update.getMessage().getChatId().toString();
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String userName = update.getMessage().getFrom().getFirstName();
                long userID = update.getMessage().getFrom().getId();
                if (!tempIDStorage.contains(userID)) {
                    User userObject = new User(userName, userID);
                    sqlController = SQLController.getInstance(userID, userObject);
                    sqlController.databaseConnect();
                    message = update.getMessage().getText().toLowerCase();
                    sqlController.setUserToDB(sqlController.databaseConnect(), userID);

                    if (!sqlController.getUserByID(userID).equals("")) {
                        tempIDStorage.add(userID);
                    }
                }
                    sqlController.addMessageCount(userID);

                if (message.contains("славик") && message.contains("кто в топе")) {
                        String user = sqlController.getTopUsers();
                        execute( SendMessage.builder()
                                .chatId(CHAT_ID)
                                .parseMode("HTML")
                                .text(user)
                                .build());
                }

                if (message.contains("славик") && message.contains("код") && message.contains("дай")) {


                        execute(SendMessage.builder()
                                .chatId(CHAT_ID)
                                .text("```  ```")
                                .build());

                }
                if (message.contains("славик") && message.contains("дня")) {
                    if(message.contains("добавь")) {
                        message = MessageChangeUtils.cropToRequest(message);
                        sqlController.setTopOfTheDay(sqlController.getLastTopOfDayIndex()+1, message);
                    }
                    ResultConstructor topOfTheDay = sqlController.getTopOfTheDay(userID, 0);
                    String messageConcat;

                    if(!topOfTheDay.isNewToList()) {
                         messageConcat = ". До следующего выбора " + + topOfTheDay.getTimeToNext() +  " часа";
                    } else{
                        messageConcat = "";
                    }
                        execute(SendMessage.builder()
                                .chatId(CHAT_ID)
                                .text(topOfTheDay.getCommand() + " на сегодня  : " + topOfTheDay.getUser() + messageConcat)
                                .build());

                }

                if (message.contains("славик") && message.contains("выгрузи")) {
                    PictureHTTPClient pictureHTTPClient = new PictureHTTPClient();
                    final InputStream[] linkToImage = new InputStream[1];
                    CompletableFuture<InputFile> future =  CompletableFuture.supplyAsync(() -> {
                        try {
                            message = cropToRequest(message);
                        } catch (UnsupportedEncodingException unsupportedEncodingException) {
                            unsupportedEncodingException.printStackTrace();
                        }
                        preparedMessage = transliterate(message);
                        System.out.println("image prepared");
                        return preparedMessage;
                    })
                            .thenCompose(result -> CompletableFuture.supplyAsync(() -> {
                                String resultURL = "";
                                try {
                                   resultURL = pictureHTTPClient.getImageLink(result);
                                    System.out.println("got Image link");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return resultURL;
                            }))
                            .thenCompose(resultURL -> CompletableFuture.supplyAsync(() ->{
                                InputFile photo = null;
                                URL url = null;
                                try {
                                    url = new URL(resultURL);
                                    linkToImage[0] = new BufferedInputStream(url.openStream());
                                    photo = new InputFile().setMedia(linkToImage[0], "image");
                                    System.out.println("image got set");
                                } catch (Exception e ) {
                                    e.printStackTrace();
                                }
                                return photo;
                            }));


                    InputFile photo = future.get();
                    if(photo!= null) {
                        execute(SendPhoto.builder().photo(photo).chatId(getChatId()).build());
                        linkToImage[0].close();


                    }

                }


            }
        } catch(Exception e) {
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