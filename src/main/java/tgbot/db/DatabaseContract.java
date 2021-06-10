package tgbot.db;

import tgbot.MyTelegramBot;

public  interface DatabaseContract {
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


    String COMMAND_ID_COLUMN = "command_id";
    String COMMAND_TEXT_COLUMN = "command_text";
    String COMMAND_DATE_COLUMN = "command_date";

    static String createSchema() {
        return "CREATE SCHEMA IF NOT EXISTS \"" + MyTelegramBot.getChatId() + "\" AUTHORIZATION CURRENT_USER ";
    }

    String createTable(int commandId);
    String fillTable(long userID, String cmd);
    String columnsSize();
}
