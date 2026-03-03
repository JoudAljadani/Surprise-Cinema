package GUI;

import App.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicketSuccess extends JPanel {

    private final Appframe app;
    private final Image bg;

    // clickable areas
    private Rectangle backBtnRect;
    private boolean backPressed = false;

    public TicketSuccess(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();

        setLayout(new BorderLayout());
        add(new TicketPanel(), BorderLayout.CENTER);
    }

    // ===========================
    // MAIN PANEL
    // ===========================
    class TicketPanel extends JPanel {

        TicketPanel() {

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        backPressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    if (backPressed && backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);

                        // مؤقتًا تقدري ترجعينه لأي صفحة موجودة:
                        // app.showPage(Appframe.SPLASH);
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
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.drawImage(bg, 0, 0, w, h, null);

            // ===== PAGE TITLE =====
            int titleY = 95, x = w / 2;
            UIComponents.drawCenteredText(g2,"Your Ticket is Ready!", x, titleY,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            // ===== BIG TICKET BOX =====
            int boxW = 260;
            int boxH = 300;
            int boxX = (w - boxW) / 2;
            int boxY = titleY + 35;

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 22, 22);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(220, 220, 220));
            g2.drawRoundRect(boxX, boxY, boxW, boxH, 22, 22);

            // placeholder text inside ticket box
            UIComponents.drawCenteredText(g2,"Ticket details are here", x,
                    boxY + boxH / 2 + 6, UIComponents.FONT_BODY, UIComponents.TEXT_BLACK);


            // ===== ENJOY MESSAGE =====
            int msgY = boxY + boxH + 70;
            UIComponents.drawCenteredText(g2,"Lights off, volume up, just enjoy the ride!!!", x,
                    msgY, new Font("Arial", Font.PLAIN, 18), UIComponents.TEXT_WHITE_SOFT);

            // ===== BACK BUTTON =====
            int btnW = 300;
            int btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 600;

            backBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawPrimaryButton(g2, backBtnRect, "Back to Home", backPressed);
        }
    }
}