package GUI;

import App.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyTicket extends JPanel {

    private final Appframe app;
    private final Image bg;

    // close button only
    private Rectangle closeBtnRect;
    private boolean closePressed = false;

    public MyTicket(Appframe app) {
        this.app = app;
        bg = new ImageIcon("resources/images/Background.png").getImage();

        setLayout(new BorderLayout());
        add(new TicketPanel(), BorderLayout.CENTER);
    }

    class TicketPanel extends JPanel {

        TicketPanel() {
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    if (closeBtnRect != null && closeBtnRect.contains(e.getPoint())) {
                        closePressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

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
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.drawImage(bg, 0, 0, w, h, null);

            // ===== TITLE =====
            int titleY = 95, x = w / 2;
            UIComponents.drawCenteredText(g2, "Your current ticket", x, titleY,
                    UIComponents.FONT_BRAND, UIComponents.TEXT_WHITE);

            // ===== TICKET BOX =====
            int boxW = 260;
            int boxH = 300;
            int boxX = (w - boxW) / 2;
            int boxY = titleY + 35;

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 22, 22);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(220, 220, 220));
            g2.drawRoundRect(boxX, boxY, boxW, boxH, 22, 22);

            // placeholder text
            UIComponents.drawCenteredText(g2, "Ticket details are here", x,
                    boxY + boxH / 2 + 6,
                    UIComponents.FONT_BODY,
                    UIComponents.TEXT_BLACK);

            // ===== MESSAGE =====
            int msgY = boxY + boxH + 70;
            UIComponents.drawCenteredText(g2,
                    "Lights off, volume up, just enjoy the ride!!!",
                    x,
                    msgY,
                    new Font("Arial", Font.PLAIN, 18),
                    UIComponents.TEXT_WHITE_SOFT);

            // ===== CLOSE BUTTON =====
            int btnW = 300;
            int btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 600;

            closeBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawPrimaryButton(g2, closeBtnRect, "Close", closePressed);
        }
    }
}