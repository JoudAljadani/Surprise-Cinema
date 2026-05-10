import javax.swing.*;
import java.awt.*;

public class Movie {

    private int id;
    String name;
    String genre;
    String duration;
    String rating;
    String story;
    String posterUrl;
    Image posterImage;

    Movie(int id, String name, String genre, String duration,
          String rating, String story, String posterUrl) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.story = story;
        this.posterUrl = posterUrl;

        loadPoster();
    }

    Movie(String name, String genre, String duration,
          String rating, String story, String posterUrl) {

        this.id = 0;
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.story = story;
        this.posterUrl = posterUrl;

        loadPoster();
    }

    private void loadPoster() {
        try {
            if (posterUrl != null && !posterUrl.isEmpty()) {
                posterImage = new ImageIcon(new java.net.URL(posterUrl)).getImage();
            } else {
                posterImage = null;
            }
        } catch (Exception e) {
            posterImage = null;
        }
    }

    public int getId() {
        return id;
    }
}
