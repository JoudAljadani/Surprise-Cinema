package GUI;

import App.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class History extends JPanel {

    private final Appframe app;
    private final Image bg;

    private Rectangle backBtnRect;
    private Rectangle closeBtnRect;

    private boolean backHover, backPressed;
    private boolean closeHover, closePressed;

    // Demo data (edit if you want)
    private final String[] movieNames = {"Interstellar", "Batman", "Parasite"};
    private final String[] dates      = {"Feb 20, 2026", "Feb 12, 2026", "Jan 30, 2026"};
    private final String[] times      = {"7:30 PM",      "9:00 PM",      "6:15 PM"};
    private final String[] seats      = {"A12",          "B07",          "C03"};
    private final Image[] posters;

    public History(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();

        posters = new Image[]{
                new ImageIcon("resources/images/movie1.jpg").getImage(), // Interstellar
                new ImageIcon("resources/images/movie2.jpg").getImage(), // Batman
                new ImageIcon("resources/images/movie3.jpg").getImage()  // Parasite
        };

        setLayout(new BorderLayout());
        add(new HistoryPanel(), BorderLayout.CENTER);
    }

    class HistoryPanel extends JPanel {

        HistoryPanel() {

            // No hover for cards, only buttons
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    backHover = backBtnRect != null && backBtnRect.contains(e.getPoint());
                    closeHover = closeBtnRect != null && closeBtnRect.contains(e.getPoint());

                    boolean hand = backHover || closeHover;
                    setCursor(new Cursor(hand ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (backBtnRect != null && backBtnRect.contains(e.getPoint())) backPressed = true;
                    if (closeBtnRect != null && closeBtnRect.contains(e.getPoint())) closePressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    if (backPressed && backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOMEPAGE);
                    }

                    // Keep Close button working
                    if (closePressed && closeBtnRect != null && closeBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOMEPAGE);
                    }

                    backPressed = false;
                    closePressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backHover = backPressed = false;
                    closeHover = closePressed = false;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Background
            g2.drawImage(bg, 0, 0, w, h, null);

            // Back arrow (top-left)
            int backSize = 36;
            int backX = 20, backY = 30;
            backBtnRect = new Rectangle(backX, backY, backSize, backSize);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int ax = backX + backSize / 2;
            int ay = backY + backSize / 2;
            g2.drawLine(ax + 6, ay - 6, ax - 4, ay);
            g2.drawLine(ax - 4, ay, ax + 6, ay + 6);

            // Title
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            String title = "Last Bookings";
            FontMetrics fmT = g2.getFontMetrics();
            g2.drawString(title, (w - fmT.stringWidth(title)) / 2, 56);

            // Cards layout
            int cardX = 28;
            int cardW = w - 56;
            int cardH = 115;
            int gap = 14;
            int startY = 95;

            for (int i = 0; i < 3; i++) {
                int y = startY + i * (cardH + gap);
                Rectangle r = new Rectangle(cardX, y, cardW, cardH);
                drawBookingCard(g2, r, posters[i], movieNames[i], dates[i], times[i], seats[i]);
            }

            // Close button
            int btnW = 300, btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = h - 90;
            closeBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            drawButton(g2, closeBtnRect, "Close", closeHover, closePressed);
        }

        private void drawBookingCard(Graphics2D g2, Rectangle r, Image poster, String name,
                                     String date, String time, String seat) {

            // Shadow
            g2.setColor(new Color(0, 0, 0, 45));
            g2.fillRoundRect(r.x + 2, r.y + 4, r.width, r.height, 20, 20);

            // Card base
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 20, 20);

            // Border
            g2.setColor(new Color(220, 220, 220));
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(r.x, r.y, r.width, r.height, 20, 20);

            // Poster (left)
            int pX = r.x + 14;
            int pY = r.y + 14;
            int pW = 85;
            int pH = r.height - 28;

            Shape oldClip = g2.getClip();
            g2.setClip(new java.awt.geom.RoundRectangle2D.Double(pX, pY, pW, pH, 14, 14));
            g2.drawImage(poster, pX, pY, pW, pH, null);
            g2.setClip(oldClip);

            g2.setColor(new Color(210, 210, 210));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(pX, pY, pW, pH, 14, 14);

            // Text (right)
            int tx = pX + pW + 14;
            int top = r.y + 32;

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString(name, tx, top);

            g2.setColor(new Color(120, 120, 120));
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Date: " + date, tx, top + 22);
            g2.drawString("Time: " + time, tx, top + 42);
            g2.drawString("Seat: " + seat, tx, top + 62);
        }
    }

    private void drawButton(Graphics2D g2, Rectangle r, String text, boolean hover, boolean pressed) {

        int radius = 50;

        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(r.x + 2, r.y + 4, r.width, r.height, radius, radius);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

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
        g2.drawString(text,
                r.x + (r.width - fm.stringWidth(text)) / 2,
                r.y + (r.height + fm.getAscent()) / 2 - 4);
    }
}