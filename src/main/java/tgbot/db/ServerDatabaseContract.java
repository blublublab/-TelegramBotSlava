package tgbot.db;


import tgbot.MyTelegramBot;


public class ServerDatabaseContract implements DatabaseContract {
    @Override
    public String createTable(int commandID) {
        return"CREATE TABLE IF NOT EXISTS \"" + MyTelegramBot.getChatId() + "\"." + CHAT + "(" +
                COMMAND_ID_COLUMN + " " + TYPE_INT +  "  " + PRIMARY_KEY + ", " +
                USERID_COLUMN + " " + TYPE_INT + ", " +
                USER_FIRST_NAME_COLUMN + " " + TYPE_TEXT + ", " +
                COMMAND_TEXT_COLUMN + " " + TYPE_TEXT + ", " +
                COMMAND_DATE_COLUMN + " " + TYPE_DATE + ")";
    }
//TODO: rewrite code
    @Override
    public String fillTable(long commandID, String cmd) {
        return "INSERT INTO \""+ MyTelegramBot.getChatId() +"\"."+ CHAT + " (" +
                COMMAND_ID_COLUMN + ", " +
                USERID_COLUMN + ", " +
                USER_FIRST_NAME_COLUMN + ", " +
                        COMMAND_TEXT_COLUMN + ", " +
                        COMMAND_DATE_COLUMN +
                ") VALUES (" + (int) commandID +  ", 0 " + ", '" + "noUser" + "', '" + cmd  + "', " +  " NOW() " + ")" +
                " ON CONFLICT (" + COMMAND_ID_COLUMN + ") DO NOTHING";
    }

    @Override
    public String columnsSize() {
        return "SELECT  COUNT(" + COMMAND_ID_COLUMN + ") FROM \"" + MyTelegramBot.getChatId() + "\"." + CHAT ;
    }

// NOW()

    public  String getTopUserOfTheDay(int commandID){

        return   "SELECT * FROM \"" + MyTelegramBot.getChatId() + "\"." +   CHAT + " WHERE " + COMMAND_ID_COLUMN + " = " + commandID ;
    };

    public  String getTopUsers(){

        return   "SELECT "  +
                USER_FIRST_NAME_COLUMN  + ", " +
                USER_MESSAGE_COUNT_COLUMN +" FROM \"" + MyTelegramBot.getChatId() + "\"." +
                USERS  +" ORDER BY " +
                USER_MESSAGE_COUNT_COLUMN + " DESC LIMIT 10" ;
    }

    public  String fillServerTableByUser(int commandID, long userID){
        return "UPDATE \""+ MyTelegramBot.getChatId()  +"\"."+ CHAT + " " +
                "SET " + COMMAND_DATE_COLUMN + " = " + "NOW()" + ", "+  USERID_COLUMN  + " = " + userID +  " WHERE " + COMMAND_ID_COLUMN + " = " + commandID;
    }


}
