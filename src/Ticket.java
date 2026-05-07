public class Ticket {

    private String movieName;
    private String cinemaName;
    private String hall;
    private String date;
    private String showTime;
    private String seat;
    private String userEmail;

    // Constructor
    public Ticket(String movieName, String cinemaName, String hall,
            String date, String showTime, String seat, String userEmail) {
        this.movieName = movieName;
        this.cinemaName = cinemaName;
        this.hall = hall;
        this.date = date;
        this.showTime = showTime;
        this.seat = seat;
        this.userEmail = userEmail;
    }

    //Getters
    public String getMovieName() {
        return movieName;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getHall() {
        return hall;
    }

    public String getDate() {
        return date;
    }

    public String getShowTime() {
        return showTime;
    }

    public String getSeat() {
        return seat;
    }

    public String getUserEmail() {
        return userEmail;
    }
}