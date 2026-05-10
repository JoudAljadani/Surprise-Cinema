import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class TMDBMovieClient {

    private static final String API_KEY =
            "814ad94c655ebda174b9aabb77f4d2c8";

    public static ArrayList<Movie> getMoviesByGenre(String genreKey) {

        ArrayList<Movie> movieList = new ArrayList<>();

        try {
            String selectedGenreId = getGenreId(genreKey);

            String urlText =
                    "https://api.themoviedb.org/3/discover/movie"
                            + "?api_key=" + API_KEY
                            + "&language=en-US"
                            + "&sort_by=popularity.desc"
                            + "&include_adult=false"
                            + "&vote_count.gte=50"
                            + "&with_genres=" + selectedGenreId
                            + "&page=1";

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

            String[] movies = json.toString().split("\\{\"adult\"");

            int count = 0;

            for (int i = 1; i < movies.length && count < 10; i++) {

                String movieJson = "{\"adult\"" + movies[i];

                String title = extract(movieJson, "\"title\":\"", "\"");
                String overview = extract(movieJson, "\"overview\":\"", "\"");
                String rating = extract(movieJson, "\"vote_average\":", ",");
                String posterPath = extract(movieJson, "\"poster_path\":\"", "\"");

                if (!title.equals("N/A")) {

                    String posterUrl = "";

                    if (!posterPath.equals("N/A") && !posterPath.equals("null")) {
                        posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
                    }

                    String duration = (80 + new Random().nextInt(70)) + " min";

                    Movie movie = new Movie(
                            title,
                            genreKey,
                            duration,
                            rating,
                            overview,
                            posterUrl
                    );

                    movieList.add(movie);
                    count++;
                }
            }

        } catch (Exception e) {
            System.out.println("TMDB fetch error!");
            System.out.println(e.getMessage());
        }

        return movieList;
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