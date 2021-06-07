package tgbot.db;

import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;

import java.io.IOException;
import java.sql.SQLException;

import static tgbot.MyTelegramBot.CHAT_ID;

public  abstract  class DatabaseContract {
    public static final String PRIMARY_KEY = "PRIMARY KEY";

    public static final String USERS = "users";
    public static final String CHAT = "chat";

    public static final String  TYPE_INT = "INT";
    public static final String  TYPE_BOOL = "BOOLEAN";
    public static final String  TYPE_TEXT = "VARCHAR(45)";
    public static final String  TYPE_DATE = "TIMESTAMPTZ";

    public static final String USERID_COLUMN =  "user_id";
    public static final String USER_MESSAGE_COUNT_COLUMN =  "user_message_count";
    public static final String USER_FIRST_NAME_COLUMN = "user_first_name";
    public static final String USER_OF_DAY_COLUMN = "user_of_day";


    public static final  String COMMAND_ID = "command_id";
    public static final  String  COMMAND_TEXT = "command_text";
    public static final  String COMMAND_DATE = "command_data";

    public static String createSchema() {
        String testScheme = "CREATE SCHEMA IF NOT EXISTS \"" + CHAT_ID + "\" AUTHORIZATION CURRENT_USER ";
        return testScheme;
    }








    //TODO: Rewrite to more abstract SET DATA






}