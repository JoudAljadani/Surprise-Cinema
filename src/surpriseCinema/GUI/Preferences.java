package surpriseCinema.GUI;

import surpriseCinema.App.Appframe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class Preferences extends JPanel {

    private final Appframe app;

    private final Image bg;
    private final Image logo;

    private final SelectionModel model = new SelectionModel();
    private final CardLayout layout = new CardLayout();
    private final JPanel pages = new JPanel(layout);

    private static final String GENRES = "GENRES";
    private static final String COUNTRIES = "COUNTRIES";

    public Preferences(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        pages.add(new GenresPanel(), GENRES);
        pages.add(new CountriesPanel(), COUNTRIES);

        setLayout(new BorderLayout());
        add(pages, BorderLayout.CENTER);

        showGenres();
    }

    private void showGenres() { layout.show(pages, GENRES); }
    private void showCountries() { layout.show(pages, COUNTRIES); }

    // PAGE 1: GENRES
    class GenresPanel extends JPanel {

        private Rectangle nextBtnRect;
        private boolean btnPressed = false;

        private final Card[] cards = new Card[]{
                new Card("Comedy", "😂", "COMEDY"),
                new Card("Drama", "😢", "DRAMA"),
                new Card("Romance", "❤️", "ROMANCE"),
                new Card("Action", "💣", "ACTION"),
                new Card("Horror", "👻", "HORROR"),
                new Card("Fantasy", "🧙‍♂️", "FANTASY")
        };

        GenresPanel() {

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    for (Card c : cards) {
                        if (c.rect != null && c.rect.contains(e.getPoint())) {
                            model.toggleGenre(c.key);
                            repaint();
                            return;
                        }
                    }

                    if (nextBtnRect != null && nextBtnRect.contains(e.getPoint())) {
                        btnPressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (btnPressed && nextBtnRect != null && nextBtnRect.contains(e.getPoint())) {
                        showCountries();
                    }
                    btnPressed = false;
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

            g2.drawImage(bg, 0, 0, w, getHeight(), null);

            int logoSize = 200;
            int logoX = (w - logoSize) / 2;
            int logoY = 0;
            g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);

            int titleY = logoY + logoSize + 26;
            UIComponents.drawBrandTitle(g2, w, titleY);

            int x = w / 2, y = titleY + 43;

            UIComponents.drawCenteredText(g2, "Preferred movie genre?", x, y,
                    UIComponents.FONT_SUBTITLE, UIComponents.TEXT_WHITE);

            int cardsTopY = y + 34;
            layoutCardsGrid(cards, w, cardsTopY, 2);

            for (Card c : cards) {
                boolean selected = model.isGenreSelected(c.key);
                UIComponents.drawSelectableCard(g2, c.rect, c.icon, c.label, selected);
            }

            int btnW = 300;
            int btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 600;

            nextBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawPrimaryButton(g2, nextBtnRect, "Next", btnPressed);        }
    }

    // PAGE 2: COUNTRIES
    class CountriesPanel extends JPanel {

        private Rectangle saveBtnRect;
        private Rectangle backBtnRect;

        private boolean savePressed = false;
        private boolean backPressed = false;

        private final Card[] cards = new Card[]{
                new Card("United States", "🇺🇸", "USA"),
                new Card("Turkey", "🇹🇷", "TURKEY"),
                new Card("Korea", "🇰🇷", "KOREA"),
                new Card("United Kingdom", "🇬🇧", "UK"),
                new Card("Italy", "🇮🇹", "ITALY"),
                new Card("Spain", "🇪🇸", "SPAIN")
        };

        CountriesPanel() {

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    for (Card c : cards) {
                        if (c.rect != null && c.rect.contains(e.getPoint())) {
                            model.toggleCountry(c.key);
                            repaint();
                            return;
                        }
                    }

                    if (backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        backPressed = true;
                        repaint();
                    }

                    if (saveBtnRect != null && saveBtnRect.contains(e.getPoint())) {
                        savePressed = true;
                        repaint();
                        return;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    if (backPressed && backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        showGenres();
                    }

                    if (savePressed && saveBtnRect != null && saveBtnRect.contains(e.getPoint())) {
                        // save logic
                        app.showPage(Appframe.HOMEPAGE);
                    }
                    savePressed = false;
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

            g2.drawImage(bg, 0, 0, w, getHeight(), null);

            int logoSize = 200;
            int logoX = (w - logoSize) / 2;
            int logoY = 0;
            g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            String title = "SURPRISE CINEMA";
            FontMetrics fmT = g2.getFontMetrics();
            int titleX = (w - fmT.stringWidth(title)) / 2;
            int titleY = logoY + logoSize + 26;
            g2.drawString(title, titleX, titleY);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 19));
            String subtitle = "Preferred movie country?";
            FontMetrics fmS = g2.getFontMetrics();
            int subX = (w - fmS.stringWidth(subtitle)) / 2;
            int subY = titleY + 43;
            g2.drawString(subtitle, subX, subY);

            int cardsTopY = subY + 34;
            layoutCardsGrid(cards, w, cardsTopY, 2);

            for (Card c : cards) {
                boolean selected = model.isCountrySelected(c.key);
                UIComponents.drawSelectableCard(g2, c.rect, c.icon, c.label, selected);
            }

            int btnW = 145;
            int btnH = 55;
            int gap = 14;

            int total = btnW * 2 + gap;
            int startX = (w - total) / 2;
            int btnY = 600;

            backBtnRect = new Rectangle(startX, btnY, btnW, btnH);
            saveBtnRect = new Rectangle(startX + btnW + gap, btnY, btnW, btnH);

            UIComponents.drawPrimaryButton(g2, backBtnRect, "Back", backPressed);
            UIComponents.drawPrimaryButton(g2, saveBtnRect, "Save", savePressed);
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


    static class Card {
        String label, icon, key;
        Rectangle rect;

        Card(String label, String icon, String key) {
            this.label = label;
            this.icon = icon;
            this.key = key;
        }
    }

    static class SelectionModel {
        private final Set<String> genres = new HashSet<>();
        private final Set<String> countries = new HashSet<>();

        void toggleGenre(String g) { if (!genres.add(g)) genres.remove(g); }
        void toggleCountry(String c) { if (!countries.add(c)) countries.remove(c); }

        boolean isGenreSelected(String g) { return genres.contains(g); }
        boolean isCountrySelected(String c) { return countries.contains(c); }
    }
}