package GUI;
import codeImplementation.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PreferencesGenres extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image
    private final Image logo;//Logo image

    //Clickable button
    private Rectangle saveBtnRect;
    private boolean savePressed = false;

    //Temporarily stores selected genres for UI
    private final Set<String> selectedGenres = new HashSet<>();

    //Preferences
    private final Card[] cards = new Card[]{
            new Card("Comedy", "😂", "COMEDY"),
            new Card("Drama", "😢", "DRAMA"),
            new Card("Romance", "❤️", "ROMANCE"),
            new Card("Action", "💣", "ACTION"),
            new Card("Horror", "👻", "HORROR"),
            new Card("Fantasy", "🧙‍♂️", "FANTASY")
    };

//------------------------------------------------------------

    public PreferencesGenres(Appframe app) {
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

                    //if save button is pressed
                    if (saveBtnRect != null && saveBtnRect.contains(e.getPoint())) {
                        savePressed = true;
                        repaint();
                    }

                    //if cards are pressed
                    for (Card c : cards) {
                        if (c.rect != null && c.rect.contains(e.getPoint())) {
                            if (!selectedGenres.add(c.key)) selectedGenres.remove(c.key);
                            repaint();
                            return;
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    //if the user clicks the save button
                    if (savePressed && saveBtnRect != null && saveBtnRect.contains(e.getPoint())) {

                        //Force at least one selection
                        if (selectedGenres.isEmpty()) {

                            JOptionPane.showMessageDialog(
                                    null,
                                    "Please select at least one genre"
                            );

                            savePressed = false;
                            repaint();
                            return;
                        }

                        //delete the oldest genres from the database before saving new ones
                        DatabaseQueries.deleteUserGenres(Appframe.currentUser.getEmail());

                        //Save genres in database
                        for (String genre : selectedGenres) {
                            DatabaseQueries.addUserGenre(Appframe.currentUser.getEmail(), genre);
                        }

                        UserPreferences.selectedGenres.clear(); //delete old saved genres
                        UserPreferences.selectedGenres.addAll(selectedGenres); //save genres to the shared user preferences
                        app.showPage(Appframe.HOME_PAGE); //move to home page
                    }
                    savePressed = false;
                    repaint(); //Refresh the UI
                }

                @Override
                //mouse leaves the panel
                public void mouseExited(MouseEvent e) {
                    savePressed = false;
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

            //Logo
            int logoSize = 200, logoX = (w - logoSize) / 2, logoY = 0;
            g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);

            //Title
            int titleY = logoY + logoSize + 26;
            UIComponents.drawTitle(g2, w, titleY);

            //Question
            int qx = w / 2, qY = titleY + 43;
            UIComponents.drawCenteredText(g2, "Preferred movie genre?", qx, qY,
                    UIComponents.FONT_SUBTITLE, UIComponents.TEXT_WHITE);

            //Cards
            int cardsTopY = qY + 34;
            layoutCardsGrid(cards, w, cardsTopY, 2);
            for (Card c : cards) {
                boolean selected = selectedGenres.contains(c.key);
                UIComponents.drawSelectableCard(g2, c.rect, c.icon, c.label, selected);
            }

            //Next button
            int btnW = 300, btnH = 55, btnX = (w - btnW) / 2, btnY = 600;
            saveBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, saveBtnRect, "Save", savePressed);
        }
    }

    //Lays out the card rectangles in a grid
    private void layoutCardsGrid(Card[] cards, int panelW, int topY, int cols) {

        int cardW = 140, cardH = 82; //w and h for each card
        int gapX = 16, gapY = 14; //gap between cards

        int totalW = cols * cardW + (cols - 1) * gapX; //Calculate the total width of all cards + gaps
        int startX = (panelW - totalW) / 2; //where the grid should start

        for (int i = 0; i < cards.length; i++) {
            int row = i / cols; //row the card belongs to
            int col = i % cols; //col the card belongs to

            int x = startX + col * (cardW + gapX); //X position
            int y = topY + row * (cardH + gapY);   //Y position

            cards[i].rect = new Rectangle(x, y, cardW, cardH); //Create a rectangle for the card

        }
    }

    //Data class for each genre
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