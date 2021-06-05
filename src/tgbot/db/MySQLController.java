package tgbot.db;


import tgbot.UserObjects;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class MySQLController {
    private String dbName;
    private UserObjects userObject;
    private Connection connection;
    private DatabaseContract dbContract = new DatabaseContract();
    private Statement statement;

    public MySQLController(long dbName, UserObjects userObject) {
        this.dbName = String.valueOf(dbName);
        this.userObject = userObject;
    }

    public Connection DatabaseConnect() throws IOException, SQLException {
        Properties props = new Properties();
        InputStream in = Files.newInputStream(Paths.get("database.properties"));

        try {
            props.load(in);
        } catch (Throwable var6) {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }

            throw var6;
        }

        if (in != null) {
            in.close();
        }

        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        this.connection = DriverManager.getConnection(url, username, password);
        this.createDb();
        return this.connection;
    }

    private void createDb() throws SQLException {
        this.statement = this.connection.createStatement();
        this.statement.executeUpdate(this.dbContract.createDatabase(this.dbName));
        this.statement.executeUpdate(this.dbContract.createUserTable(this.dbName));
    }

    public void setUserToDB(Connection connection) throws IOException, SQLException {
        if (!this.idExist(this.userObject.getID())) {
            this.statement.executeUpdate(this.dbContract.setDataToDB(this.userObject.getID(), this.userObject.getName(), this.dbName));
            System.out.println("user added to DB: " + this.userObject.getName());
        }

    }

    public void addMessage(long userID) throws SQLException {
        if (this.idExist(userID)) {
            this.statement.executeUpdate(this.dbContract.setMessageToDB(this.userObject.getID(), this.dbName));
            System.out.println(userID + " added one message");
        }

    }

    public boolean idExist(long userID) throws SQLException {
        ResultSet resultSet = this.statement.executeQuery(this.dbContract.getUserFromDB(userID, this.dbName));
        return resultSet.next();
    }
}
