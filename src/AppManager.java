import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class AppManager {

    //Booking
    public static final String CINEMA_NAME = "Surprise Cinema";

    public static final String[] FIXED_TIMES = {
            "12:00 PM",
            "2:00 PM",
            "4:00 PM",
            "6:00 PM",
            "8:00 PM",
            "10:00 PM"
    };

    public static String generateTomorrowDate() {
        return LocalDate.now().plusDays(1).toString();
    }

    public static String generateHallByIndex(int index) {
        int hallNumber = (index % 15) + 1;
        return "Hall " + hallNumber;
    }

    public static boolean bookTicket(String selectedTime) {

        if (Appframe.currentUser == null || Appframe.currentMovie == null) {
            return false;
        }

        String movieName = Appframe.currentMovie.name;
        String movieGenre = Appframe.currentMovie.genre;
        String posterUrl = Appframe.currentMovie.posterUrl;
        String duration = Appframe.currentMovie.duration;
        String userEmail = Appframe.currentUser.getEmail();

        Show show = DatabaseQueries.getShowByMovieAndTime(
                Appframe.currentMovie.getId(),
                selectedTime
        );

        if (show == null) {
            return false;
        }

        String seat = DatabaseQueries.generateAvailableSeat(show.getId());

        if (seat == null) {
            return false;
        }

        Ticket ticket = new Ticket(
                show.getId(),
                movieName,
                movieGenre,
                posterUrl,
                show.getCinemaName(),
                show.getHall(),
                show.getShowDate(),
                show.getShowTime(),
                seat,
                userEmail,
                duration
        );

        Appframe.currentTicket = ticket;

        DatabaseQueries.addBookedSeat(show.getId(), seat, userEmail);
        DatabaseQueries.addTicket(ticket);
        TicketFileManager.saveTicketToFile(ticket);

        return true;
    }
    public static boolean submitRating(int rating) {

        if (Appframe.currentUser == null) {
            return false;
        }

        if (Appframe.currentTicket == null) {
            Appframe.currentTicket = DatabaseQueries.getLatestTicketByEmail(
                    Appframe.currentUser.getEmail());
        }

        if (Appframe.currentTicket == null) {
            return false;
        }

        if (rating < 1 || rating > 5) {
            return false;
        }

        String ticketDate = Appframe.currentTicket.getDate();
        String today = java.time.LocalDate.now().toString();

        if (ticketDate.compareTo(today) < 0) {
            Appframe.currentTicket = null;
            return false;
        }

        DatabaseQueries.addRating(Appframe.currentUser.getEmail(),
                Appframe.currentTicket.getMovieName(), rating);
        TicketFileManager.saveTicketToFile(Appframe.currentTicket, rating); // Pass rating here

        return true;
    }

    //--------------------------------------------------------
    //dashboard
        public static String[] getGenreLabels(String email) {
            ArrayList<DashStat> stats = DatabaseQueries.getUserGenres(email);
            String[] labels = new String[stats.size()];

            for (int i = 0; i < stats.size(); i++) {
                labels[i] = stats.get(i).getLabel();
            }
            return labels;
        }

        public static int[] getGenreCounts(String email) {
            ArrayList<DashStat> stats = DatabaseQueries.getUserGenres(email);
            int[] counts = new int[stats.size()];

            for (int i = 0; i < stats.size(); i++) {
                counts[i] = stats.get(i).getCount();
            }

            return counts;
        }

    //--------------------------------------------------------
    //App validation

    // Check empty sign up fields
    public static boolean isSignUpEmpty(String name, String email, String password, String age) {
        return name.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty();
    }

    // Check empty sign in fields
    public static boolean isSignInEmpty(String email, String password) {
        return email.isEmpty() || password.isEmpty();
    }

    // Check email format
    public static boolean isValidEmail(String email) {
        return email.toLowerCase().endsWith("@gmail.com");
    }

    // Check password length
    public static boolean isValidPassword(String password) {
        return password.length() > 8;
    }

    // Check if age is a number
    public static boolean isValidAge(String age) {
        try {
            Integer.parseInt(age);
            return true;
        }catch(NumberFormatException e) {
            return false;
        }
    }
    // Convert age to int
    public static int convertAge(String age) {
        return Integer.parseInt(age);
    }
}


