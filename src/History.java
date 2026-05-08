import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class History extends JPanel {

    //Variables
    private final Appframe app; //To navigate between pages
    private final Image bg; //Background image

    //Clickable buttons
    private Rectangle closeBtnRect;
    private Rectangle openBtnRect;

    private boolean closePressed;
    private boolean openPressed;

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

                    //if close button is pressed
                    if (closeBtnRect != null && closeBtnRect.contains(e.getPoint())) {
                        closePressed = true;
                    }

                    //if open file button is pressed
                    if (openBtnRect != null && openBtnRect.contains(e.getPoint())) {
                        openPressed = true;
                    }

                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    //if close, navigate to homepage
                    if (closePressed && closeBtnRect != null && closeBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
                    }

                    //if open file, open current user's history file
                    if (openPressed && openBtnRect != null && openBtnRect.contains(e.getPoint())) {
                        TicketFileManager.openTicketHistoryFile(Appframe.currentUser.getEmail());
                    }

                    closePressed = false; openPressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closePressed = false; openPressed = false;
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
            int w = getWidth(); int h = getHeight();

            //Background
            g2.drawImage(bg, 0, 0, w, h, null);

            //Title
            UIComponents.drawCenteredText(g2, "Last Bookings", w / 2,
                    56, UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            //Card box
            int cardX = 28; int cardW = w - 56;
            int cardY = 95;int cardH = 360;

            Rectangle big = new Rectangle(cardX, cardY, cardW, cardH);
            drawBigBox(g2, big);

            //Buttons
            int btnW = 145;
            int btnH = 55;
            int gap = 14;
            int total = btnW * 2 + gap;
            int startX = (w - total) / 2;
            int btnY = h - 90;

            //Close button
            closeBtnRect = new Rectangle(startX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, closeBtnRect, "Back", closePressed);

            //Open file button
            openBtnRect = new Rectangle(startX + btnW + gap, btnY, btnW, btnH);
            UIComponents.drawButton(g2, openBtnRect, "Open File", openPressed);
        }

        private void drawBigBox(Graphics2D g2, Rectangle r) {

            //Base
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 22, 22);

            //Read current user's history from file
            String history = TicketFileManager.readTicketHistory(Appframe.currentUser.getEmail());

            //Draw preview text
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            int textX = r.x + 18;
            int textY = r.y + 30;
            int lineGap = 18;

            String[] lines = history.split("\n");

            int maxLines = 16;

            for (int i = 0; i < lines.length && i < maxLines; i++) {
                g2.drawString(lines[i], textX, textY + (i * lineGap));
            }

            if (lines.length > maxLines) {
                g2.drawString("... Click Open File to view full history",
                        textX, textY + (maxLines * lineGap));
            }
        }
    }
}