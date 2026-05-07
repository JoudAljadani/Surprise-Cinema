import java.io.*;

    public class TicketFileManager {

        private static final String FILE_NAME = "ticket_history.txt";

        // Save ticket details into a text file
        public static void saveTicketToFile(Ticket ticket) {

            FileWriter fw = null;
            PrintWriter pw = null;

            try {
                // true means append, so old tickets will not be deleted
                fw = new FileWriter(FILE_NAME, true);
                pw = new PrintWriter(fw);

                pw.println("----- Ticket Booking -----");
                pw.println("User Email: " + ticket.getUserEmail());
                pw.println("Movie: " + ticket.getMovieName());
                pw.println("Cinema: " + ticket.getCinemaName());
                pw.println("Hall: " + ticket.getHall());
                pw.println("Date: " + ticket.getDate());
                pw.println("Time: " + ticket.getShowTime());
                pw.println("Seat: " + ticket.getSeat());
                pw.println("--------------------------");
                pw.println();

                System.out.println("Ticket saved to file successfully");

            } catch (IOException e) {
                System.out.println("File writing error!");
                System.out.println(e.getMessage());

            } finally {
                if (pw != null) {
                    pw.close();
                }
            }
        }

        // Read all ticket history from the file
        public static String readTicketHistory() {

            String result = "";
            FileReader fr = null;
            BufferedReader br = null;

            try {
                fr = new FileReader(FILE_NAME);
                br = new BufferedReader(fr);

                String line;

                while ((line = br.readLine()) != null) {
                    result = result + line + "\n";
                }

            } catch (FileNotFoundException e) {
                result = "No ticket history found.";

            } catch (IOException e) {
                result = "File reading error: " + e.getMessage();

            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    System.out.println("File closing error!");
                }
            }

            return result;
        }
    }

