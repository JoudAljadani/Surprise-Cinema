import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class History extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image

    //Clickable buttons
    private Rectangle closeBtnRect;
    private Rectangle dashBtnRect;

    private boolean closePressed;
    private boolean dashPressed;

//------------------------------------------------------------

    public History(Appframe app) {
        this.app = app;
        //Background image
        bg = new ImageIcon("resources/images/Background.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new HistoryPanel(), BorderLayout.CENTER);
    }

    class HistoryPanel extends JPanel {

        HistoryPanel() {
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    //if dashboard button is pressed
                    if (dashBtnRect != null && dashBtnRect.contains(e.getPoint())) dashPressed = true;
                    //if close button is pressed
                    if (closeBtnRect != null && closeBtnRect.contains(e.getPoint())) closePressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //if dashboard button, navigate to dashboard page
                    if (dashPressed && dashBtnRect != null && dashBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.DASHBOARD);
                    }

                    //if close, navigate to homepage
                    if (closePressed && closeBtnRect != null && closeBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
                    }
                    dashPressed = false;
                    closePressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    dashPressed = false;
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
            UIComponents.drawCenteredText(g2, "Last Bookings", w / 2, 56,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            //Card box
            int cardX = 28, cardW = w - 56, cardY = 95, cardH = 360;
            Rectangle big = new Rectangle(cardX, cardY, cardW, cardH);
            drawBigBox(g2, big);

            //Buttons
            int btnW = 145, btnH = 55, gap = 14;
            int total = btnW * 2 + gap;
            int startX = (w - total) / 2;
            int btnY = h - 90;

            //Dashboard button
            dashBtnRect = new Rectangle(startX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, dashBtnRect, "Dashboard", dashPressed);

            //Close button
            closeBtnRect = new Rectangle(startX + btnW + gap, btnY, btnW, btnH);
            UIComponents.drawButton(g2, closeBtnRect, "Close", closePressed);
        }

        private void drawBigBox(Graphics2D g2, Rectangle r) {
            //Base
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 22, 22);

            //Text in the box
            String text = "History Booking details are here";
            UIComponents.drawCenteredText(g2, text,r.x + r.width / 2, r.y + r.height / 2,
                    new Font("Arial", Font.BOLD, 16), Color.BLACK);
        }
    }
}