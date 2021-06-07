package tgbot.db;

import tgbot.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
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
       statement.executeUpdate( serverDatabaseContract.createTable());
       statement.executeUpdate( usersDatabaseContract.createTable());
    }

    public void setUserToDB(Connection connection) throws IOException, SQLException {
        if (!this.idExist(this.userObject.getID())) {
            statement.executeUpdate(usersDatabaseContract.fillTable(userObject.getID(), userObject.getName()));
        }

    }

    public void addMessageCount(long userID) throws SQLException {
        if (this.idExist(userID)) {
            statement.executeUpdate(usersDatabaseContract.addMessageCountToDB(userID));
            System.out.println(userID + " added one message");
        }


    }
    public String getTopOfTheDay(long userID) throws SQLException {
        ResultSet resultSet  = statement.executeQuery(serverDatabaseContract.getTopUserOfTheDay());
        String userOfDay = "";
        resultSet.next();
        String testData = resultSet.getString(DatabaseContract.COMMAND_DATE); // TODO: REWORK TO SAME TYPE
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");
        String formattedString = ZonedDateTime.now().format(formatter);
                if(resultSet.next() && testData.equals(formattedString) ) { // TODO: rework if data > 24  then remake
                   userOfDay = resultSet.getString(DatabaseContract.USER_OF_DAY_COLUMN);
                } else {
                    statement.executeUpdate(serverDatabaseContract.fillServerTableByUser(formattedString, userID));

                }
                return userOfDay;
    }
    public boolean idExist(long userID) throws SQLException {
        ResultSet resultSet = statement.executeQuery(usersDatabaseContract.getUserFromDB(userID));
        return resultSet.next();
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
}
