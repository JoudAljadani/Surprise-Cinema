import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image
    private final Image logo;//Logo image

    //Icons images
    private final Image ticketImg; //For last ticket page
    private final Image prefsImg;//For editing preferences page
    private final Image historyImg;//For history of bookings page
    private final Image rateImg;//For rating last movie page

    //Clickable buttons
    private Rectangle ticketRect, prefsRect, historyRect, rateRect;
    private Rectangle surpriseBtnRect;

    //To tracks which card was pressed
    private enum Target { NONE, TICKET, PREFS, HISTORY, RATE }
    private Target pressedTarget = Target.NONE;
    private boolean surprisePressed;

//------------------------------------------------------------

    public HomePage(Appframe app) {
        this.app = app;

        bg   = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        ticketImg  = new ImageIcon("resources/images/ticket.png").getImage();
        prefsImg   = new ImageIcon("resources/images/prefs.png").getImage();
        historyImg = new ImageIcon("resources/images/history.png").getImage();
        rateImg    = new ImageIcon("resources/images/star.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new HomePanel(), BorderLayout.CENTER);
    }

    class HomePanel extends JPanel {

        HomePanel() {

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    //Check if (Surprise Me!) button is pressed
                    if (surpriseBtnRect != null && surpriseBtnRect.contains(e.getPoint())) {
                        surprisePressed = true;
                    }

                    //Check if target card is pressed
                    pressedTarget = getTargetAt(e.getPoint());

                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    //Surprise button click (navigate to movieResult page)
                    if (surprisePressed && surpriseBtnRect != null && surpriseBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.MOVIERESULT);
                    }

                    //Navigate if mouse is released on the same card that was pressed
                    Target releasedTarget = getTargetAt(e.getPoint());
                    if (pressedTarget != Target.NONE && pressedTarget == releasedTarget){
                        if (pressedTarget == Target.TICKET)//if target pressed is ticket, navigate to myTicket page
                            app.showPage(Appframe.MY_TICKET);
                        else if (pressedTarget == Target.PREFS)//if target pressed is Edit preferences, navigate to preferences page
                            app.showPage(Appframe.PREFERENCES_GENRES);
                        else if (pressedTarget == Target.HISTORY)//if target pressed is history of booking, navigate to history page
                            app.showPage(Appframe.HISTORY);
                        else if (pressedTarget == Target.RATE)//if target pressed is Rate movie, navigate to rating page
                            app.showPage(Appframe.RATE);
                    }
                    surprisePressed = false;
                    pressedTarget = Target.NONE;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    surprisePressed = false;
                    pressedTarget = Target.NONE;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });
        }

        //Returns which card the point is inside or none if outside all cards
        private Target getTargetAt(Point p) {
            if (ticketRect != null && ticketRect.contains(p)) return Target.TICKET;
            if (prefsRect != null && prefsRect.contains(p)) return Target.PREFS;
            if (historyRect != null && historyRect.contains(p)) return Target.HISTORY;
            if (rateRect != null && rateRect.contains(p)) return Target.RATE;
            return Target.NONE;
        }

        @Override
        //Draw UI elements
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            //To make corner smoother
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //Get panel width and height
            int w = getWidth(), h = getHeight();

            //Background
            g2.drawImage(bg, 0, 0, w, h, null);

            //Logo
            int logoSize = 200;
            int logoX = (w - logoSize) / 2;
            g2.drawImage(logo, logoX, 0, logoSize, logoSize, null);

            //Title
            int titleY = logoSize + 26;
            UIComponents.drawTitle(g2, w, titleY);

            //Welcome message
            int x = w/2, y= logoSize + 54;
            UIComponents.drawCenteredText(g2, "Welcome "+ Appframe.currentUser.getName(), x, y,
                    UIComponents.FONT_SUBTITLE, UIComponents.TEXT_WHITE_SOFT2);

            //4 cards grid
            int cardW = 130, cardH = 110;
            int gapX = 16, gapY = 16;
            int totalW = cardW * 2 + gapX;
            int startX = (w - totalW) / 2;
            int startY = logoSize + 80;

            //Rectangles are recreated every repaint
            ticketRect = new Rectangle(startX, startY, cardW, cardH);
            prefsRect = new Rectangle(startX + cardW + gapX, startY, cardW, cardH);
            historyRect = new Rectangle(startX, startY + cardH + gapY, cardW, cardH);
            rateRect = new Rectangle(startX + cardW + gapX, startY + cardH + gapY, cardW, cardH);

            //Draw cards
            UIComponents.drawIconCard(g2, ticketRect, "Ticket", ticketImg, pressedTarget == Target.TICKET);
            UIComponents.drawIconCard(g2, prefsRect, "Edit Preferences", prefsImg, pressedTarget == Target.PREFS);
            UIComponents.drawIconCard(g2, historyRect, "History", historyImg, pressedTarget == Target.HISTORY);
            UIComponents.drawIconCard(g2, rateRect, "Rate Movie", rateImg, pressedTarget == Target.RATE);

            //Surprise button
            int btnW = 300, btnH = 55, btnX = (w - btnW) / 2, btnY = 575;
            surpriseBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, surpriseBtnRect, "Surprise Me!", surprisePressed);
        }
    }
}
