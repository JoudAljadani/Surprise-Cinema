public class Ticket {

    private int showId;
    private String movieName;
    private String movieGenre;
    private String posterUrl;
    private String cinemaName;
    private String hall;
    private String date;
    private String showTime;
    private String seat;
    private String userEmail;
    private String duration;


    // Constructor
    public Ticket(int showId, String movieName, String movieGenre,String posterUrl, String cinemaName, String hall,
            String date, String showTime, String seat, String userEmail, String duration) {
        this.showId = showId;
        this.movieName = movieName;
        this.movieGenre = movieGenre;
        this.posterUrl = posterUrl;
        this.cinemaName = cinemaName;
        this.hall = hall;
        this.date = date;
        this.showTime = showTime;
        this.seat = seat;
        this.userEmail = userEmail;
        this.duration = duration;
    }

    //Getters

    public int getShowId() {
        return showId;
    }

    public String getDuration() {
        return duration;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public String getPosterUrl() {
        return posterUrl;
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