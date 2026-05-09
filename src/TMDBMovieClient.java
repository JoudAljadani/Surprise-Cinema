import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class TMDBMovieClient {

    private static final String API_KEY =
            "814ad94c655ebda174b9aabb77f4d2c8";

    public static Movie getRandomMovie() {
        try {
            String selectedGenre = pickSelectedGenre();
            String selectedGenreId = getGenreId(selectedGenre);
            String selectedGenreName = getGenreName(selectedGenreId);

            int randomPage = new Random().nextInt(20) + 1;

            String urlText =
                    "https://api.themoviedb.org/3/discover/movie"
                            + "?api_key=" + API_KEY
                            + "&language=en-US"
                            + "&sort_by=popularity.desc"
                            + "&include_adult=false"
                            + "&vote_count.gte=50"
                            + "&with_genres=" + selectedGenreId
                            + "&page=" + randomPage;

            URL url = new URL(urlText);

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream(),
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder json = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            reader.close();

            return extractMovie(json.toString(), selectedGenreName);

        } catch (Exception e) {
            return new Movie(
                    "Network Error",
                    "Genre unavailable",
                    "Unknown",
                    "0.0",
                    "Could not connect to TMDB server.",
                    ""
            );
        }
    }

    private static String pickSelectedGenre() {
        Set<String> genres = UserPreferences.selectedGenres;

        if (genres == null || genres.isEmpty()) {
            return "COMEDY";
        }

        ArrayList<String> list = new ArrayList<>(genres);
        return list.get(new Random().nextInt(list.size()));
    }

    private static Movie extractMovie(String json, String selectedGenreName) {
        String[] movies = json.split("\\{\"adult\"");

        if (movies.length <= 1) {
            return new Movie(
                    "No Movie Found",
                    selectedGenreName,
                    "Unknown",
                    "0.0",
                    "No movie matched your preferences.",
                    ""
            );
        }

        int index = new Random().nextInt(movies.length - 1) + 1;
        String movieJson = "{\"adult\"" + movies[index];

        String title = extract(movieJson, "\"title\":\"", "\"");
        String overview = extract(movieJson, "\"overview\":\"", "\"");
        String rating = extract(movieJson, "\"vote_average\":", ",");
        String posterPath = extract(movieJson, "\"poster_path\":\"", "\"");

        if (rating.equals("N/A")) {
            rating = "0.0";
        }

        String posterUrl = "";

        if (!posterPath.equals("N/A") && !posterPath.equals("null")) {
            posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
        }

        String duration = (80 + new Random().nextInt(70)) + " min";

        return new Movie(
                title,
                selectedGenreName,
                duration,
                rating,
                overview,
                posterUrl
        );
    }

    private static String extract(
            String text,
            String startKey,
            String endKey
    ) {
        int start = text.indexOf(startKey);

        if (start == -1) {
            return "N/A";
        }

        start += startKey.length();

        int end = text.indexOf(endKey, start);

        if (end == -1) {
            return "N/A";
        }

        return text.substring(start, end)
                .replace("\\\"", "\"")
                .replace("\\n", " ");
    }

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

    private static String getGenreName(String id) {
        switch (id) {
            case "35":
                return "Comedy";
            case "18":
                return "Drama";
            case "10749":
                return "Romance";
            case "28":
                return "Action";
            case "27":
                return "Horror";
            case "14":
                return "Fantasy";
            default:
                return "Comedy";
        }
    }
}