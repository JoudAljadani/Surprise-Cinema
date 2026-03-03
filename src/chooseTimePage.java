package GUI;

import App.Appframe;

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

        // MAIN PANEL
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
                int backSize = 36, backX = 18, backY = 32;
                backRect = new Rectangle(backX, backY, backSize, backSize);
                UIComponents.drawTextBackArrow(g2, backRect, backPressed);

                // ===== PAGE TITLE =====
                int titleY = 95, x = w/2;
                UIComponents.drawCenteredText(g2,"Pick Your showtime!", x, titleY,
                        UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

                // ===== QUESTION =====
                int qY = titleY + 34;
                UIComponents.drawCenteredText(g2,"When would you like to watch this movie today?", x, qY,
                        UIComponents.FONT_SUBTITLE, UIComponents.TEXT_WHITE);

                // ===== TIME GRID (2 cols, 2 rows) =====
                int topY = qY + 35;
                layoutCardsGrid(timeCards, w, topY, 2);

                for (Card c : timeCards) {
                    boolean selected = (selectedSlot == c);
                    UIComponents.drawSelectableCard(g2, c.rect, c.icon, c.label, selected);
                }

                // ===== NEXT BUTTON =====
                int btnW = 300, btnH = 55, btnX = (w - btnW) / 2, btnY = 600;
                nextBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
                UIComponents.drawPrimaryButton(g2, nextBtnRect, "Book Now!", nextPressed);
            }
        }

        // HELPERS
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
        // DATA CLASS
        static class Card {
            String label, icon, key;
            Rectangle rect;

            Card(String label, String icon, String key) {
                this.label = label;
                this.icon = icon;
                this.key = key;
            }
        }
}
