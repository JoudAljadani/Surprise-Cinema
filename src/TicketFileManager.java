import javax.swing.*;
import java.io.*;
import java.awt.Desktop;

public class TicketFileManager {

    private static String getFileName(String email) {
        return "ticket_history_" + email.replace("@", "_").replace(".", "_") + ".txt";
    }

    public static void saveTicketToFile(Ticket ticket) {
        String fileName = getFileName(ticket.getUserEmail());

        try (FileWriter fw = new FileWriter(fileName, true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println("----- Ticket Booking -----");
            pw.println("Movie: " + ticket.getMovieName());
            pw.println("Movie Genre: " + ticket.getMovieGenre());
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