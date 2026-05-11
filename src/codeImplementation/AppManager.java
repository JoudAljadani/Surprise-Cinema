package codeImplementation;

import GUI.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class AppManager {

    // Custom exception inside AppManager
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

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

    // This method is synchronized because booking is a shared process.
    // If more than one thread tries to book at the same time,
    // synchronized allows only one thread to enter this method at a time.
    // This helps protect the booking process from conflicts.
    public static synchronized boolean bookTicket(String selectedTime) {

        // Print the thread name to show which thread is running the booking process.
        System.out.println("Booking started in: " + Thread.currentThread().getName());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) { // If the thread is interrupted while sleeping, this catch handles the interruption.
            System.out.println("Booking thread interrupted");
        }

        if (Appframe.currentUser == null || Appframe.currentMovie == null) {
            return false;
        }

        String movieName = Appframe.currentMovie.getName();
        String movieGenre = Appframe.currentMovie.getGenre();
        String posterUrl = Appframe.currentMovie.getPosterUrl();
        String duration = Appframe.currentMovie.getDuration();
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

    //--------------------------------------------------------
    // Sign up / sign in implementation

    public static User signUpUser(String name, String email, String password,
                                  String age, String gender) throws ValidationException {

        validateSignUpInput(name, email, password, age);

        int ageNumber = convertAge(age);

        if (DatabaseQueries.emailExists(email)) {
            throw new ValidationException("Email already exists");
        }

        User user = new User(
                name,
                email,
                password,
                ageNumber,
                gender
        );

        DatabaseQueries.addUser(user);

        return user;
    }

    public static User signInUser(String email, String password) throws ValidationException {

        validateSignInInput(email, password);

        User user = DatabaseQueries.getUserByLogin(email, password);

        if (user == null) {
            throw new ValidationException("Wrong email or password");
        }

        return user;
    }

    //--------------------------------------------------------
    // Validation with exception handling

    public static void validateSignUpInput(String name, String email,
                                           String password, String age)
            throws ValidationException {

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty()) {
            throw new ValidationException("Please fill all fields");
        }

        validateEmail(email);
        validatePassword(password);
        validateAge(age);
    }

    public static void validateSignInInput(String email, String password)
            throws ValidationException {

        if (email.isEmpty() || password.isEmpty()) {
            throw new ValidationException("Please enter email and password");
        }

        validateEmail(email);
    }

    public static void validateEmail(String email) throws ValidationException {

        if (!email.toLowerCase().endsWith("@gmail.com")) {
            throw new ValidationException("Please enter a valid email address");
        }
    }

    public static void validatePassword(String password) throws ValidationException {

        if (password.length() <= 8) {
            throw new ValidationException("Password must be at least 9 characters");
        }
    }

    public static void validateAge(String age) throws ValidationException {

        try {
            Integer.parseInt(age);
        } catch (NumberFormatException e) {
            throw new ValidationException("Age must be a number");
        }
    }

    public static int convertAge(String age) throws ValidationException {

        try {
            return Integer.parseInt(age);
        } catch (NumberFormatException e) {
            throw new ValidationException("Age must be a number");
        }
    }


    public static void prepareMovieSystem() {
        DatabaseQueries.prepareMoviesFromAPI();
        DatabaseQueries.prepareShows();
    }

    //--------------------------------------------------------
    // Rating
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

        DatabaseQueries.addRating(
                Appframe.currentUser.getEmail(),
                Appframe.currentTicket.getMovieName(),
                rating
        );

        TicketFileManager.saveTicketToFile(Appframe.currentTicket, rating);

        return true;
    }

    //--------------------------------------------------------
    // Dashboard

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

    // This method is a demo for the Thread concept.
    // It creates two different threads that try to book the same exact seat at the same time.
    // This helps show how the system handles concurrent booking.
    public static void runTwoUsersSameSeatDemo(String selectedTime) {

        // If no movie is selected, the demo cannot find the show or seat.
        if (Appframe.currentMovie == null) {
            System.out.println("No movie selected for thread demo");
            return;
        }

        // Get the show based on the selected movie and selected showtime.
        // The two demo users will try to book a seat from this same show.
        Show show = DatabaseQueries.getShowByMovieAndTime(
                Appframe.currentMovie.getId(), selectedTime
        );

        if (show == null) {
            System.out.println("No show found for thread demo");
            return;
        }

        // Generate one available seat.
        // Both demo threads will try to book this same seat to simulate a conflict.
        String sameSeat = DatabaseQueries.generateAvailableSeat(show.getId());

        if (sameSeat == null) {
            System.out.println("No available seat for thread demo");
            return;
        }

        //User1 tries to book the exact same seat using demo_user1 email.
        Runnable user1Task = () -> {
            // The method returns true if the seat was booked successfully and false if the seat was already taken.
            boolean booked = DatabaseQueries.tryBookSpecificSeat(show.getId(), sameSeat, "demo_user1@gmail.com");

            if (booked) {
                System.out.println(Thread.currentThread().getName() + " booked seat " + sameSeat);
            } else {
                System.out.println(Thread.currentThread().getName() + " could NOT book seat " + sameSeat);
            }
        };

        // User2 tries to book the exact same seat using demo_user2 email.
        Runnable user2Task = () -> {
            // The method returns true if the seat was booked successfully and false if the seat was already taken.
            boolean booked = DatabaseQueries.tryBookSpecificSeat(show.getId(), sameSeat, "demo_user2@gmail.com");

            if (booked) {
                System.out.println(Thread.currentThread().getName() + " booked seat " + sameSeat);
            } else {
                System.out.println(Thread.currentThread().getName() + " could NOT book seat " + sameSeat);
            }
        };

        Thread user1Thread = new Thread(user1Task, "User_1_Booking_Thread");
        Thread user2Thread = new Thread(user2Task, "User_2_Booking_Thread");

        // Start both threads.
        // This is where the two booking attempts begin at the same time.
        user1Thread.start();
        user2Thread.start();
    }


}