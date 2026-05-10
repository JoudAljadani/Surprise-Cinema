import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class AppManager {

    //Booking
    private static final Random random = new Random();

    public static final String CINEMA_NAME = "Surprise Cinema";

    public static String generateHall() {
        int hallNumber = random.nextInt(3) + 1;
        return "Hall " + hallNumber;
    }

    public static String generateDate() {
        return LocalDate.now().toString();
    }

    public static String generateSeat() {
        char row = (char) ('A' + random.nextInt(5));
        int number = random.nextInt(15) + 1;

        return row + String.valueOf(number);
    }

    public static String generateRandomTime(String range) {
        String[] parts = range.split(" - ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

        LocalTime start = LocalTime.parse(parts[0], formatter);
        LocalTime end = LocalTime.parse(parts[1], formatter);

        int startMinutes = start.getHour() * 60 + start.getMinute();

        int endMinutes = end.getHour() * 60 + end.getMinute();

        // Handle range that crosses midnight
        if (endMinutes < startMinutes) {
            endMinutes += 24 * 60;
        }
        int randomMinutes = startMinutes + random.nextInt(endMinutes - startMinutes + 1);
        randomMinutes = randomMinutes % (24 * 60);
        LocalTime randomTime = LocalTime.of(randomMinutes / 60, randomMinutes % 60);
        return randomTime.format(formatter);
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


