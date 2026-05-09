import javax.swing.*;
import java.awt.*;

public class Movie {

    String name;
    String genre;
    String duration;
    String rating;
    String story;
    String posterUrl;
    Image posterImage;

    Movie(String name, String genre, String duration,
            String rating, String story, String posterUrl){
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.story = story;
        this.posterUrl = posterUrl;
        try {
            posterImage = new ImageIcon(new java.net.URL(posterUrl)).getImage();
        } catch (Exception e) {
            posterImage = null;
        }
    }
}