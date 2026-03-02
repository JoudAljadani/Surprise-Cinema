package GUI;

import App.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class PreferencesGenres extends JPanel {

    private final Appframe app;
    private final Image bg;
    private final Image logo;

    private Rectangle nextBtnRect;
    private boolean nextPressed = false;

    // ONLY UI state (for colors)
    private final Set<String> selectedGenres = new HashSet<>();

    private final Card[] cards = new Card[]{
            new Card("Comedy", "😂", "COMEDY"),
            new Card("Drama", "😢", "DRAMA"),
            new Card("Romance", "❤️", "ROMANCE"),
            new Card("Action", "💣", "ACTION"),
            new Card("Horror", "👻", "HORROR"),
            new Card("Fantasy", "🧙‍♂️", "FANTASY")
    };

    public PreferencesGenres(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        setLayout(new BorderLayout());
        add(new Panel(), BorderLayout.CENTER);
    }

    class Panel extends JPanel {
        Panel() {
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    // cards toggle (for color only)
                    for (Card c : cards) {
                        if (c.rect != null && c.rect.contains(e.getPoint())) {
                            if (!selectedGenres.add(c.key)) selectedGenres.remove(c.key);
                            repaint();
                            return;
                        }
                    }

                    // next
                    if (nextBtnRect != null && nextBtnRect.contains(e.getPoint())) {
                        nextPressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (nextPressed && nextBtnRect != null && nextBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.PREFERENCES_COUNTRIES);
                    }
                    nextPressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    nextPressed = false;
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

            // background
            g2.drawImage(bg, 0, 0, w, h, null);

            // logo
            int logoSize = 200;
            int logoX = (w - logoSize) / 2;
            int logoY = 0;
            g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);

            // brand title
            int titleY = logoY + logoSize + 26;
            UIComponents.drawBrandTitle(g2, w, titleY);

            // subtitle/question
            int cx = w / 2;
            int qY = titleY + 43;
            UIComponents.drawCenteredText(g2, "Preferred movie genre?", cx, qY,
                    UIComponents.FONT_SUBTITLE, UIComponents.TEXT_WHITE);

            // cards
            int cardsTopY = qY + 34;
            layoutCardsGrid(cards, w, cardsTopY, 2);

            for (Card c : cards) {
                boolean selected = selectedGenres.contains(c.key);
                UIComponents.drawSelectableCard(g2, c.rect, c.icon, c.label, selected);
            }

            // next button
            int btnW = 300, btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 600;

            nextBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawPrimaryButton(g2, nextBtnRect, "Next", nextPressed);
        }
    }

    // helpers
    private void layoutCardsGrid(Card[] cards, int panelW, int topY, int cols) {
        int cardW = 140, cardH = 82;
        int gapX = 16, gapY = 14;

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