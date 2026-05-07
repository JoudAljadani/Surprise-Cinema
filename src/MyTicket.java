import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyTicket extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image

    //Clickable button
    private Rectangle closeBtnRect;
    private boolean closePressed = false;

//------------------------------------------------------------

    public MyTicket(Appframe app) {
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
                    //if close is pressed
                    if (closeBtnRect != null && closeBtnRect.contains(e.getPoint())) {
                        closePressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //if close, navigate to home page
                    if (closePressed && closeBtnRect != null && closeBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
                    }
                    closePressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closePressed = false;
                    repaint();
                }
            });
        }

        @Override
        //Draw UI elements
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            //Make corner smoother
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Get panel width and height
            int w = getWidth(), h = getHeight();

            //Background
            g2.drawImage(bg, 0, 0, w, h, null);

            //Title
            int titleY = 95, x = w / 2;
            UIComponents.drawCenteredText(g2, "Your current ticket", x, titleY,
                    UIComponents.FONT_BRAND, UIComponents.TEXT_WHITE);

            //Ticket box
            int boxW = 260, boxH = 300, boxX = (w - boxW) / 2, boxY = titleY + 35;
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 22, 22);

            //Text in the ticket box
            UIComponents.drawCenteredText(g2, "Ticket details are here", x,
                    boxY + boxH / 2 + 6,
                    UIComponents.FONT_BODY,
                    UIComponents.TEXT_BLACK);

            //Enjoying message
            int msgY = boxY + boxH + 70;
            UIComponents.drawCenteredText(g2,"Lights off, volume up, just enjoy the ride!!!", x, msgY,
                    new Font("Arial", Font.PLAIN, 18), UIComponents.TEXT_WHITE_SOFT);

            //Close button
            int btnW = 300, btnH = 55, btnX = (w - btnW) / 2, btnY = 600;
            closeBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, closeBtnRect, "Close", closePressed);
        }
    }
}