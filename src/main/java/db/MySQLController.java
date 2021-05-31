package db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;


public class MySQLController {
    private boolean isConnected = false;
    private DatabaseContract dbContract = new DatabaseContract();

    public Connection DatabaseConnect() throws IOException, SQLException {
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("database.properties"))){
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

            Connection connection = getConnection(url, username, password);

        return connection;
    }



    public void setUserToDB(long userID, String userName, Connection connection) throws IOException, SQLException {
            Statement statement = connection.createStatement();
           int a =  statement.executeUpdate(dbContract.setUserToDB(userID, userName));
            System.out.println("userAddedtoDB: " + userName);
        }
    }

