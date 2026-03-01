package GUI;

import App.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JPanel {

    private final Appframe app;
    private final Image bg;
    private final Image logo;

    // Icons
    private final Image ticketImg;
    private final Image prefsImg;
    private final Image historyImg;
    private final Image rateImg;

    // Click areas
    private Rectangle ticketRect, prefsRect, historyRect, rateRect;
    private Rectangle surpriseBtnRect;

    // Hover / Press state
    private enum Target { NONE, TICKET, PREFS, HISTORY, RATE }
    private Target hoverTarget = Target.NONE;
    private Target pressedTarget = Target.NONE;

    private boolean surpriseHover, surprisePressed;

    public HomePage(Appframe app) {
        this.app = app;

        bg   = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        ticketImg  = new ImageIcon("resources/images/ticket.png").getImage();
        prefsImg   = new ImageIcon("resources/images/prefs.png").getImage();
        historyImg = new ImageIcon("resources/images/history.png").getImage();
        rateImg    = new ImageIcon("resources/images/star.png").getImage();

        setLayout(new BorderLayout());
        add(new HomePanel(), BorderLayout.CENTER);
    }

    class HomePanel extends JPanel {

        HomePanel() {

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {

                    // Surprise button hover
                    surpriseHover = surpriseBtnRect != null && surpriseBtnRect.contains(e.getPoint());

                    // Cards hover
                    hoverTarget = getTargetAt(e.getPoint());

                    boolean hand = surpriseHover || hoverTarget != Target.NONE;
                    setCursor(new Cursor(hand ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    // Surprise button press
                    if (surpriseBtnRect != null && surpriseBtnRect.contains(e.getPoint())) {
                        surprisePressed = true;
                    }

                    // Cards press
                    pressedTarget = getTargetAt(e.getPoint());

                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    // Surprise button click
                    if (surprisePressed && surpriseBtnRect != null && surpriseBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.MOVIERESULT);
                    }

                    // Card click:
                    // Only navigate if mouse is released on the SAME card that was pressed
                    Target releasedTarget = getTargetAt(e.getPoint());
                    if (pressedTarget != Target.NONE && pressedTarget == releasedTarget) {
                        if (pressedTarget == Target.TICKET)
                            app.showPage(Appframe.MYTICKET);
                        else if (pressedTarget == Target.PREFS)
                            app.showPage(Appframe.PREFERENCES);
                        else if (pressedTarget == Target.HISTORY)
                            app.showPage(Appframe.HISTORY);
                        else if (pressedTarget == Target.RATE)
                            app.showPage(Appframe.RATE);
                    }

                    surprisePressed = false;
                    pressedTarget = Target.NONE;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    surpriseHover = false;
                    surprisePressed = false;
                    hoverTarget = Target.NONE;
                    pressedTarget = Target.NONE;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });
        }

        private Target getTargetAt(Point p) {
            if (ticketRect != null && ticketRect.contains(p)) return Target.TICKET;
            if (prefsRect != null && prefsRect.contains(p)) return Target.PREFS;
            if (historyRect != null && historyRect.contains(p)) return Target.HISTORY;
            if (rateRect != null && rateRect.contains(p)) return Target.RATE;
            return Target.NONE;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            g2.drawImage(bg, 0, 0, w, h, null);

            // Logo
            int logoSize = 200;
            int logoX = (w - logoSize) / 2;
            g2.drawImage(logo, logoX, 0, logoSize, logoSize, null);

            // Title
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            String title = "SURPRISE CINEMA";
            FontMetrics fmT = g2.getFontMetrics();
            g2.drawString(title, (w - fmT.stringWidth(title)) / 2, logoSize + 26);

            // Welcome
            g2.setColor(new Color(255, 255, 255, 180));
            g2.setFont(new Font("Arial", Font.PLAIN, 17));
            String welcome = "Welcome, Emma!";
            FontMetrics fmW = g2.getFontMetrics();
            g2.drawString(welcome, (w - fmW.stringWidth(welcome)) / 2, logoSize + 54);

            // 4 cards grid
            int cardW = 130, cardH = 110;
            int gapX = 16, gapY = 16;
            int totalW = cardW * 2 + gapX;
            int startX = (w - totalW) / 2;
            int startY = logoSize + 80;

            // IMPORTANT: Rectangles are re-created every repaint (that's why we use Target enum)
            ticketRect  = new Rectangle(startX, startY, cardW, cardH);
            prefsRect   = new Rectangle(startX + cardW + gapX, startY, cardW, cardH);
            historyRect = new Rectangle(startX, startY + cardH + gapY, cardW, cardH);
            rateRect    = new Rectangle(startX + cardW + gapX, startY + cardH + gapY, cardW, cardH);

            drawCard(g2, ticketRect,  "Ticket",            ticketImg,
                    hoverTarget == Target.TICKET, pressedTarget == Target.TICKET);

            drawCard(g2, prefsRect,   "Edit Preferences",  prefsImg,
                    hoverTarget == Target.PREFS, pressedTarget == Target.PREFS);

            drawCard(g2, historyRect, "History",           historyImg,
                    hoverTarget == Target.HISTORY, pressedTarget == Target.HISTORY);

            drawCard(g2, rateRect,    "Rate Movie",        rateImg,
                    hoverTarget == Target.RATE, pressedTarget == Target.RATE);

            // Surprise button
            int btnW = 300, btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 575;
            surpriseBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            drawButton(g2, surpriseBtnRect, "Surprise Me!", surpriseHover, surprisePressed);
        }

        private void drawCard(Graphics2D g2, Rectangle r, String label, Image icon, boolean hover, boolean pressed) {

            // Shadow
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(r.x + 2, r.y + 3, r.width, r.height, 20, 20);

            // Background
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 20, 20);

            // Hover overlay
            if (hover) {
                g2.setColor(new Color(255, 0, 0, 15));
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 20, 20);
            }

            // Press overlay
            if (pressed) {
                g2.setColor(new Color(200, 0, 0, 60));
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 20, 20);
            }

            // Border
            g2.setColor(new Color(220, 220, 220));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(r.x, r.y, r.width, r.height, 20, 20);

            // Icon (bigger)
            int imgSize = 70; // <-- change this if you want even bigger
            int imgX = r.x + (r.width - imgSize) / 2;
            int imgY = r.y + 10;

            if (icon != null) {
                g2.drawImage(icon, imgX, imgY, imgSize, imgSize, null);
            }

            // Label
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label,
                    r.x + (r.width - fm.stringWidth(label)) / 2,
                    r.y + r.height - 14);
        }
    }

    private void drawButton(Graphics2D g2, Rectangle r, String text, boolean hover, boolean pressed) {

        int radius = 50;

        // Shadow
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(r.x + 2, r.y + 4, r.width, r.height, radius, radius);

        // Base
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        // Hover overlay
        if (hover) {
            g2.setColor(new Color(255, 0, 0, 18));
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
        }

        // Press overlay
        if (pressed) {
            g2.setColor(new Color(200, 0, 0, 70));
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
        }

        // Border
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(220, 220, 220));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        // Text
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text,
                r.x + (r.width - fm.stringWidth(text)) / 2,
                r.y + (r.height + fm.getAscent()) / 2 - 4);
    }
}
