package codeImplementation;
import javax.swing.*;
import java.awt.*;
import GUI.*;
public class Movie {

    //Variables
    private int id;
    private String name;
    private String genre;
    private String duration;
    private String rating;
    private String story;
    private String posterUrl;
    private Image posterImage;

    //movie object loaded from the database
    Movie(int id, String name, String genre, String duration, String rating, String story, String posterUrl) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.story = story;
        this.posterUrl = posterUrl;

        loadPoster(); //Convert the poster URL into an image
    }

    //movie object loaded from the API
    Movie(String name, String genre, String duration, String rating, String story, String posterUrl) {
        this.id = 0;
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.story = story;
        this.posterUrl = posterUrl;

        loadPoster(); //Convert the poster URL into an image
    }

    //Convert the poster URL into an image
    private void loadPoster() {
        try {
            //checks if the URL exists
            if (posterUrl != null && !posterUrl.isEmpty()) {
                //get the poster image from the URL
                posterImage = new ImageIcon(new java.net.URL(posterUrl)).getImage();

            } else {
                posterImage = null; //no poster URL available
            }

        } catch (Exception e) {
            posterImage = null; //URL exists but loading failed
        }
    }

    //Getters
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
