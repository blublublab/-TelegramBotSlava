package tgbot.db;

import java.io.IOException;
import java.sql.SQLException;

import static tgbot.MyTelegramBot.CHAT_ID;

public class UsersDatabaseContract extends  DatabaseContract {


    public   String createUsersTable(){
        String usersTableCreateCmd  =  "CREATE TABLE IF NOT EXISTS \"" + CHAT_ID  + "\"." + USERS + "(" + USERID_COLUMN + " " + TYPE_INT +  " " +  PRIMARY_KEY  + ", "
                + USER_MESSAGE_COUNT_COLUMN + " " + TYPE_INT   +  " DEFAULT 0, " + USER_FIRST_NAME_COLUMN + " " + TYPE_TEXT + ", " +
                USER_OF_DAY_COLUMN + " " + TYPE_BOOL + ")";;
        return usersTableCreateCmd;
    }
    public String setUserDataToDB(long userID, String userName) throws IOException, SQLException {

        String dbInsert = "INSERT  INTO\""+ CHAT_ID +"\"." + USERS + "(" +
                USERID_COLUMN + ", " +
                USER_FIRST_NAME_COLUMN + ", " +
                USER_MESSAGE_COUNT_COLUMN + ", " +
                USER_OF_DAY_COLUMN  +
                ") VALUES (" + userID + ", '" + userName + "', 0, FALSE)";

        return dbInsert;
    }
    public String getUserFromDB(long userID) {
        String selectFromDb =  "SELECT * FROM \"" + CHAT_ID + "\"." +   USERS + " WHERE " + USERID_COLUMN +  " = " + userID;

        return selectFromDb;

    }



    public String addMessageCountToDB(long userID){
        String setMessageCount =  "UPDATE \"" + CHAT_ID + "\"." + USERS + " SET " +
                USER_MESSAGE_COUNT_COLUMN  + " = "  +
                USER_MESSAGE_COUNT_COLUMN + " +1 WHERE " + USERID_COLUMN + " = " + userID;

        return  setMessageCount;


    }


}
