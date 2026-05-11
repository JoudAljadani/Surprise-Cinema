package GUI;
import codeImplementation.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class chooseTimePage extends JPanel {

    //Variables
    private final Appframe app; //To navigate between pages
    private final Image bg; //Background image

    //Clickable buttons
    private Rectangle nextBtnRect;
    private boolean nextPressed = false;
    private Card selectedSlot = null;

    //Back arrow
    private Rectangle backRect;
    private boolean backPressed = false;

    //------------------------------------------------------------

    //Fixed time slots
    private final Card[] timeCards = new Card[]{
            new Card("12:00 PM", "🕛", "12PM"),
            new Card("2:00 PM", "🕑", "2PM"),
            new Card("4:00 PM", "🕓", "4PM"),
            new Card("6:00 PM", "🕕", "6PM"),
            new Card("8:00 PM", "🕗", "8PM"),
            new Card("10:00 PM", "🕙", "10PM")
    };

    public chooseTimePage(Appframe app) {
        this.app = app;

        //Background
        bg = new ImageIcon("resources/images/Background.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new TimePanel(), BorderLayout.CENTER);
    }

    class TimePanel extends JPanel {

        TimePanel() {
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    //If back arrow is pressed
                    if (backRect != null && backRect.contains(e.getPoint())) {
                        backPressed = true;
                        repaint();
                        return;
                    }

                    //Select one time card
                    for (Card c : timeCards) {
                        if (c.rect != null && c.rect.contains(e.getPoint())) {
                            selectedSlot = c;
                            repaint();
                            return;
                        }
                    }

                    //If next button is pressed
                    if (nextBtnRect != null && nextBtnRect.contains(e.getPoint())) {
                        nextPressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    //Back to movie result page
                    if (backPressed && backRect != null && backRect.contains(e.getPoint())) {
                        app.showPage(Appframe.MOVIERESULT);
                    }

                    backPressed = false;

                    //Book ticket
                    if (nextPressed && nextBtnRect != null && nextBtnRect.contains(e.getPoint())) {

                        //Force time selection
                        if (selectedSlot == null) {
                            JOptionPane.showMessageDialog(null, "Please select a showtime");
                            nextPressed = false;
                            repaint();
                            return;
                        }

                        //------------------------------------------------------------------------
                        // Demo only: two users try to book the same seat
                        //AppManager.runTwoUsersSameSeatDemo(selectedSlot.label);
                        //------------------------------------------------------------------------

                        // Store the selected showtime before starting the booking process.
                        // This value will be sent to AppManager so it knows which show the user wants to book.
                        String selectedTime = selectedSlot.label;

                        // Runnable contains the work that will run inside the new thread.
                        // I put the booking process here because booking may take time,
                        // especially because it connects to the database and saves ticket information.
                        Runnable bookingTask = () -> {

                            // Call AppManager to handle the real booking logic.
                            // This method checks the selected movie, gets the show, finds an available seat,
                            // creates the ticket, and saves the booking in the database and file.
                            boolean booked = AppManager.bookTicket(selectedTime);

                            //Return to Swing GUI thread to update the interface
                            SwingUtilities.invokeLater(() -> {

                                if (booked) { // If booking succeeded, show a success message and move to TicketSuccess page.
                                    JOptionPane.showMessageDialog(null, "Ticket booked successfully");
                                    app.showPage(Appframe.TICKET_SUCCESS);
                                } else { // If booking failed, show an error message and stay on the same page.
                                    JOptionPane.showMessageDialog(null, "Sorry, booking failed");
                                }

                                nextPressed = false;
                                repaint();
                            });
                        };

                        // Create a new thread for the booking task.
                        // The name "Booking_Thread" helps identify it when printing/debugging.
                        Thread bookingThread = new Thread(bookingTask, "Booking_Thread");
                        bookingThread.start(); // start() runs the booking task in a separate path of execution. I should not call run() directly because it would execute on the same GUI thread.
                    }

                    nextPressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    nextPressed = false;
                    backPressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            //Make corners smoother
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Get panel width and height
            int w = getWidth();
            int h = getHeight();

            //Background
            g2.drawImage(bg, 0, 0, w, h, null);

            //Back arrow
            int backSize = 36;
            int backX = 18;
            int backY = 32;

            backRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backRect, backPressed);

            //Title
            int titleY = 95;
            int x = w / 2;

            UIComponents.drawCenteredText(g2, "Pick Your showtime!", x, titleY,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            //Question
            int qY = titleY + 34;

            UIComponents.drawCenteredText(g2, "When would you like to watch this movie tomorrow?", x, qY,
                    UIComponents.FONT_SUBTITLE, UIComponents.TEXT_WHITE);

            //Time slots
            int topY = qY + 35;

            layoutCardsGrid(timeCards, w, topY, 2);

            for (Card c : timeCards) {
                boolean selected = (selectedSlot == c);
                UIComponents.drawSelectableCard(g2, c.rect, c.icon, c.label, selected);
            }

            //Next button
            int btnW = 300;
            int btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 600;

            nextBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, nextBtnRect, "Book Now!", nextPressed);
        }
    }

    //Lays out the card rectangles in a grid
    private void layoutCardsGrid(Card[] cards, int panelW, int topY, int cols) {

        int cardW = 140;
        int cardH = 82;
        int gapX = 16;
        int gapY = 14;

        int totalW = cols * cardW + (cols - 1) * gapX;
        int startX = (panelW - totalW) / 2;

        //Assign rect for each card based on row and column
        for (int i = 0; i < cards.length; i++) {
            int row = i / cols;
            int col = i % cols;

            int x = startX + col * (cardW + gapX);
            int y = topY + row * (cardH + gapY);

            cards[i].rect = new Rectangle(x, y, cardW, cardH);
        }
    }

    //Data class for each showtime
    static class Card {
        String label;
        String icon;
        String key;
        Rectangle rect;

        Card(String label, String icon, String key) {
            this.label = label;
            this.icon = icon;
            this.key = key;
        }
    }
}