package tgbot.db;

import tgbot.MyTelegramBot;

public class UsersDatabaseContract implements DatabaseContract {


    @Override
    public  String createTable(int notUsableID) {

        return  "CREATE TABLE IF NOT EXISTS \"" + MyTelegramBot.getChatId() + "\"." + USERS + "(" + USERID_COLUMN + " " + TYPE_INT + "  " + PRIMARY_KEY  + ", "
                + USER_MESSAGE_COUNT_COLUMN + " " + TYPE_INT   +  " DEFAULT 0, " + USER_FIRST_NAME_COLUMN + " " + TYPE_TEXT + ", " +
                USER_OF_DAY_COLUMN + " " + TYPE_BOOL + ")";
    }

    @Override
    public String fillTable(long userID, String userName) {
        return "INSERT  INTO \""+ MyTelegramBot.getChatId() +"\"." + USERS + "(" +
                USERID_COLUMN + ", " +
                USER_FIRST_NAME_COLUMN + ", " +
                USER_MESSAGE_COUNT_COLUMN + ", " +
                USER_OF_DAY_COLUMN  +
                ") VALUES (" + userID + ", '" + userName + "', 0, FALSE)";

    }

    @Override
    public String columnsSize() {
        return "SELECT  COUNT(" + COMMAND_ID_COLUMN + ") FROM \"" + MyTelegramBot.getChatId() + "\"." + USERS;
    }


    public  String getUserFromDB(long userID) {

        return  "SELECT * FROM \"" + MyTelegramBot.getChatId() + "\"." +   USERS + " WHERE " + USERID_COLUMN +  " = " + userID;

    }



    public  String addMessageCountToDB(long userID){
        return "UPDATE \"" + MyTelegramBot.getChatId() + "\"." + USERS + " SET " +
                USER_MESSAGE_COUNT_COLUMN  + " = "  +
                USER_MESSAGE_COUNT_COLUMN + " +1 WHERE " + USERID_COLUMN + " = " + userID;

    }


}
