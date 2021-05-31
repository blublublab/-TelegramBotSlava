package tgbot.db;


import tgbot.UserObjects;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;
public class MySQLController {
    private String dbName;
    private UserObjects userObject;
    private Connection connection;
    private DatabaseContract dbContract = new DatabaseContract();
    private Statement statement;

    public MySQLController(long dbName, UserObjects userObject) {
        this.dbName = String.valueOf(dbName);
        this.userObject =  userObject;
    }

    public Connection DatabaseConnect() throws IOException, SQLException {
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("database.properties"))){
            props.load(in);
        }

        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
            connection = getConnection(url, username, password);
            createDb();

        return connection;
    }

    private void createDb() throws SQLException {
        statement =  connection.createStatement();
         statement.executeUpdate(dbContract.createDatabase(dbName));
         statement.executeUpdate(dbContract.createUserTable(dbName));
    }

    public void setUserToDB( Connection connection) throws IOException, SQLException {

            if(! idExist(userObject.getID())) {
                statement.executeUpdate(dbContract.setDataToDB(userObject.getID(), userObject.getName(), dbName));
                System.out.println("user added to DB: " + userObject.getName());
            }

        }

        public void addMessage( long userID) throws SQLException {
           if(idExist(userID)){
                 statement.executeUpdate(dbContract.setMessageToDB(userObject.getID(), dbName));
               System.out.println(userID + " added one message");
           }

        }
        public boolean idExist(long userID) throws SQLException {
            ResultSet  resultSet =  statement.executeQuery(dbContract.getUserFromDB(userID , dbName));
            return resultSet.next();
        }
        }


