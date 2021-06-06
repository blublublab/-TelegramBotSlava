package tgbot.db;

import java.io.IOException;
import java.sql.SQLException;

import static tgbot.MyTelegramBot.CHAT_ID;

public class DatabaseContract {
    public static final String PRIMARY_KEY = "PRIMARY KEY";

    public static final String USERS = "users";

    public static final String  TYPE_INT = "INT";
    public static final String  TYPE_BOOL = "BOOLEAN";
    public static final String  TYPE_TEXT = "VARCHAR(45)";


    public static final String USERID_COLUMN =  "user_id";
    public static final String USER_MESSAGE_COUNT_COLUMN =  "user_message_count";
    public static final String USER_FIRST_NAME_COLUMN = "user_first_name";
    public static final String USER_OF_DAY_COLUMN = "user_of_day";




   public String createUserTable(){
       String test  =  "CREATE TABLE IF NOT EXISTS \"" + CHAT_ID  + "\"." + USERS + "(" + USERID_COLUMN + " " + TYPE_INT +  " " +  PRIMARY_KEY  + ", "
               + USER_MESSAGE_COUNT_COLUMN + " " + TYPE_INT   +  " DEFAULT 0, " + USER_FIRST_NAME_COLUMN + " " + TYPE_TEXT + ", " +
               USER_OF_DAY_COLUMN + " " + TYPE_BOOL + ")";;

       return test;


   }

   public String createSchema() {
       String testScheme = "CREATE SCHEMA IF NOT EXISTS \"" + CHAT_ID + "\" AUTHORIZATION CURRENT_USER ";
       return testScheme;
   }


    public String setDataToDB(long userID, String userName) throws IOException, SQLException {

        String dbInsert = "INSERT  INTO\""+ CHAT_ID +"\"." + USERS + "(" +
                USERID_COLUMN + ", " +
                USER_FIRST_NAME_COLUMN + ", " + USER_MESSAGE_COUNT_COLUMN + ", " + USER_OF_DAY_COLUMN  + ") VALUES (" + userID + ", '" + userName + "', 0, FALSE)";

        return dbInsert;
    }

    public String getUserFromDB(long userID) {
       String selectfromdb =  "SELECT * FROM \"" + CHAT_ID + "\"." +   USERS + " WHERE " + USERID_COLUMN +  " = " + userID;

       return selectfromdb;

    }
    //TODO: Rewrite to more abstract SET DATA
    public String setMessageToDB(long userID){
       String setMessageCount =  "UPDATE \"" + CHAT_ID + "\"." + USERS + " SET " +
               USER_MESSAGE_COUNT_COLUMN  + " = "  +
               USER_MESSAGE_COUNT_COLUMN + " +1 WHERE " + USERID_COLUMN + " = " + userID;

        return  setMessageCount;


    }

    public String getTopUsers(){
       String dbtest =   "SELECT "  +
               USER_FIRST_NAME_COLUMN  + ", " +
               USER_MESSAGE_COUNT_COLUMN +" FROM \"" + CHAT_ID + "\"."
               + USERS  +" ORDER BY " +
               USER_MESSAGE_COUNT_COLUMN + " DESC LIMIT 10" ;


        return dbtest;
    }




}