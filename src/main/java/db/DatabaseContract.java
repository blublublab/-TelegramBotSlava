package db;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseContract {

    public static final String PRIMARY_KEY = "PRIMARY_KEY";

    public static final String USERS = "users";

    public static final String  TYPE_INT = "INTEGER";
    public static final String  TYPE_BOOL = "TINY_INT";
    public static final String  TYPE_TEXT = "VARCHAR(45)";


    public static final String USERID_COLUMN =  "user_id";
    public static final String USER_MESSAGE_COUNT_COLUMN =  "user_message_count";
    public static final String USER_FIRST_NAME_COLUMN = "user_first_name";
    public static final String USER_OF_DAY_COLUMN = "user_of_day";



   public String createDatabase(String dbName) {
        return "CREATE DATABASE IF NOT EXISTS" +  dbName;
   }

   public String createUserTable(){

       return "CREATE TABLE IF NOT EXISTS" + USERS + "(" + USERID_COLUMN + " " + TYPE_INT +  " " +  PRIMARY_KEY  + ", "
               + USER_MESSAGE_COUNT_COLUMN + " " + TYPE_INT  + ", " + USER_FIRST_NAME_COLUMN + " " + TYPE_TEXT + ", " +
               USER_OF_DAY_COLUMN + " " + TYPE_BOOL + ")";

   }


    public String setUserToDB(long userID, String userName) throws IOException, SQLException {
      return "INSERT " + USERS + "(" +
                USERID_COLUMN + ", " +
                USER_FIRST_NAME_COLUMN + ") VALUES (" + userID + ", '" + userName + "')";
    }
}