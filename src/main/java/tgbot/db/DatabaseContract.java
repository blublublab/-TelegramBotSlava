package tgbot.db;

import static tgbot.MyTelegramBot.CHAT_ID;
public interface  DatabaseContract {
    String PRIMARY_KEY = "PRIMARY KEY";

    String USERS = "users";
    String CHAT = "chat";

    String TYPE_INT = "INT";
    String TYPE_BOOL = "BOOLEAN";
    String TYPE_TEXT = "VARCHAR(45)";
    String TYPE_DATE = "TIMESTAMPTZ";

    String USERID_COLUMN = "user_id";
    String USER_MESSAGE_COUNT_COLUMN = "user_message_count";
    String USER_FIRST_NAME_COLUMN = "user_first_name";
    String USER_OF_DAY_COLUMN = "user_of_day";


    String COMMAND_ID = "command_id";
    String COMMAND_TEXT = "command_text";
    String COMMAND_DATE = "command_data";

    static String createSchema() {
        return "CREATE SCHEMA IF NOT EXISTS \"" + CHAT_ID + "\" AUTHORIZATION CURRENT_USER ";
    }

    String createTable();


    String fillTable(long userID, String cmd);

}
