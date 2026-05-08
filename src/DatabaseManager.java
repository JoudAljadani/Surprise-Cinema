import java.sql.*;

public class DatabaseManager {

    // Database information
    public static final String URL = "jdbc:mysql://localhost:3306/SurpriseCinemaDB";
    public static final String USER = "root";
    public static final String PASSWORD = "JanaBajaba038";

    // Create connection
    public static Connection connect() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection error!");
            System.out.println(e.getMessage());
        }
        return con;
    }


    // Create database
    public static void createDatabase() {

        Connection con = null;

        try{

            String ConnectionURL = "jdbc:mysql://localhost:3306";
            con = DriverManager.getConnection(ConnectionURL, USER, PASSWORD);
            Statement st = con.createStatement();

            st.executeUpdate("CREATE DATABASE IF NOT EXISTS SurpriseCinemaDB");
            System.out.println("Database created successfully");
            con.close();

        }catch(SQLException e){
            System.out.println("Database creation error!");
            System.out.println(e.getMessage());
        }
    }

    // Create tables
    public static void createTables() {

        Connection con = null;
        try{
            con = connect();
            Statement st = con.createStatement();

            // USERS table
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS USERS (" +
                            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "NAME VARCHAR(50), " +
                            "EMAIL VARCHAR(100), " +
                            "PASSWORD VARCHAR(30), " +
                            "AGE INT, " +
                            "GENDER VARCHAR(20))"
            );
            System.out.println("USERS table created successfully");

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS TICKETS (" +
                            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "MOVIE_NAME VARCHAR(100), " +
                            "MOVIE_GENRE VARCHAR(100), " +
                            "MOVIE_COUNTRY VARCHAR(100), " +
                            "CINEMA_NAME VARCHAR(100), " +
                            "HALL VARCHAR(50), " +
                            "SHOW_DATE VARCHAR(50), " +
                            "SHOW_TIME VARCHAR(50), " +
                            "SEAT VARCHAR(20), " +
                            "USER_EMAIL VARCHAR(100))"
            );
            System.out.println("TICKETS table created successfully");

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS USER_GENRES (" +
                            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "USER_EMAIL VARCHAR(100), " +
                            "GENRE VARCHAR(50))"
            );
            System.out.println("USER_GENRES table created successfully");

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS USER_COUNTRIES (" +
                            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "USER_EMAIL VARCHAR(100), " +
                            "COUNTRY VARCHAR(50))"
            );
            System.out.println("USER_COUNTRIES table created successfully");
            con.close();
        }catch (SQLException e) {
            System.out.println("Table creation error!");
            System.out.println(e.getMessage());
        }
    }
}

