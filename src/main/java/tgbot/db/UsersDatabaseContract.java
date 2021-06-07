package tgbot.db;

import static tgbot.MyTelegramBot.CHAT_ID;

public class UsersDatabaseContract implements DatabaseContract {

    @Override
    public  String createTable() {
        return   "CREATE TABLE IF NOT EXISTS \"" + CHAT_ID  + "\"." + USERS + "(" + USERID_COLUMN + " " + TYPE_INT +  " " +  PRIMARY_KEY  + ", "
                + USER_MESSAGE_COUNT_COLUMN + " " + TYPE_INT   +  " DEFAULT 0, " + USER_FIRST_NAME_COLUMN + " " + TYPE_TEXT + ", " +
                USER_OF_DAY_COLUMN + " " + TYPE_BOOL + ")";
    }

    @Override
    public String fillTable(long userID, String userName) {
        return "INSERT  INTO\""+ CHAT_ID +"\"." + USERS + "(" +
                USERID_COLUMN + ", " +
                USER_FIRST_NAME_COLUMN + ", " +
                USER_MESSAGE_COUNT_COLUMN + ", " +
                USER_OF_DAY_COLUMN  +
                ") VALUES (" + userID + ", '" + userName + "', 0, FALSE)";

    }



    public  String getUserFromDB(long userID) {

        return  "SELECT * FROM \"" + CHAT_ID + "\"." +   USERS + " WHERE " + USERID_COLUMN +  " = " + userID;

    }



    public  String addMessageCountToDB(long userID){
        return "UPDATE \"" + CHAT_ID + "\"." + USERS + " SET " +
                USER_MESSAGE_COUNT_COLUMN  + " = "  +
                USER_MESSAGE_COUNT_COLUMN + " +1 WHERE " + USERID_COLUMN + " = " + userID;

    }


}
