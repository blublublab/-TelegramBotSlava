package tgbot.db;


import static tgbot.MyTelegramBot.CHAT_ID;

public class ServerDatabaseContract implements DatabaseContract {
    @Override
    public String createTable(int commandID) {
        return "CREATE TABLE IF NOT EXISTS \"" + CHAT_ID + "\"." + CHAT + "(" +
                COMMAND_ID_COLUMN + commandID +
                USERID_COLUMN + " " + TYPE_INT + ", " +
                USER_FIRST_NAME_COLUMN + " " + TYPE_TEXT + ", " +
                COMMAND_TEXT_COLUMN + " " + TYPE_TEXT + ", " +
                COMMAND_DATE_COLUMN + " " + TYPE_DATE + ")";
    }
//TODO: rewrite code
    @Override
    public String fillTable(long notExist, String cmd) {

        return "INSERT INTO\""+ CHAT_ID +"\"."+ CHAT + " (" +
                COMMAND_ID_COLUMN +
                USERID_COLUMN + ", " +
                USER_FIRST_NAME_COLUMN + ", " +
                        COMMAND_TEXT_COLUMN + ", " +
                        COMMAND_DATE_COLUMN +
                ") VALUES (" + "0" + ", '" + "noUser" + "', '" + cmd  + "', " +  " NOW() " + ")";
    }

// NOW()

    public  String getTopUserOfTheDay(){
        return   "SELECT " + USERID_COLUMN  +" FROM \"" + CHAT_ID + "\"." +   CHAT + " WHERE " + COMMAND_ID_COLUMN + " = " + "0" ;
    };

    public  String getTopUsers(){

        return   "SELECT "  +
                USER_FIRST_NAME_COLUMN  + ", " +
                USER_MESSAGE_COUNT_COLUMN +" FROM \"" + CHAT_ID + "\"." +
                USERS  +" ORDER BY " +
                USER_MESSAGE_COUNT_COLUMN + " DESC LIMIT 10" ;
    }

    public  String fillServerTableByUser(String date, long userID){
        return "UPDATE \""+ CHAT_ID +"\"."+ CHAT + " " +
                "SET " + COMMAND_DATE_COLUMN + " = " + date + ", "+  USERID_COLUMN  + " = " + userID +  " WHERE" + COMMAND_ID_COLUMN + " = 0 ";
    }

}
