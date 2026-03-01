package GUI;

import App.Appframe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyTicket extends JPanel {

    private final Appframe app;
    private final Image bg;

    // NEW: poster image
    private final Image poster;

    private Rectangle backBtnRect;
    private Rectangle homeBtnRect;

    private boolean backHover, backPressed;
    private boolean homeHover, homePressed;

    public MyTicket(Appframe app) {
        this.app = app;
        bg = new ImageIcon("resources/images/Background.png").getImage();

        // NEW: load poster
        poster = new ImageIcon("resources/images/movie1.jpg").getImage();

        setLayout(new BorderLayout());
        add(new TicketPanel(), BorderLayout.CENTER);
    }

    class TicketPanel extends JPanel {

        TicketPanel() {

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    backHover = backBtnRect != null && backBtnRect.contains(e.getPoint());
                    homeHover = homeBtnRect != null && homeBtnRect.contains(e.getPoint());

                    if (backHover || homeHover) setCursor(new Cursor(Cursor.HAND_CURSOR));
                    else setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (backBtnRect != null && backBtnRect.contains(e.getPoint())) backPressed = true;
                    if (homeBtnRect != null && homeBtnRect.contains(e.getPoint())) homePressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    if (backPressed && backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOMEPAGE);
                    }

                    if (homePressed && homeBtnRect != null && homeBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOMEPAGE);
                    }

                    backPressed = false;
                    homePressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backHover = backPressed = false;
                    homeHover = homePressed = false;
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

            // Back button (top-left)
            int backSize = 36;
            int backX = 20, backY = 30;
            backBtnRect = new Rectangle(backX, backY, backSize, backSize);

            // Back arrow ()
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int ax = backX + backSize / 2;
            int ay = backY + backSize / 2;
            g2.drawLine(ax + 6, ay - 6, ax - 4, ay);
            g2.drawLine(ax - 4, ay, ax + 6, ay + 6);

            // Title
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            String title = "Your current ticket";
            FontMetrics fmT = g2.getFontMetrics();
            g2.drawString(title, (w - fmT.stringWidth(title)) / 2, 56);

            // Ticket card
            int cardX = 28;
            int cardY = 85;
            int cardW = w - 56;
            int cardH = 420;

            // Shadow
            g2.setColor(new Color(0,0,0,50));
            g2.fillRoundRect(cardX + 3, cardY + 5, cardW, cardH, 24, 24);

            // Card background
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 24, 24);
            g2.setColor(new Color(220,220,220));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(cardX, cardY, cardW, cardH, 24, 24);

            // Divider line
            int dividerY = cardY + 220;
            g2.setColor(new Color(210,210,210));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(cardX + 20, dividerY, cardX + cardW - 20, dividerY);

            // Poster area
            int posterX = cardX + 20;
            int posterY = cardY + 18;
            int posterW = cardW - 40;
            int posterH = 160;

            //poster with rounded corners
            Shape oldClip = g2.getClip();
            g2.setClip(new java.awt.geom.RoundRectangle2D.Double(posterX, posterY, posterW, posterH, 14, 14));
            g2.drawImage(poster, posterX, posterY, posterW, posterH, null);
            g2.setClip(oldClip);

            // Poster border
            g2.setColor(new Color(200,200,200));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(posterX, posterY, posterW, posterH, 14, 14);

            // Movie title
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            String movieName = "Interstellar";
            FontMetrics fmM = g2.getFontMetrics();
            int movieY = posterY + posterH + 30;
            g2.drawString(movieName, (w - fmM.stringWidth(movieName)) / 2, movieY);

            // Booking details
            int infoY = dividerY + 35;

            // Left column
            g2.setColor(new Color(150,150,150));
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            g2.drawString("Date", cardX + 24, infoY);
            g2.drawString("Time", cardX + 24, infoY + 55);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.drawString("Feb 20, 2026", cardX + 24, infoY + 20);
            g2.drawString("7:30 PM", cardX + 24, infoY + 75);

            // Right column
            int rightX = cardX + cardW / 2 + 10;

            g2.setColor(new Color(150,150,150));
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            g2.drawString("Seat", rightX, infoY);
            g2.drawString("Hall", rightX, infoY + 55);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.drawString("A12", rightX, infoY + 20);
            g2.drawString("Cinema 3", rightX, infoY + 75);

            // Booking ID
            g2.setColor(new Color(150,150,150));
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            String bid = "Booking ID: #SC-2026-0042";
            FontMetrics fmB = g2.getFontMetrics();
            g2.drawString(bid, (w - fmB.stringWidth(bid)) / 2, infoY + 120);

            // Tagline
            g2.setColor(new Color(255,255,255,200));
            g2.setFont(new Font("Arial", Font.ITALIC, 14));
            String tag = "Lights off, volume up, just enjoy the ride!";
            FontMetrics fmTag = g2.getFontMetrics();
            g2.drawString(tag, (w - fmTag.stringWidth(tag)) / 2, cardY + cardH + 45);

            // Back to Home button
            int btnW = 300, btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = h - 90;
            homeBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            drawButton(g2, homeBtnRect, "Close", homeHover, homePressed);
        }
    }

    private void drawButton(Graphics2D g2, Rectangle r, String text, boolean hover, boolean pressed) {
        int radius = 50;

        g2.setColor(new Color(0,0,0,40));
        g2.fillRoundRect(r.x + 2, r.y + 4, r.width, r.height, radius, radius);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        if (hover) {
            g2.setColor(new Color(255,0,0,18));
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
        }

        if (pressed) {
            g2.setColor(new Color(200,0,0,70));
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
        }

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(220,220,220));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text,
                r.x + (r.width - fm.stringWidth(text)) / 2,
                r.y + (r.height + fm.getAscent()) / 2 - 4);
    }
}