import java.util.*;
public class DashboardManager {

    public static String[] getGenreLabels(String email) {
        ArrayList<DashStat> stats = DatabaseQueries.getUserGenres(email);
        String[] labels = new String[stats.size()];

        for (int i = 0; i < stats.size(); i++) {
            labels[i] = stats.get(i).getLabel();
        }
        return labels;
    }

    public static int[] getGenreCounts(String email) {
        ArrayList<DashStat> stats = DatabaseQueries.getUserGenres(email);
        int[] counts = new int[stats.size()];

        for (int i = 0; i < stats.size(); i++) {
            counts[i] = stats.get(i).getCount();
        }

        return counts;
    }
}

