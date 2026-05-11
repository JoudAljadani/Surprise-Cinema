package codeImplementation;
import GUI.*;

import javax.swing.*;
import java.io.*;
import java.awt.Desktop;
import java.util.ArrayList;

public class TicketFileManager {

    //generate file name using user email
    private static String getFileName(String email) {
        return "ticket_history_" + email.replace("@", "_").replace(".", "_") + ".txt";
    }

    //Save a new ticket into the history file
    public static void saveTicketToFile(Ticket ticket) {

        String fileName = getFileName(ticket.getUserEmail()); //get file name for this user

        //open file and write in append mode
        try (FileWriter fw = new FileWriter(fileName, true);
             PrintWriter pw = new PrintWriter(fw)) {

            //write ticket details
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
            //handling the error if writing fails
            System.out.println("File writing error!");
            System.out.println(e.getMessage());
        }
    }

    //save rating for latest ticket
    public static void saveTicketToFile(Ticket ticket, int rating) {
        addRatingToLastTicket(ticket, rating);
    }

    //update rating in latest ticket
    public static void addRatingToLastTicket(Ticket ticket, int rating) {
        String fileName = getFileName(ticket.getUserEmail());  //get user file name
        File file = new File(fileName); //create file object

        try {
            if (!file.exists()) { //create file if it does not exist
                saveTicketToFile(ticket);
            }

            ArrayList<String> lines = new ArrayList<>(); //store file lines

            //read file lines
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) { //read all lines into ArrayList
                    lines.add(line);
                }
            }

            //store latest ticket indexes
            int endIndex = -1;
            int startIndex = -1;

            //find the last ticket separator
            for (int i = lines.size() - 1; i >= 0; i--) {
                if (lines.get(i).equals("--------------------------")) {
                    endIndex = i;
                    break;
                }
            }

            //find the start of that same last ticket
            if (endIndex != -1) {
                for (int i = endIndex; i >= 0; i--) {
                    if (lines.get(i).equals("----- Ticket Booking -----")) {
                        startIndex = i;
                        break;
                    }
                }
            }

            //exit if ticket boundaries not found
            if (startIndex == -1 || endIndex == -1) {
                return;
            }

            //if rating already exists, update it
            boolean ratingFound = false;
            for (int i = startIndex; i < endIndex; i++) {
                if (lines.get(i).startsWith("Rating:")) {
                    lines.set(i, "Rating: " + rating);
                    ratingFound = true;
                    break;
                }
            }

            //if rating does not exist, add it before the separator
            if (!ratingFound) {
                lines.add(endIndex, "Rating: " + rating);
            }

            //rewrite the file with the updated rating
            try (PrintWriter pw = new PrintWriter(new FileWriter(fileName, false))) {
                for (String line : lines) {
                    pw.println(line);
                }
            }

        } catch (IOException e) {
            //handling the error if update fails
            System.out.println("Add rating to file error!");
            System.out.println(e.getMessage());
        }
    }

    public static String readTicketHistory(String email) {
        String fileName = getFileName(email); //generate file name
        String result = ""; //to store file contents

        //read file line by line
        try (FileReader fr = new FileReader(fileName);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                result += line + "\n";
            }

        } catch (FileNotFoundException e) {
            //handling file not found exception
            result = "No ticket history found.";

        } catch (IOException e) {
            //handling unexpected io exception
            result = "File reading error: " + e.getMessage();
        }

        return result; //return history text
    }

    public static void openTicketHistoryFile(String email) {
        String fileName = getFileName(email); //generate file name
        File file = new File(fileName);  //create file object

        try {
            if (!file.exists()) { //if file does not exist show error msg
                JOptionPane.showMessageDialog(null, "No history file found.");
                return;
            }

            Desktop.getDesktop().open(file); //open file

        } catch (IOException e) {
            //handle error if file opening fails
            JOptionPane.showMessageDialog(null, "Could not open history file.");
        }
    }
}