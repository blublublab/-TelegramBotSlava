import org.json.JSONException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MyTelegramBot extends TelegramLongPollingBot {
    private String resultURL;
    public static final String BOT_USERNAME = "slavahoholBot";
    public static final String BOT_TOKEN =  "1613889029:AAFpVn9y3VvETCqluixVrmzmOv1N-cr1UaE";
    public static long CHAT_ID;


    private String sendString;
    @Override
    public void onUpdateReceived(Update update) {
         CHAT_ID = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {

            String userUserName = update.getMessage().getFrom().getFirstName();
            long userID = update.getMessage().getFrom().getId();
            String message = update.getMessage().getText().toLowerCase();
            log(userUserName , Long.toString(userID), message);
            if(message.contains("славик") && message.contains("код дай")){
                try {
                    execute(new SendMessage().setChatId(CHAT_ID).setText("```  ```"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(message.contains("славик") && message.contains("выгрузи")) {



            PictureHTTPClient pictureHTTPClient = new PictureHTTPClient();
                  // TODO:  REPAIR MULTITHREADING
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (resultURL == null) {
                                try {
                                    System.out.println(1);
                                    resultURL = pictureHTTPClient.getImageLink(prepareMessage(message));
                                    Thread.sleep(1000);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                            System.out.println(resultURL);
                        }
                    });
                thread.start();

                SendPhoto sendPhotoIm = new SendPhoto();
                    sendPhotoIm.setChatId(MyTelegramBot.CHAT_ID).setPhoto(resultURL);
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

    private void log(String username,String user_id, String txt) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + username +  ". (id = " + user_id + ") \n Text - " + txt);
    }
}


