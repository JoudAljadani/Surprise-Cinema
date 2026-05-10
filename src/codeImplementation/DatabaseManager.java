package codeImplementation;
import GUI.*;
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

        try {
            String connectionURL = "jdbc:mysql://localhost:3306";
            con = DriverManager.getConnection(connectionURL, USER, PASSWORD);

            Statement st = con.createStatement();

            st.executeUpdate("CREATE DATABASE IF NOT EXISTS SurpriseCinemaDB");

            System.out.println("Database created successfully");

            con.close();

        } catch (SQLException e) {
            System.out.println("Database creation error!");
            System.out.println(e.getMessage());
        }
    }

    // Create tables
    public static void createTables() {

        Connection con = null;

        try {
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

            // TICKETS table
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS TICKETS (" +
                            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "SHOW_ID INT, " +
                            "MOVIE_NAME VARCHAR(100), " +
                            "MOVIE_GENRE VARCHAR(100), " +
                            "POSTER_URL VARCHAR(500), " +
                            "CINEMA_NAME VARCHAR(100), " +
                            "HALL VARCHAR(50), " +
                            "SHOW_DATE VARCHAR(50), " +
                            "SHOW_TIME VARCHAR(50), " +
                            "SEAT VARCHAR(20), " +
                            "USER_EMAIL VARCHAR(100), " +
                            "DURATION VARCHAR(30))"
            );
            System.out.println("TICKETS table created successfully");

            // USER_GENRES table
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS USER_GENRES (" +
                            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "USER_EMAIL VARCHAR(100), " +
                            "GENRE VARCHAR(50))"
            );
            System.out.println("USER_GENRES table created successfully");

            // RATINGS table
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS RATINGS (" +
                            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "USER_EMAIL VARCHAR(100), " +
                            "MOVIE_NAME VARCHAR(100), " +
                            "RATING INT)"
            );
            System.out.println("RATINGS table created successfully");

            // MOVIES table
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS MOVIES (" +
                            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "TITLE VARCHAR(150), " +
                            "GENRE VARCHAR(50), " +
                            "DURATION VARCHAR(30), " +
                            "RATING VARCHAR(20), " +
                            "STORY VARCHAR(1000), " +
                            "POSTER_URL VARCHAR(500))"
            );
            System.out.println("MOVIES table created successfully");

            // SHOWS table
            // UNIQUE because same hall cannot have more than one show at the same date and time
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS SHOWS (" +
                            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "MOVIE_ID INT, " +
                            "CINEMA_NAME VARCHAR(100), " +
                            "HALL VARCHAR(50), " +
                            "SHOW_DATE VARCHAR(50), " +
                            "SHOW_TIME VARCHAR(50), " +
                            "UNIQUE (HALL, SHOW_DATE, SHOW_TIME))"
            );
            System.out.println("SHOWS table created successfully");

            // BOOKED_SEATS table
            // UNIQUE because same seat cannot be booked twice for the same show
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS BOOKED_SEATS (" +
                            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "SHOW_ID INT, " +
                            "SEAT VARCHAR(20), " +
                            "USER_EMAIL VARCHAR(100), " +
                            "UNIQUE (SHOW_ID, SEAT))"
            );
            System.out.println("BOOKED_SEATS table created successfully");
            con.close();
        } catch (SQLException e) {
            System.out.println("Table creation error!");
            System.out.println(e.getMessage());
        }
    }
}