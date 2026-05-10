public class Show {

    private int id;
    private int movieId;
    private String cinemaName;
    private String hall;
    private String showDate;
    private String showTime;

    public Show(int id, int movieId, String cinemaName,
                String hall, String showDate, String showTime) {

        this.id = id;
        this.movieId = movieId;
        this.cinemaName = cinemaName;
        this.hall = hall;
        this.showDate = showDate;
        this.showTime = showTime;
    }

    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getHall() {
        return hall;
    }

    public String getShowDate() {
        return showDate;
    }

    public String getShowTime() {
        return showTime;
    }
}
