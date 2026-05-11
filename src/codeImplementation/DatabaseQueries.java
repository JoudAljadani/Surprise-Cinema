package codeImplementation;
import GUI.*;
import java.sql.*;
import java.util.ArrayList;


public class DatabaseQueries {

    //-------------------------------------------------------------------------------------------------------------------------------
    //USERS TABLE Queries
    //Add new user to USERS table (for SignUp)
    public static void addUser(User user) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "INSERT INTO USERS " +
                    "(NAME, EMAIL, PASSWORD, AGE, GENDER) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getAge());
            ps.setString(5, user.getGender());

            ps.executeUpdate();

            System.out.println("User added successfully");

            con.close();

        } catch (SQLException e) {
            System.out.println("User Insert error!");
            System.out.println(e.getMessage());
        }
    }

    //Check if email already exists (for SignUp)
    public static boolean emailExists(String email) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql = "SELECT * FROM USERS WHERE EMAIL = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

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

    //get user account -> check if account exist (for Login)
    public static User getUserByLogin(String email, String password) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "SELECT * FROM USERS " +
                    "WHERE EMAIL = ? AND PASSWORD = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("NAME");
                String userEmail = rs.getString("EMAIL");
                String userPassword = rs.getString("PASSWORD");
                int age = rs.getInt("AGE");
                String gender = rs.getString("GENDER");

                con.close();

                //return user object
                return new User(name, userEmail, userPassword, age, gender);
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("User login error!");
            System.out.println(e.getMessage());
        }

        return null;
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    //TICKETS TABLE Queries
    //Add new ticket to TICKETS table
    public static void addTicket(Ticket ticket) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "INSERT INTO TICKETS " +
                    "(SHOW_ID, MOVIE_NAME, MOVIE_GENRE, POSTER_URL, CINEMA_NAME, HALL, SHOW_DATE, SHOW_TIME, SEAT, USER_EMAIL, DURATION) " +
                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, ticket.getShowId());
            ps.setString(2, ticket.getMovieName());
            ps.setString(3, ticket.getMovieGenre());
            ps.setString(4, ticket.getPosterUrl());
            ps.setString(5, ticket.getCinemaName());
            ps.setString(6, ticket.getHall());
            ps.setString(7, ticket.getDate());
            ps.setString(8, ticket.getShowTime());
            ps.setString(9, ticket.getSeat());
            ps.setString(10, ticket.getUserEmail());
            ps.setString(11, ticket.getDuration());

            ps.executeUpdate();

            System.out.println("Ticket added successfully");

            con.close();

        } catch (SQLException e) {
            System.out.println("Ticket insert error!");
            System.out.println(e.getMessage());
        }
    }

    //Get Recent Ticket (for Rating and view MyTicket)
    public static Ticket getLatestTicketByEmail(String email) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "SELECT * FROM TICKETS " +
                    "WHERE USER_EMAIL = ? " +
                    "ORDER BY ID DESC LIMIT 1"; //to get the recent record

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int showId = rs.getInt("SHOW_ID");
                String movieName = rs.getString("MOVIE_NAME");
                String movieGenre = rs.getString("MOVIE_GENRE");
                String posterUrl = rs.getString("POSTER_URL");
                String cinemaName = rs.getString("CINEMA_NAME");
                String hall = rs.getString("HALL");
                String date = rs.getString("SHOW_DATE");
                String showTime = rs.getString("SHOW_TIME");
                String seat = rs.getString("SEAT");
                String userEmail = rs.getString("USER_EMAIL");
                String duration = rs.getString("DURATION");

                con.close();

                //return recent ticket object
                return new Ticket(showId, movieName, movieGenre, posterUrl, cinemaName,
                        hall, date, showTime, seat, userEmail, duration);
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("Get latest ticket error!");
            System.out.println(e.getMessage());
        }

        return null;
    }

    //Get user genre and its counts (for Dashboard)
    public static ArrayList<DashStat> getUserGenres(String email) {
        ArrayList<DashStat> stats = new ArrayList<>();
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "SELECT MOVIE_GENRE, COUNT(*) AS TOTAL " +
                    "FROM TICKETS " +
                    "WHERE USER_EMAIL = ? " +
                    "GROUP BY MOVIE_GENRE";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //get each genre and its counts
                String genre = rs.getString("MOVIE_GENRE");
                int count = rs.getInt("TOTAL");

                //add each genre to stats array list
                stats.add(new DashStat(genre, count));
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("Get genres error!");
            System.out.println(e.getMessage());
        }

        return stats;//return stats array list of all genre
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    //USER_GENRES TABLE Queries
    //Add new USER_GENRES (when signUp & edit preferences)
    public static void addUserGenre(String email, String genre) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "INSERT INTO USER_GENRES " +
                    "(USER_EMAIL, GENRE) " +
                    "VALUES (?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, genre);

            ps.executeUpdate();

            con.close();

        } catch (SQLException e) {
            System.out.println("Genre insert error!");
            System.out.println(e.getMessage());
        }
    }

    //Delete new USER_GENRES (When edit preferences)
    public static void deleteUserGenres(String email) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "DELETE FROM USER_GENRES " +
                    "WHERE USER_EMAIL = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);

            ps.executeUpdate();

            con.close();

        } catch (SQLException e) {
            System.out.println("Delete genres error!");
            System.out.println(e.getMessage());
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    //RATINGS TABLE Queries
    //Add new RATING (when signUp & edit preferences)
    public static void addRating(String email, String movieName, int rating) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "INSERT INTO RATINGS " +
                    "(USER_EMAIL, MOVIE_NAME, RATING) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, movieName);
            ps.setInt(3, rating);

            ps.executeUpdate();

            con.close();

        } catch (SQLException e) {
            System.out.println("Rating insert error!");
            System.out.println(e.getMessage());
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    //MOVIES & SHOWS TABLES Queries
    //Checking if MOVIE TABLE is empty
    public static boolean isMoviesTableEmpty() {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql = "SELECT COUNT(*) FROM MOVIES";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                con.close();
                return count == 0; //if count == 0 the shows table is empty
            }

            con.close();
        } catch (SQLException e) {
            System.out.println("Check movies table error!");
            System.out.println(e.getMessage());
        }

        return true;
    }

    //Add new MOVIE
    public static void addMovie(Movie movie) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "INSERT INTO MOVIES " +
                    "(TITLE, GENRE, DURATION, RATING, STORY, POSTER_URL) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, movie.getName());
            ps.setString(2, movie.getGenre());
            ps.setString(3, movie.getDuration());
            ps.setString(4, movie.getRating());
            ps.setString(5, movie.getStory());
            ps.setString(6, movie.getPosterUrl());

            ps.executeUpdate();

            con.close();

        } catch (SQLException e) {
            System.out.println("Add movie error!");
            System.out.println(e.getMessage());
        }
    }

    public static void prepareMoviesFromAPI() {
        if (!isMoviesTableEmpty()) {
            System.out.println("Movies already exist in database");
            return;
        }

        String[] genres = {"COMEDY", "DRAMA", "ROMANCE", "ACTION", "HORROR", "FANTASY"};

        for (String genre : genres) {
            ArrayList<Movie> movies = TMDBMovieClient.getMoviesByGenre(genre);

            for (Movie movie : movies) {
                addMovie(movie);
            }
        }

        System.out.println("Movies inserted from TMDB API");
    }

    //Checking if SHOWS TABLE is empty
    public static boolean isShowsTableEmpty() {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql = "SELECT COUNT(*) FROM SHOWS";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                con.close();
                return count == 0; //if count == 0 the shows table is empty
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("Check shows table error!");
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static void prepareShows() {
        if (!isShowsTableEmpty()) {
            System.out.println("Shows already exist in database");
            return;
        }

        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String selectSql =
                    "SELECT ID FROM MOVIES " +
                    "ORDER BY RAND() " +
                    "LIMIT 15";

            PreparedStatement selectPs = con.prepareStatement(selectSql);

            ResultSet rs = selectPs.executeQuery();

            int hallIndex = 0;

            while (rs.next()) {
                int movieId = rs.getInt("ID");

                String hall = AppManager.generateHallByIndex(hallIndex);
                String date = AppManager.generateTomorrowDate();

                for (String time : AppManager.FIXED_TIMES) {
                    String insertSql =
                            "INSERT INTO SHOWS " +
                            "(MOVIE_ID, CINEMA_NAME, HALL, SHOW_DATE, SHOW_TIME) " +
                            "VALUES (?, ?, ?, ?, ?)";

                    PreparedStatement insertPs = con.prepareStatement(insertSql);

                    insertPs.setInt(1, movieId);
                    insertPs.setString(2, AppManager.CINEMA_NAME);
                    insertPs.setString(3, hall);
                    insertPs.setString(4, date);
                    insertPs.setString(5, time);

                    insertPs.executeUpdate();
                }

                hallIndex++;
            }

            con.close();

            System.out.println("Shows created successfully");

        } catch (SQLException e) {
            System.out.println("Prepare shows error!");
            System.out.println(e.getMessage());
        }
    }


    public static Movie getRandomMovieByUserPreferences(String email) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "SELECT DISTINCT M.* FROM MOVIES M " +
                    "JOIN USER_GENRES G ON M.GENRE = G.GENRE " +
                    "JOIN SHOWS S ON M.ID = S.MOVIE_ID " +
                    "WHERE G.USER_EMAIL = ? " +
                    "AND S.ID IN ( " +
                    "   SELECT SH.ID FROM SHOWS SH " +
                    "   LEFT JOIN BOOKED_SEATS B ON SH.ID = B.SHOW_ID " +
                    "   GROUP BY SH.ID " +
                    "   HAVING COUNT(B.ID) < 15 " +
                    ") " +
                    "ORDER BY RAND() LIMIT 1";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("ID");
                String title = rs.getString("TITLE");
                String genre = rs.getString("GENRE");
                String duration = rs.getString("DURATION");
                String rating = rs.getString("RATING");
                String story = rs.getString("STORY");
                String posterUrl = rs.getString("POSTER_URL");

                con.close();

                return new Movie(id, title, genre, duration, rating, story, posterUrl);
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("Get random movie error!");
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static Show getShowByMovieAndTime(int movieId, String showTime) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "SELECT * FROM SHOWS " +
                     "WHERE MOVIE_ID = ? " +
                     "AND SHOW_TIME = ? " +
                     "AND SHOW_DATE = ? " +
                     "ORDER BY ID LIMIT 1";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, movieId);
            ps.setString(2, showTime);
            ps.setString(3, AppManager.generateTomorrowDate());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("ID");
                int mId = rs.getInt("MOVIE_ID");
                String cinemaName = rs.getString("CINEMA_NAME");
                String hall = rs.getString("HALL");
                String showDate = rs.getString("SHOW_DATE");
                String time = rs.getString("SHOW_TIME");

                con.close();

                return new Show(id, mId, cinemaName, hall, showDate, time);
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("Get show error!");
            System.out.println(e.getMessage());
        }

        return null;
    }
    //-------------------------------------------------------------------------------------------------------------------------------
    //BOOKED_SEATS TABLE Queries
    //Check if the seat to book is booked
    public static boolean isSeatBooked(int showId, String seat) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "SELECT * FROM BOOKED_SEATS " +
                    "WHERE SHOW_ID = ? AND SEAT = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, showId);
            ps.setString(2, seat);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                con.close();
                return true;
            }

            con.close();

        } catch (SQLException e) {
            System.out.println("Seat check error!");
            System.out.println(e.getMessage());
        }

        return false;
    }

    //get seats available to book randomly
    public static String generateAvailableSeat(int showId) {
        ArrayList<String> seats = new ArrayList<>();

        for (int i = 1; i <= 15; i++) {
            seats.add("A" + i);
        }

        while (!seats.isEmpty()) {
            int index = (int) (Math.random() * seats.size());

            String seat = seats.get(index);

            if (!isSeatBooked(showId, seat)) {
                return seat;
            }

            seats.remove(index);
        }

        return null;
    }

    //Add new BOOKED_SEATS
    public static void addBookedSeat(int showId, String seat, String email) {
        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "INSERT INTO BOOKED_SEATS " +
                            "(SHOW_ID, SEAT, USER_EMAIL) " +
                            "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, showId);
            ps.setString(2, seat);
            ps.setString(3, email);


            ps.executeUpdate();

            con.close();

        } catch (SQLException e) {
            System.out.println("Add booked seat error!");
            System.out.println(e.getMessage());
        }
    }

    //Thread demo method
    //Two users can try to book the same seat at the same time.
    //synchronized makes only one thread enter this method at a time.
    public static synchronized boolean tryBookSpecificSeat(int showId, String seat, String email) {
        if (isSeatBooked(showId, seat)) {
            return false;
        }

        Connection con = null;

        try {
            con = DatabaseManager.connect();

            String sql =
                    "INSERT INTO BOOKED_SEATS " +
                    "(SHOW_ID, SEAT, USER_EMAIL) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, showId);
            ps.setString(2, seat);
            ps.setString(3, email);

            ps.executeUpdate();

            con.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Thread demo booking error!");
            System.out.println(e.getMessage());
        }

        return false;
    }
}