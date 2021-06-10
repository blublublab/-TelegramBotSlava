package tgbot.db;

import tgbot.MyTelegramBot;
import tgbot.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

//TODO: REWORK CONNECTIONS
public class SQLController {
    private String dbName;
    private User userObject;
    private Connection connection;
    boolean alreadyConnected = false;
    private Statement statement;
    private static SQLController instance;
    private final ServerDatabaseContract serverDatabaseContract = new ServerDatabaseContract();;
    private final UsersDatabaseContract usersDatabaseContract = new UsersDatabaseContract();

    public static SQLController getInstance(long dbName, User userObject) {
        if (instance == null) {
            instance = new SQLController(dbName, userObject);
        }
        return instance;
    };
   private SQLController(long dbName, User userObject) {
        this.dbName = String.valueOf(dbName);
        this.userObject = userObject;
    }


    public Connection databaseConnect() throws IOException, SQLException {

        if (!alreadyConnected) {
            Properties props = new Properties();
            InputStream in = Files.newInputStream(Paths.get("database.properties"));
            props.load(in);
            in.close();
            String url = props.getProperty("url");
            String user = props.getProperty("user");
            String password = props.getProperty("password");
            connection = DriverManager.getConnection(url, user, password);
            alreadyConnected = true;
            statement = connection.createStatement();
        }
            statement.executeUpdate(DatabaseContract.createSchema());
            createTables();
            return connection;
        }


    private void createTables() throws SQLException {
       statement.executeUpdate( serverDatabaseContract.createTable(0));
       statement.executeUpdate( usersDatabaseContract.createTable(0));
       statement.executeUpdate( serverDatabaseContract.fillTable(0, "Хохол дня"));
    }

    public void setUserToDB(Connection connection, long userID) throws IOException, SQLException {
        if (getUserByID(userID).equals("")) {
            statement.executeUpdate(usersDatabaseContract.fillTable(userObject.getID(), userObject.getName()));
        }

    }

    public void addMessageCount(long userID) throws SQLException {
        if (!getUserByID(userID).equals("")) {
            statement.executeUpdate(usersDatabaseContract.addMessageCountToDB(userID));
            System.out.println(userID + " added one message");
        }


    }
    public ResultConstructor getTopOfTheDay(long userID, int commandID) throws SQLException {

        boolean isNewInList = false;

        ResultSet resultSet2  = statement.executeQuery(serverDatabaseContract.getTopUserOfTheDay(commandID));
        int userOfDay = 0;
        Timestamp requestTS = new Timestamp(new Date().getTime());
        String command = "";
        int remainingHours = 0;

        if (resultSet2.next()){

                Timestamp serverTS = resultSet2.getTimestamp(DatabaseContract.COMMAND_DATE_COLUMN);

                long dayLength = TimeUnit.HOURS.toMillis(24);
                if(requestTS.getTime() -  serverTS.getTime() > dayLength ) {
                    int randomChoose = (int) (Math.random()*(MyTelegramBot.getTempIDStorage().size()-1));
                    userID =  MyTelegramBot.getTempIDStorage().get(randomChoose);
                    statement.executeUpdate(serverDatabaseContract.fillServerTableByUser(commandID, userID));
                    isNewInList = true;
                }
            resultSet2 = statement.executeQuery(serverDatabaseContract.getTopUserOfTheDay(commandID));
                resultSet2.next();
            userOfDay =  resultSet2.getInt(DatabaseContract.USERID_COLUMN);
            command = resultSet2.getString(DatabaseContract.COMMAND_TEXT_COLUMN);
            remainingHours =  24 - (int) TimeUnit.MILLISECONDS.toHours(serverTS.getTime() - requestTS.getTime());
            System.out.println(userOfDay);

        }

        return new ResultConstructor(getUserByID(userOfDay), command,remainingHours, isNewInList);
        }


    public void setTopOfTheDay(int commandID, String commandName) throws SQLException {
        statement.executeUpdate(serverDatabaseContract.fillTable(commandID, commandName));
    }




    public String getTopUsers() throws SQLException {
       StringBuilder result = new StringBuilder();
       ResultSet resultSet = statement.executeQuery(serverDatabaseContract.getTopUsers());
       int i = 0;
       while(resultSet.next()){

           int messagesCount = resultSet.getInt(DatabaseContract.USER_MESSAGE_COUNT_COLUMN);
           String firstName  = resultSet.getString(DatabaseContract.USER_FIRST_NAME_COLUMN);
           result.append(i+1).append(". ").append(firstName).append(" <b>").append(messagesCount).append("</b>").append("\n");
           i++;
       }
    return result.toString();
    }

    public int getLastTopOfDayIndex() throws SQLException {
       ResultSet resultSet =   statement.executeQuery(serverDatabaseContract.columnsSize());
        int lastIndex = 0;
       if(resultSet.next()){
        lastIndex = resultSet.getInt(0);
       }
       return lastIndex;
   }

   public String getUserByID(long userID) throws SQLException {
       ResultSet resultSet = statement.executeQuery(usersDatabaseContract.getUserFromDB(userID));
       if(!resultSet.next()) {
         return "";
       }
       return resultSet.getString(DatabaseContract.USER_FIRST_NAME_COLUMN);
   }

}
