import java.sql.*;
public class DatabaseQueries {

    //-------------------------------------------------------------------------------
    //User
    // Add new user to USERS table
    public static void addUser(User user) {
        Connection con = null;

        try {
            // (1) Create connection
            con = DatabaseManager.connect();

            // (2) Create statement object
            Statement st = con.createStatement();

            // (3) Execute SQL statement
            String sql =
                    "INSERT INTO USERS " +
                            "(NAME, EMAIL, PASSWORD, AGE, GENDER) " +
                            "VALUES (" +
                            "'" + user.getName() + "', " +
                            "'" + user.getEmail() + "', " +
                            "'" + user.getPassword() + "', " +
                            user.getAge() + ", " +
                            "'" + user.getGender() + "')";

            st.executeUpdate(sql);
            System.out.println("User added successfully");
            // (4) Close connection
            con.close();

        }catch (SQLException e) {
            System.out.println("User Insert error!");
            System.out.println(e.getMessage());
        }
    }

    // Check if email already exists
    public static boolean emailExists(String email) {

        Connection con = null;
        try {
            // (1) Create connection
            con = DatabaseManager.connect();
            // (2) Create statement object
            Statement st = con.createStatement();

            // (3) Execute query
            String sql =
                    "SELECT * FROM USERS " +
                    "WHERE EMAIL = '" + email + "'";

            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                con.close();
                return true;
            }

            con.close();
        } catch (SQLException e) {
            System.out.println("Email check error!");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static User getUserByLogin(String email, String password) {

        Connection con = null;

        try {
            con = DatabaseManager.connect();
            Statement st = con.createStatement();

            String sql =
                    "SELECT * FROM USERS " +
                    "WHERE EMAIL = '" + email + "' " +
                    "AND PASSWORD = '" + password + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                String name = rs.getString("NAME");
                String userEmail = rs.getString("EMAIL");
                String userPassword = rs.getString("PASSWORD");
                int age = rs.getInt("AGE");
                String gender = rs.getString("GENDER");
                con.close();
                return new User(name, userEmail, userPassword, age, gender);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Get user login error!");
            System.out.println(e.getMessage());
        }
        return null;
    }



    //-------------------------------------------------------------------------------
    //Ticket
    // Add new ticket to TICKETS table
    public static void addTicket(Ticket ticket) {

        Connection con = null;
        try {
            // (1) Create connection
            con = DatabaseManager.connect();
            // (2) Create statement object
            Statement st = con.createStatement();
            // (3) Execute SQL statement
            String sql =
                    "INSERT INTO TICKETS " +
                            "(MOVIE_NAME, CINEMA_NAME, HALL, SHOW_DATE, SHOW_TIME, SEAT, USER_EMAIL) " +
                            "VALUES (" +
                            "'" + ticket.getMovieName() + "', " +
                            "'" + ticket.getCinemaName() + "', " +
                            "'" + ticket.getHall() + "', " +
                            "'" + ticket.getDate() + "', " +
                            "'" + ticket.getShowTime() + "', " +
                            "'" + ticket.getSeat() + "', " +
                            "'" + ticket.getUserEmail() + "')";
            st.executeUpdate(sql);
            System.out.println("Ticket added successfully");
            // (4) Close connection
            con.close();
        } catch (SQLException e) {

            System.out.println("Ticket insert error!");

            System.out.println(e.getMessage());
        }
    }

    public static void addUserGenre(String email, String genre) {
        Connection con = null;
        try {
            con = DatabaseManager.connect();
            Statement st = con.createStatement();
            String sql =
                    "INSERT INTO USER_GENRES (USER_EMAIL, GENRE) " +
                            "VALUES ('" + email + "', '" + genre + "')";

            st.executeUpdate(sql);
            con.close();

        } catch (SQLException e) {
            System.out.println("Genre insert error!");
            System.out.println(e.getMessage());
        }
    }

    public static void addUserCountry(String email, String country) {
        Connection con = null;
        try {
            con = DatabaseManager.connect();
            Statement st = con.createStatement();

            String sql =
                    "INSERT INTO USER_COUNTRIES (USER_EMAIL, COUNTRY) " +
                            "VALUES ('" + email + "', '" + country + "')";

            st.executeUpdate(sql);
            con.close();
        } catch (SQLException e) {
            System.out.println("Country insert error!");
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUserGenres(String email) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();
            Statement st = con.createStatement();

            String sql =
                    "DELETE FROM USER_GENRES " +
                            "WHERE USER_EMAIL = '" + email + "'";

            st.executeUpdate(sql);
            con.close();

        } catch (SQLException e) {
            System.out.println("Delete genres error!");
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUserCountries(String email) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();
            Statement st = con.createStatement();

            String sql =
                    "DELETE FROM USER_COUNTRIES " +
                            "WHERE USER_EMAIL = '" + email + "'";

            st.executeUpdate(sql);
            con.close();

        } catch (SQLException e) {
            System.out.println("Delete countries error!");
            System.out.println(e.getMessage());
        }
    }


}
