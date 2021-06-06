package tgbot.db;

import tgbot.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class MySQLController {
    private String dbName;
    private User userObject;
    private Connection connection;
    private DatabaseContract dbContract = new DatabaseContract();
    private Statement statement;

    public MySQLController(long dbName, User userObject) {
        this.dbName = String.valueOf(dbName);
        this.userObject = userObject;
    }

    public Connection databaseConnect() throws IOException, SQLException {
        Properties props = new Properties();
        InputStream in = Files.newInputStream(Paths.get("database.properties"));
        props.load(in);
        in.close();
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        connection = DriverManager.getConnection(url, username, password);
        createDb();
        return  connection;
    }

    private void createDb() throws SQLException {
        statement = this.connection.createStatement();
        statement.executeUpdate(dbContract.createDatabase(this.dbName));
        statement.executeUpdate(dbContract.createUserTable(this.dbName));
    }

    public void setUserToDB(Connection connection) throws IOException, SQLException {
        if (!this.idExist(this.userObject.getID())) {
            statement.executeUpdate(dbContract.setDataToDB(userObject.getID(), userObject.getName(), this.dbName));
            System.out.println("user added to DB: " + userObject.getName());
        }

    }

    public void addMessage(long userID) throws SQLException {
        if (this.idExist(userID)) {
            statement.executeUpdate(dbContract.setMessageToDB(this.userObject.getID(), this.dbName));
            System.out.println(userID + " added one message");
        }



    }

    public boolean idExist(long userID) throws SQLException {
        ResultSet resultSet = statement.executeQuery(dbContract.getUserFromDB(userID, this.dbName));
        return resultSet.next();
    }

    public String getTopUsers(Connection connection) throws SQLException {
       StringBuilder result = new StringBuilder();
       ResultSet resultSet = statement.executeQuery(dbContract.getTopUsers(dbName));
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
