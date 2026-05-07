import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PreferencesCountries extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image
    private final Image logo;//Logo image

    //Clickable buttons
    private Rectangle saveBtnRect;
    private boolean savePressed = false;

    private Rectangle backArrowRect;
    private boolean backArrowPressed = false;

    private final Set<String> selectedCountries = new HashSet<>();

    private final Card[] cards = new Card[]{
            new Card("United States", "🇺🇸", "USA"),
            new Card("Turkey", "🇹🇷", "TURKEY"),
            new Card("Korea", "🇰🇷", "KOREA"),
            new Card("United Kingdom", "🇬🇧", "UK"),
            new Card("Italy", "🇮🇹", "ITALY"),
            new Card("Spain", "🇪🇸", "SPAIN")
    };

//------------------------------------------------------------

    public PreferencesCountries(Appframe app) {
        this.app = app;

        //Background
        bg = new ImageIcon("resources/images/Background.png").getImage();
        //Logo
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new Panel(), BorderLayout.CENTER);
    }

    class Panel extends JPanel {
        Panel() {
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    //if back arrow is pressed
                    if (backArrowRect != null && backArrowRect.contains(e.getPoint())) {
                        backArrowPressed = true;
                        repaint();
                        return;
                    }

                    //if cards are pressed
                    for (Card c : cards) {
                        if (c.rect != null && c.rect.contains(e.getPoint())) {
                            if (!selectedCountries.add(c.key)) selectedCountries.remove(c.key);
                            repaint();
                            return;
                        }
                    }

                    //if save button is pressed
                    if (saveBtnRect != null && saveBtnRect.contains(e.getPoint())) {
                        savePressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    //if back, navigate to preferences genres page
                    if (backArrowPressed && backArrowRect != null && backArrowRect.contains(e.getPoint())) {
                        app.showPage(Appframe.PREFERENCES_GENRES);
                    }

                    //if save, navigate to home page
                    if (savePressed && saveBtnRect != null && saveBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
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
        //Draw UI elements
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            //Make corner smoother
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Get panel width and height
            int w = getWidth(), h = getHeight();

            //Background
            g2.drawImage(bg, 0, 0, w, h, null);

            //Back arrow
            int backSize = 36, backX = 18, backY = 32;
            backArrowRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backArrowRect, backArrowPressed);

            //Logo
            int logoSize = 200;
            int logoX = (w - logoSize) / 2;
            int logoY = 0;
            g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);

            //Title
            int titleY = logoY + logoSize + 26;
            UIComponents.drawTitle(g2, w, titleY);

            //Question
            int cx = w / 2;
            int qY = titleY + 43;
            UIComponents.drawCenteredText(g2, "Preferred movie country?", cx, qY,
                    UIComponents.FONT_SUBTITLE, UIComponents.TEXT_WHITE);

            //Cards (countries)
            int cardsTopY = qY + 34;
            layoutCardsGrid(cards, w, cardsTopY, 2);
            for (Card c : cards) {
                boolean selected = selectedCountries.contains(c.key);
                UIComponents.drawSelectableCard(g2, c.rect, c.icon, c.label, selected);
            }

            //Save button
            int btnW = 300, btnH = 55, btnX = (w - btnW) / 2, btnY = 600;
            saveBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, saveBtnRect, "Save", savePressed);
        }
    }

    //Lays out the card rectangles in a grid
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

    //Data class for each country
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