import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicketSuccess extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image

    //Clickable button
    private Rectangle backBtnRect;
    private boolean backPressed = false;

//------------------------------------------------------------

    public TicketSuccess(Appframe app) {
        this.app = app;

        //Background
        bg = new ImageIcon("resources/images/Background.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new TicketPanel(), BorderLayout.CENTER);
    }

    class TicketPanel extends JPanel {

        TicketPanel() {

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    //if back to home is pressed
                    if (backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        backPressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //if back, navigate to home page
                    if (backPressed && backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
                    }
                    backPressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backPressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {//this is to draw UI elements
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            //Make corner smoother
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Get panel width and height
            int w = getWidth(), h = getHeight();

            g2.drawImage(bg, 0, 0, w, h, null);//background

            //Title
            int titleY = 95, x = w / 2;
            UIComponents.drawCenteredText(g2,"Your Ticket is Ready!", x, titleY,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            //Ticket box
            int boxW = 260, boxH = 300, boxX = (w - boxW) / 2, boxY = titleY + 35;
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 22, 22);


            //Text in the ticket box
            Ticket ticket = Appframe.currentTicket;

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 14));

            if (ticket == null) {
                UIComponents.drawCenteredText(g2, "No ticket found", x,
                        boxY + boxH / 2 + 6,
                        UIComponents.FONT_BODY,
                        UIComponents.TEXT_BLACK);
            } else {
                int textX = boxX + 25;
                int textY = boxY + 55;
                int lineGap = 28;

                g2.drawString("Movie: " + ticket.getMovieName(), textX, textY);
                g2.drawString("Duration: " + ticket.getDuration(), textX, textY + lineGap);
                g2.drawString("Cinema: " + ticket.getCinemaName(), textX, textY + lineGap * 2);
                g2.drawString("Hall: " + ticket.getHall(), textX, textY + lineGap * 3);
                g2.drawString("Date: " + ticket.getDate(), textX, textY + lineGap * 4);
                g2.drawString("Time: " + ticket.getShowTime(), textX, textY + lineGap * 5);
                g2.drawString("Seat: " + ticket.getSeat(), textX, textY + lineGap * 6);
            }

            //Enjoying message
            int msgY = boxY + boxH + 70;
            UIComponents.drawCenteredText(g2,"Lights off, volume up, just enjoy the ride!!!", x,
                    msgY, new Font("Arial", Font.PLAIN, 18), UIComponents.TEXT_WHITE_SOFT);

            //Back to homepage button and text
            int btnW = 300, btnH = 55, btnX = (w - btnW) / 2, btnY = 600;
            backBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, backBtnRect, "Back to Home", backPressed);
        }
    }
}