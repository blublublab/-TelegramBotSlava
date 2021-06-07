package tgbot.db;

import tgbot.User;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
//TODO: REWORK CONNECTIONS
public class SQLController {
    private String dbName;
    private User userObject;
    private Connection connection;
    boolean alreadyConnected = false;
    private Statement statement;
    private static SQLController instance;


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
            createTables();
            return connection;
        }


    private void createTables() throws SQLException {

        statement.executeUpdate(DatabaseContract.createSchema());
        statement.executeUpdate();
        statement.executeUpdate(dbContract.createChatTable());

        statement.executeUpdate(dbContract.fillChatTable("Хохол дня"));



    }

    public void setUserToDB(Connection connection) throws IOException, SQLException {
        if (!this.idExist(this.userObject.getID())) {
            statement.executeUpdate(dbContract.setDataToDB(userObject.getID(), userObject.getName()));
            System.out.println("user added to DB: " + userObject.getName());
        }

    }

    public void addMessage(long userID) throws SQLException {
        if (this.idExist(userID)) {
            statement.executeUpdate(dbContract.setMessageToDB(userID));
            System.out.println(userID + " added one message");
        }


    }
    public String getTopOfTheDay(long userID) throws SQLException {
        ResultSet resultSet  = statement.executeQuery(ServerDatabaseContract.getTopUserOfTheDay());
        String userOfDay = "";
        String testData = resultSet.getString(DatabaseContract.COMMAND_DATE); // TODO: REWORK TO SAME TYPE
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");
        String formattedString = ZonedDateTime.now().format(formatter);
                if(resultSet.next() && testData.equals(formattedString) ) { // TODO: rework if data > 24  then remake
                   userOfDay = resultSet.getString(DatabaseContract.USER_OF_DAY_COLUMN);
                } else {

                    statement.executeUpdate(dbContract.fillChatTableByUser(formattedString, userID));

                }
                return userOfDay;
    }
    public boolean idExist(long userID) throws SQLException {
        ResultSet resultSet = statement.executeQuery(dbContract.getUserFromDB(userID));
        return resultSet.next();
    }

    public String getTopUsers(Connection connection) throws SQLException {
       StringBuilder result = new StringBuilder();
       ResultSet resultSet = statement.executeQuery(dbContract.getTopUsers());
       int i = 0;
       while(resultSet.next()){

           int messagesCount = resultSet.getInt(DatabaseContract.USER_MESSAGE_COUNT_COLUMN);
           String firstName  = resultSet.getString(DatabaseContract.USER_FIRST_NAME_COLUMN);
           result.append(i+1).append(". ").append(firstName).append(" <b>").append(messagesCount).append("</b>").append("\n");
           i++;
       }
    return result.toString();
    }
}
