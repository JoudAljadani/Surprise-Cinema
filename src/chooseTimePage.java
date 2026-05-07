import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class chooseTimePage extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image

    //Clickable buttons
    private Rectangle nextBtnRect;
    private boolean nextPressed = false;
    private Card selectedSlot = null;

    //Back arrow
    private Rectangle backRect;
    private boolean backPressed = false;

//------------------------------------------------------------

    //Time slots
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

        //Background
        bg = new ImageIcon("resources/images/Background.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new TimePanel(), BorderLayout.CENTER);
    }

    class TimePanel extends JPanel {

        TimePanel() {
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    //if back arrow is pressed
                    if (backRect != null && backRect.contains(e.getPoint())) {
                        backPressed = true;
                        repaint();
                        return;
                    }

                    //click one time cards (to avoid overlapping)
                    for (Card c : timeCards) {
                        if (c.rect != null && c.rect.contains(e.getPoint())) {
                            selectedSlot = c;
                            repaint();
                            return;
                        }
                    }

                    //if next button is pressed
                    if (nextBtnRect != null && nextBtnRect.contains(e.getPoint())) {
                        nextPressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //if back, navigate back to resultMovie page
                    if (backPressed && backRect != null && backRect.contains(e.getPoint())) {
                        app.showPage(Appframe.MOVIERESULT);
                    }
                    backPressed = false;

                    //if next button, navigate to ticket generation success page
                    if (nextPressed && nextBtnRect != null && nextBtnRect.contains(e.getPoint())) {
                        if (selectedSlot == null){//to force selection
                            nextPressed = false;
                            repaint();
                            return;
                        }
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
        //Draw UI elements
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            //Make corner smoother
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //Get panel width and height
            int w = getWidth(), h = getHeight();

            g2.drawImage(bg, 0, 0, w, h, null);//background

            //Back arrow
            int backSize = 36, backX = 18, backY = 32;
            backRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backRect, backPressed);

            //Title
            int titleY = 95, x = w/2;
            UIComponents.drawCenteredText(g2,"Pick Your showtime!", x, titleY,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            //Question
            int qY = titleY + 34;
            UIComponents.drawCenteredText(g2,"When would you like to watch this movie today?", x, qY,
                    UIComponents.FONT_SUBTITLE, UIComponents.TEXT_WHITE);

            //Time slots
            int topY = qY + 35;
            layoutCardsGrid(timeCards, w, topY, 2);
            for (Card c : timeCards) {
                boolean selected = (selectedSlot == c);
                UIComponents.drawSelectableCard(g2, c.rect, c.icon, c.label, selected);
            }

            //Next button
            int btnW = 300, btnH = 55, btnX = (w - btnW) / 2, btnY = 600;
            nextBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, nextBtnRect, "Book Now!", nextPressed);
        }
    }

    //Lays out the card rectangles in a grid
    private void layoutCardsGrid(Card[] cards, int panelW, int topY, int cols) {

        int cardW = 140, cardH = 82, gapX = 16, gapY = 14;
        int totalW = cols * cardW + (cols - 1) * gapX, startX = (panelW - totalW) / 2;

        //Assign rect for each card based on row and column
        for (int i = 0; i < cards.length; i++){
            int row = i / cols;
            int col = i % cols;
            int x = startX + col * (cardW + gapX);
            int y = topY + row * (cardH + gapY);
            cards[i].rect = new Rectangle(x, y, cardW, cardH);
        }
    }

    //Data class for each showtime
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
