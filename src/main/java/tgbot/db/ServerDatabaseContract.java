package tgbot.db;

import static tgbot.MyTelegramBot.CHAT_ID;

public abstract class ServerDatabaseContract extends DatabaseContract{


    public String createServerTable() {
        return "CREATE TABLE IF NOT EXISTS \"" + CHAT_ID + "\"." + CHAT + "(" +
                COMMAND_ID + " SERIAL, " +
                USERID_COLUMN + " " + TYPE_INT + ", " +
                USER_FIRST_NAME_COLUMN + " " + TYPE_TEXT + ", " +
                COMMAND_TEXT + " " + TYPE_TEXT + ", " +
                COMMAND_DATE + " " + TYPE_DATE + ")";;
    }

    public String getTopUserOfTheDay(){
        return   "SELECT * FROM \"" + CHAT_ID + "\"." +   CHAT + " WHERE " + COMMAND_ID + " = " + "0" ;
    };

    public String fillServerTable(String cmd) {
        return "INSERT  INTO\""+ CHAT_ID +"\"."+ CHAT + " " +
                USERID_COLUMN + ", " +
                USER_FIRST_NAME_COLUMN + ", " +
                COMMAND_TEXT + ", " +
                COMMAND_DATE +
                ") VALUES (" + " " + ", '" + " " + "', " + cmd  + ", " +  0 + ")";

    }

    public String getTopUsers(){

        return   "SELECT "  +
                USER_FIRST_NAME_COLUMN  + ", " +
                USER_MESSAGE_COUNT_COLUMN +" FROM \"" + CHAT_ID + "\"." +
                USERS  +" ORDER BY " +
                USER_MESSAGE_COUNT_COLUMN + " DESC LIMIT 10" ;
    }

    public String fillServerTableByUser(String date, long userID){
        return "UPDATE \""+ CHAT_ID +"\"."+ CHAT + " " +
                "SET " + COMMAND_DATE  + " = " + date + ", "+  USERID_COLUMN  + " = " + userID +  " WHERE" + COMMAND_ID + " = 0 ";
    }

}
