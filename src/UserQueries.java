import java.sql.*;
public class UserQueries {

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

    // Check user login
    public static boolean loginUser(String email, String password) {

        Connection con = null;
        try {
            // (1) Create connection
            con = DatabaseManager.connect();

            // (2) Create statement object
            Statement st = con.createStatement();
            // (3) Execute query
            String sql =
                    "SELECT * FROM USERS " +
                            "WHERE EMAIL = '" + email + "' " +
                            "AND PASSWORD = '" + password + "'";
            ResultSet rs = st.executeQuery(sql);
            // (4) Check result
            if (rs.next()) {
                con.close();
                return true;
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("Login error!");
            System.out.println(e.getMessage());
        }
        return false;
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
}