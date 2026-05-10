package codeImplementation;
import javax.swing.*;
import java.awt.*;
import GUI.*;
public class Movie {

    private int id;
    private String name;
    private String genre;
    private String duration;
    private String rating;
    private String story;
    private String posterUrl;
    private Image posterImage;

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

    public Image getPosterImage() {
        return posterImage;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getStory() {
        return story;
    }

    public String getRating() {
        return rating;
    }

    public String getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public String getName() {
        return name;
    }
}
