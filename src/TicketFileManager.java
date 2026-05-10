import javax.swing.*;
import java.io.*;
import java.awt.Desktop;
import java.util.ArrayList;

public class TicketFileManager {

    private static String getFileName(String email) {
        return "ticket_history_" + email.replace("@", "_").replace(".", "_") + ".txt";
    }

    // Save ticket when user books a ticket
    public static void saveTicketToFile(Ticket ticket) {
        String fileName = getFileName(ticket.getUserEmail());

        try (FileWriter fw = new FileWriter(fileName, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println("----- Ticket Booking -----");
            pw.println("Movie: " + ticket.getMovieName());
            pw.println("Movie Genre: " + ticket.getMovieGenre());
            pw.println("Duration: " + ticket.getDuration());
            pw.println("Cinema: " + ticket.getCinemaName());
            pw.println("Hall: " + ticket.getHall());
            pw.println("Date: " + ticket.getDate());
            pw.println("Time: " + ticket.getShowTime());
            pw.println("Seat: " + ticket.getSeat());
            pw.println("--------------------------");
            pw.println();

        } catch (IOException e) {
            System.out.println("File writing error!");
            System.out.println(e.getMessage());
        }
    }

    // This method does NOT save the whole ticket again.
    // It only adds/updates the rating in the latest ticket record.
    public static void saveTicketToFile(Ticket ticket, int rating) {
        addRatingToLastTicket(ticket, rating);
    }

    public static void addRatingToLastTicket(Ticket ticket, int rating) {
        String fileName = getFileName(ticket.getUserEmail());
        File file = new File(fileName);

        try {
            if (!file.exists()) {
                saveTicketToFile(ticket);
            }

            ArrayList<String> lines = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;

                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }

            int endIndex = -1;
            int startIndex = -1;

            // Find the last ticket separator
            for (int i = lines.size() - 1; i >= 0; i--) {
                if (lines.get(i).equals("--------------------------")) {
                    endIndex = i;
                    break;
                }
            }

            // Find the start of that same last ticket
            if (endIndex != -1) {
                for (int i = endIndex; i >= 0; i--) {
                    if (lines.get(i).equals("----- Ticket Booking -----")) {
                        startIndex = i;
                        break;
                    }
                }
            }

            if (startIndex == -1 || endIndex == -1) {
                return;
            }

            boolean ratingFound = false;

            // If rating already exists, update it
            for (int i = startIndex; i < endIndex; i++) {
                if (lines.get(i).startsWith("Rating:")) {
                    lines.set(i, "Rating: " + rating);
                    ratingFound = true;
                    break;
                }
            }

            // If rating does not exist, add it before the separator
            if (!ratingFound) {
                lines.add(endIndex, "Rating: " + rating);
            }

            // Rewrite the file with the updated rating
            try (PrintWriter pw = new PrintWriter(new FileWriter(fileName, false))) {
                for (String line : lines) {
                    pw.println(line);
                }
            }

        } catch (IOException e) {
            System.out.println("Add rating to file error!");
            System.out.println(e.getMessage());
        }
    }

    public static String readTicketHistory(String email) {
        String fileName = getFileName(email);
        String result = "";

        try (FileReader fr = new FileReader(fileName);
             BufferedReader br = new BufferedReader(fr)) {

            String line;

            while ((line = br.readLine()) != null) {
                result += line + "\n";
            }

        } catch (FileNotFoundException e) {
            result = "No ticket history found.";
        } catch (IOException e) {
            result = "File reading error: " + e.getMessage();
        }

        return result;
    }

    public static void openTicketHistoryFile(String email) {
        String fileName = getFileName(email);
        File file = new File(fileName);

        try {
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No history file found.");
                return;
            }

            Desktop.getDesktop().open(file);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not open history file.");
        }
    }
}