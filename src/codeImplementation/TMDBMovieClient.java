package codeImplementation;

import GUI.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TMDBMovieClient {

    //API key used to access TMDB
    private static final String API_KEY = "814ad94c655ebda174b9aabb77f4d2c8";

    //gets movies from API based on the selected genre
    public static ArrayList<Movie> getMoviesByGenre(String genreKey) {

        ArrayList<Movie> movieList = new ArrayList<>(); //list to store the movies

        try {
            //convert genre key to TMDB genre ID
            String selectedGenreId = getGenreId(genreKey);

            //build the API URL request for discovering movies by genre
            String urlText = "https://api.themoviedb.org/3/discover/movie"
                            + "?api_key=" + API_KEY
                            + "&language=en-US"
                            + "&sort_by=popularity.desc"
                            + "&include_adult=false"
                            + "&vote_count.gte=50"
                            + "&with_genres=" + selectedGenreId
                            + "&page=1";

            //read the API response as JSON text
            String jsonResponse = readApiResponse(urlText);

            //split the JSON response into separate movie sections
            String[] movies = jsonResponse.split("\\{\"adult\"");

            //count how many movies were added
            int count = 0;

            //loop through movies
            for (int i = 1; i < movies.length && count < 10; i++) {

                //add back the part removed by split
                String movieJson = "{\"adult\"" + movies[i];

                //extract movies info
                String id = extract(movieJson, "\"id\":", ",");
                String title = extract(movieJson, "\"title\":\"", "\"");
                String overview = extract(movieJson, "\"overview\":\"", "\"");
                String rating = extract(movieJson, "\"vote_average\":", ",");
                String posterPath = extract(movieJson, "\"poster_path\":\"", "\"");

                if (!title.equals("N/A")) { //only create a movie if the title exists

                    String posterUrl = ""; //default poster URL

                    //build full poster URL
                    if (!posterPath.equals("N/A") && !posterPath.equals("null")) {
                        posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
                    }

                    //Get real movie duration from TMDB API using movie ID
                    String duration = getMovieDuration(id);

                    Movie movie = new Movie(title, genreKey, duration, rating, overview, posterUrl); //create movie obj
                    movieList.add(movie); //add movie obj to list
                    count++; //increment
                }
            }

        } catch (Exception e) {

            //handling the error if fetching movies fails
            System.out.println("TMDB fetch error!");
            System.out.println(e.getMessage());
        }
        return movieList; //return the final movie list
    }

    //reads the API response and returns it as text
    private static String readApiResponse(String urlText) throws Exception {

        //convert the URL text into a URL object
        URL url = new URL(urlText);

        //open connection to the API
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //set request method to GET
        connection.setRequestMethod("GET");

        //read the API response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

        //store JSON response
        StringBuilder json = new StringBuilder();

        //to store each line read from the response
        String line;

        //read the response line by line
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        reader.close(); //close reader
        return json.toString(); //return JSON response as text
    }

    // Gets the duration of a movie from TMDB using its ID
    private static String getMovieDuration(String movieId) {

        try {

            //if movie ID does not exist
            if (movieId.equals("N/A")) {
                return "N/A";
            }

            //build the API URL request for movie details
            String urlText = "https://api.themoviedb.org/3/movie/" + movieId
                            + "?api_key=" + API_KEY;

            String jsonResponse = readApiResponse(urlText); //read movie details response
            String duration = extract(jsonResponse, "\"runtime\":", ","); //extract duration

            //return N/A if duration does not exist
            if (duration.equals("N/A") || duration.equals("null")) {
                return "N/A";
            }

            return duration + " min"; //return the duration in min

        } catch (Exception e) {
            //return N/A if duration request fail
            return "N/A";
        }
    }

    //extracts text
    private static String extract(String text, String startKey, String endKey) {
        int start = text.indexOf(startKey);  //find start key position

        //if start key is not found
        if (start == -1) {
            return "N/A";
        }

        start += startKey.length(); //move start position after the start key
        int end = text.indexOf(endKey, start);  //find end key position

        //if end key is not found
        if (end == -1) {
            return "N/A";
        }

        //return extracted value
        return text.substring(start, end)
                .replace("\\\"", "\"")
                .replace("\\n", " ");
    }

    //Converts genre key to TMDB genre ID
    private static String getGenreId(String genre) {
        switch (genre) {
            case "COMEDY":
                return "35";
            case "DRAMA":
                return "18";
            case "ROMANCE":
                return "10749";
            case "ACTION":
                return "28";
            case "HORROR":
                return "27";
            case "FANTASY":
                return "14";
            default:
                return "35";
        }
    }
}