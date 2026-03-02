package GUI;

import App.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class PreferencesCountries extends JPanel {

    private final Appframe app;
    private final Image bg;
    private final Image logo;

    private Rectangle saveBtnRect;

    // Back arrow (top-left)
    private Rectangle backArrowRect;
    private boolean backArrowPressed = false;

    private boolean savePressed = false;

    // ONLY UI state (for colors)
    private final Set<String> selectedCountries = new HashSet<>();

    private final Card[] cards = new Card[]{
            new Card("United States", "🇺🇸", "USA"),
            new Card("Turkey", "🇹🇷", "TURKEY"),
            new Card("Korea", "🇰🇷", "KOREA"),
            new Card("United Kingdom", "🇬🇧", "UK"),
            new Card("Italy", "🇮🇹", "ITALY"),
            new Card("Spain", "🇪🇸", "SPAIN")
    };

    public PreferencesCountries(Appframe app) {
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

                    // BACK ARROW
                    if (backArrowRect != null && backArrowRect.contains(e.getPoint())) {
                        backArrowPressed = true;
                        repaint();
                        return;
                    }

                    // cards toggle (for color only)
                    for (Card c : cards) {
                        if (c.rect != null && c.rect.contains(e.getPoint())) {
                            if (!selectedCountries.add(c.key)) selectedCountries.remove(c.key);
                            repaint();
                            return;
                        }
                    }

                    // save
                    if (saveBtnRect != null && saveBtnRect.contains(e.getPoint())) {
                        savePressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    // BACK ARROW ACTION
                    if (backArrowPressed && backArrowRect != null && backArrowRect.contains(e.getPoint())) {
                        app.showPage(Appframe.PREFERENCES_GENRES);
                    }

                    // SAVE ACTION
                    if (savePressed && saveBtnRect != null && saveBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE); // ✅ (كان عندك HOME_PAGE)
                    }

                    savePressed = false;
                    backArrowPressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    savePressed = false;
                    backArrowPressed = false;
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

            // BACK ARROW (Top Left) using UIComponents
            int backSize = 36, backX = 18, backY = 32;
            backArrowRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backArrowRect, backArrowPressed);

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
            UIComponents.drawCenteredText(g2, "Preferred movie country?", cx, qY,
                    UIComponents.FONT_SUBTITLE, UIComponents.TEXT_WHITE);

            // cards
            int cardsTopY = qY + 34;
            layoutCardsGrid(cards, w, cardsTopY, 2);

            for (Card c : cards) {
                boolean selected = selectedCountries.contains(c.key);
                UIComponents.drawSelectableCard(g2, c.rect, c.icon, c.label, selected);
            }

            // SAVE button only
            int btnW = 300, btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 600;

            saveBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawPrimaryButton(g2, saveBtnRect, "Save", savePressed);
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