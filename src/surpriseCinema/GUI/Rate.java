package GUI;

import App.Appframe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;

public class Rate extends JPanel {

    private final Appframe app;
    private final Image bg;
    private final Image poster;

    private Rectangle backBtnRect;
    private Rectangle submitBtnRect;

    private Rectangle[] starRects = new Rectangle[5];

    private boolean backHover, backPressed;
    private boolean submitHover, submitPressed;

    private int rating = 0; // selected rating 1..5

    public Rate(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();
        poster = new ImageIcon("resources/images/movie1.jpg").getImage(); // change if needed

        setLayout(new BorderLayout());
        add(new RatePanel(), BorderLayout.CENTER);
    }

    class RatePanel extends JPanel {

        RatePanel() {

            // Hover only for Back + Submit
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {

                    backHover = backBtnRect != null && backBtnRect.contains(e.getPoint());
                    submitHover = submitBtnRect != null && submitBtnRect.contains(e.getPoint());

                    boolean hand = backHover || submitHover;
                    setCursor(new Cursor(hand ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    if (backBtnRect != null && backBtnRect.contains(e.getPoint())) backPressed = true;
                    if (submitBtnRect != null && submitBtnRect.contains(e.getPoint())) submitPressed = true;

                    // Select rating by clicking stars (no hover effect)
                    for (int i = 0; i < 5; i++) {
                        if (starRects[i] != null && starRects[i].contains(e.getPoint())) {
                            rating = i + 1;
                            break;
                        }
                    }

                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    if (backPressed && backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOMEPAGE);
                    }

                    // No popup, just go home on submit
                    if (submitPressed && submitBtnRect != null && submitBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOMEPAGE);
                    }

                    backPressed = false;
                    submitPressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backHover = backPressed = false;
                    submitHover = submitPressed = false;
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
            String title = "Rate Movie";
            FontMetrics fmT = g2.getFontMetrics();
            g2.drawString(title, (w - fmT.stringWidth(title)) / 2, 56);

            // Card
            int cardX = 28;
            int cardY = 120;
            int cardW = w - 56;
            int cardH = 420;

            g2.setColor(new Color(0,0,0,50));
            g2.fillRoundRect(cardX + 3, cardY + 5, cardW, cardH, 24, 24);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 24, 24);

            g2.setColor(new Color(220,220,220));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(cardX, cardY, cardW, cardH, 24, 24);

            // Poster
            int posterX = cardX + 20;
            int posterY = cardY + 18;
            int posterW = cardW - 40;
            int posterH = 170;

            Shape oldClip = g2.getClip();
            g2.setClip(new java.awt.geom.RoundRectangle2D.Double(posterX, posterY, posterW, posterH, 14, 14));
            g2.drawImage(poster, posterX, posterY, posterW, posterH, null);
            g2.setClip(oldClip);

            g2.setColor(new Color(200,200,200));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(posterX, posterY, posterW, posterH, 14, 14);

            // Movie name
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            String movieName = "Interstellar";
            FontMetrics fmM = g2.getFontMetrics();
            int movieY = posterY + posterH + 35;
            g2.drawString(movieName, (w - fmM.stringWidth(movieName)) / 2, movieY);

            // Instruction
            g2.setColor(new Color(130,130,130));
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            String inst = "Tap a star to rate";
            FontMetrics fmI = g2.getFontMetrics();
            g2.drawString(inst, (w - fmI.stringWidth(inst)) / 2, movieY + 22);

            // Stars row
            int starSize = 44;
            int gap = 14;
            int totalStarsW = starSize * 5 + gap * 4;
            int startX = (w - totalStarsW) / 2;
            int starsY = movieY + 55;

            for (int i = 0; i < 5; i++) {
                int sx = startX + i * (starSize + gap);

                // Click area
                starRects[i] = new Rectangle(sx, starsY - starSize + 10, starSize, starSize);

                boolean filled = (i + 1) <= rating;

                drawStar(g2, sx + starSize / 2, starsY, starSize / 2.1, starSize / 4.2, filled);
            }

            // Submit button
            int btnW = 220, btnH = 45;
            int btnX = (w - btnW) / 2;
            int btnY = starsY + 60;
            submitBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            drawButton(g2, submitBtnRect, "Submit Rating", submitHover, submitPressed);
        }

        // Simple star drawing
        private void drawStar(Graphics2D g2, int cx, int cy, double outerR, double innerR, boolean filled) {

            Path2D star = new Path2D.Double();
            double angle = -Math.PI / 2;
            double step = Math.PI / 5;

            for (int i = 0; i < 10; i++) {
                double r = (i % 2 == 0) ? outerR : innerR;
                double x = cx + Math.cos(angle) * r;
                double y = cy + Math.sin(angle) * r;

                if (i == 0) star.moveTo(x, y);
                else star.lineTo(x, y);

                angle += step;
            }
            star.closePath();

            if (filled) {
                g2.setColor(new Color(255, 180, 0));
                g2.fill(star);
            } else {
                g2.setColor(new Color(200, 200, 200));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(star);
            }
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
        g2.setFont(new Font("Arial", Font.BOLD, 15)); // smaller for the smaller button
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text,
                r.x + (r.width - fm.stringWidth(text)) / 2,
                r.y + (r.height + fm.getAscent()) / 2 - 4);
    }
}