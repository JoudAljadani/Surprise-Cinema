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
                        // TODO: رجعيه للهوم لما تسوينها
                        // app.showPage(Appframe.HOME);

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
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));

            String title = "Your Ticket is Ready!";
            FontMetrics fmT = g2.getFontMetrics();
            int titleX = (w - fmT.stringWidth(title)) / 2;
            int titleY = 95;

            g2.drawString(title, titleX, titleY);

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
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String ph = "Ticket details are here";
            FontMetrics fmPH = g2.getFontMetrics();
            int phX = boxX + (boxW - fmPH.stringWidth(ph)) / 2;
            int phY = boxY + boxH / 2;
            g2.drawString(ph, phX, phY);

            // ===== ENJOY MESSAGE =====
            g2.setColor(new Color(255, 255, 255, 230));
            g2.setFont(new Font("Arial", Font.PLAIN, 18));

            String msg = "Lights off, volume up, just enjoy the ride!!!";
            FontMetrics fm = g2.getFontMetrics();
            int msgX = (w - fm.stringWidth(msg)) / 2;
            int msgY = boxY + boxH + 70;

            g2.drawString(msg, msgX, msgY);

            // ===== BACK BUTTON =====
            int btnW = 300;
            int btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 600;

            backBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            drawButton(g2, backBtnRect, "Back to Home", false, backPressed);
        }
    }

    // ===========================
    // BUTTON (same style)
    // ===========================
    private void drawButton(Graphics2D g2, Rectangle r, String text, boolean hover, boolean pressed) {

        int radius = 50;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        // hover unused
        if (hover) {
            g2.setColor(new Color(255, 0, 0, 18));
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
        }

        if (pressed) {
            g2.setColor(new Color(200, 0, 0, 70));
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
        }

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(220, 220, 220));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));

        FontMetrics fm = g2.getFontMetrics();
        int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
        int ty = r.y + (r.height + fm.getAscent()) / 2 - 4;

        g2.drawString(text, tx, ty);
    }
}