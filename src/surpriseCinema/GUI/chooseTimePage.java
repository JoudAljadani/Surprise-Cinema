package surpriseCinema.GUI;

import surpriseCinema.App.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class chooseTimePage extends JPanel {

        private final Appframe app;
        private final Image bg;

        // clickable areas
        private Rectangle nextBtnRect;
        private boolean nextPressed = false;

        private Card selectedSlot = null;

    // back arrow
    private Rectangle backRect;
    private boolean backPressed = false;

        // 4 time slots (no overlap)
        private final Card[] timeCards = new Card[]{
                new Card("6:00 AM - 8:59 AM", "🌅", "EARLY_MORNING"),
                new Card("9:00 AM - 11:59 AM", "🌤️", "LATE_MORNING"),
                new Card("12:00 PM - 2:59 PM", "☀️", "EARLY_AFTERNOON"),
                new Card("3:00 PM - 5:59 PM", "🌥️", "LATE_AFTERNOON"),
                new Card("6:00 PM - 8:59 PM", "🌙", "EVENING"),
                new Card("9:00 PM - 5:59 AM", "🌌", "LATE_NIGHT")
        };

        public chooseTimePage(Appframe app) {
            this.app = app;

            bg = new ImageIcon("resources/images/Background.png").getImage();

            setLayout(new BorderLayout());
            add(new TimePanel(), BorderLayout.CENTER);
        }

        // ===========================
        // MAIN PANEL
        // ===========================
        class TimePanel extends JPanel {

            TimePanel() {

                addMouseListener(new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {

                        if (backRect != null && backRect.contains(e.getPoint())) {
                            backPressed = true;
                            repaint();
                            return;
                        }

                        // click time cards
                        for (Card c : timeCards) {
                            if (c.rect != null && c.rect.contains(e.getPoint())) {
                                selectedSlot = c;
                                repaint();
                                return;
                            }
                        }

                        // press next
                        if (nextBtnRect != null && nextBtnRect.contains(e.getPoint())) {
                            nextPressed = true;
                            repaint();
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                        if (backPressed && backRect != null && backRect.contains(e.getPoint())) {
                            app.showPage(Appframe.MOVIERESULT); // يرجع لصفحة الفيلم
                        }
                        backPressed = false;

                        if (nextPressed && nextBtnRect != null && nextBtnRect.contains(e.getPoint())) {

                            //force selection
                            if (selectedSlot == null) { nextPressed = false; repaint(); return; }

                            app.showPage(Appframe.TICKET_SUCCESS);
                        }

                        nextPressed = false;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        nextPressed = false;
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

                // ===== BACK ARROW (no box) =====
                int bx = 18, by = 32;
                backRect = new Rectangle(bx, by, 28, 28);
                drawBackArrow(g2, backRect, backPressed);

                // ===== PAGE TITLE =====
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 22));

                String pageTitle = "Pick Your showtime!";
                FontMetrics fmT = g2.getFontMetrics();

                int titleX = (w - fmT.stringWidth(pageTitle)) / 2;
                int titleY = 95;

                g2.drawString(pageTitle, titleX, titleY);

                // ===== QUESTION =====
                g2.setFont(new Font("Arial", Font.PLAIN, 15));
                String q = "When would you like to watch this movie today?";
                FontMetrics fmQ = g2.getFontMetrics();

                int qX = (w - fmQ.stringWidth(q)) / 2;
                int qY = titleY + 34;

                g2.drawString(q, qX, qY);

                // ===== TIME GRID (2 cols, 2 rows) =====
                int topY = qY + 35;
                layoutCardsGrid(timeCards, w, topY, 2);

                for (Card c : timeCards) {
                    boolean selected = (selectedSlot == c);
                    drawCard(g2, c.rect, c.icon, c.label, selected);
                }

                // ===== NEXT BUTTON =====
                int btnW = 300;
                int btnH = 55;
                int btnX = (w - btnW) / 2;
                int btnY = 600;

                nextBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
                drawButton(g2, nextBtnRect, "Book Now!", nextPressed);
            }
        }

        // ===========================
        // HELPERS
        // ===========================
        private void layoutCardsGrid(Card[] cards, int panelW, int topY, int cols) {

            int cardW = 140;
            int cardH = 82;
            int gapX = 16;
            int gapY = 14;

            int totalW = cols * cardW + (cols - 1) * gapX;
            int startX = (panelW - totalW) / 2;

            for (int i = 0; i < cards.length; i++) {
                int row = i / cols;
                int col = i % cols;

                int x = startX + col * (cardW + gapX);
                int y = topY + row * (cardH + gapY);

                cards[i].rect = new Rectangle(x, y, cardW, cardH);
            }
        }

    private void drawCard(Graphics2D g2, Rectangle r, String icon, String text, boolean selected) {

        int radius = 22;

        // base card
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        // ✅ selected overlay (زي ضغط الزر)
        if (selected) {
            g2.setColor(new Color(200, 0, 0, 70)); // نفس فكرة ضغط الزر
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
        }

        // border ثابت (بدون ما يصير أسود)
        g2.setStroke(new BasicStroke(2.3f));
        g2.setColor(new Color(242, 242, 242));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        // icon
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        FontMetrics fi = g2.getFontMetrics();
        int ix = r.x + (r.width - fi.stringWidth(icon)) / 2;
        int iy = r.y + 34;
        g2.drawString(icon, ix, iy);

        // text
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics ft = g2.getFontMetrics();
        int tx = r.x + (r.width - ft.stringWidth(text)) / 2;
        int ty = r.y + 62;
        g2.drawString(text, tx, ty);
    }

        private void drawButton(Graphics2D g2, Rectangle r, String text, boolean pressed) {

            int radius = 50;

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

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

        // ===========================
        // DATA CLASS
        // ===========================
        static class Card {
            String label, icon, key;
            Rectangle rect;

            Card(String label, String icon, String key) {
                this.label = label;
                this.icon = icon;
                this.key = key;
            }
        }

    private void drawBackArrow(Graphics2D g2, Rectangle r, boolean pressed) {

        g2.setFont(new Font("Arial", Font.BOLD, 26));
        g2.setColor(pressed ? new Color(255,255,255,160) : Color.WHITE);

        String arrow = "<";
        FontMetrics fm = g2.getFontMetrics();

        int ax = r.x + 2;
        int ay = r.y + fm.getAscent() - 2;

        g2.drawString(arrow, ax, ay);
    }

}
