package GUI;
import codeImplementation.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MovieResult extends JPanel {

    private final Appframe app;
    private final Image bg;

    private Rectangle otherBtnRect;
    private Rectangle acceptBtnRect;

    private boolean otherPressed = false;
    private boolean acceptPressed = false;

    private Rectangle backRect;
    private boolean backPressed = false;

    private Movie currentMovie;

    public MovieResult(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();

        setLayout(new BorderLayout());
        add(new MoviePanel(), BorderLayout.CENTER);

        pickAnotherMovie();
    }

    //shoud seperate
    public void pickAnotherMovie() {

        if (Appframe.currentUser == null) {
            currentMovie = null;
            repaint();
            return;
        }

        currentMovie = DatabaseQueries.getRandomMovieByUserPreferences(
                Appframe.currentUser.getEmail()
        );

        repaint();
    }

    class MoviePanel extends JPanel {

        MoviePanel() {

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    if (backRect != null && backRect.contains(e.getPoint())) {
                        backPressed = true;
                        repaint();
                        return;
                    }

                    if (otherBtnRect != null && otherBtnRect.contains(e.getPoint())) {
                        otherPressed = true;
                        repaint();
                        return;
                    }

                    if (acceptBtnRect != null && acceptBtnRect.contains(e.getPoint())) {
                        acceptPressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    if (backPressed && backRect != null && backRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
                    }

                    if (otherPressed && otherBtnRect != null && otherBtnRect.contains(e.getPoint())) {
                        pickAnotherMovie();
                    }

                    if (acceptPressed && acceptBtnRect != null && acceptBtnRect.contains(e.getPoint())) {
                        Appframe.currentMovie = currentMovie;
                        app.showPage(Appframe.CHOOSE_TIME);
                    }

                    backPressed = false;
                    otherPressed = false;
                    acceptPressed = false;

                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backPressed = false;
                    otherPressed = false;
                    acceptPressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            g2.drawImage(bg, 0, 0, w, h, null);

            int backSize = 36;
            int backX = 18;
            int backY = 32;

            backRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backRect, backPressed);

            int titleY = 80;
            int x = w / 2;

            UIComponents.drawCenteredText(g2, "Your surprise movie is", x, titleY,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            int boxW = 250;
            int boxH = 300;
            int boxX = (w - boxW) / 2;
            int boxY = titleY + 22;

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 22, 22);

            Movie m = currentMovie;

            if (m == null) {
                UIComponents.drawCenteredText(g2, "There is no movie available", x, boxY + boxH / 2,
                        UIComponents.FONT_BODY, UIComponents.TEXT_BLACK);

                UIComponents.drawCenteredText(g2, "for this genre tomorrow...", x, boxY + boxH / 2 + 22,
                        UIComponents.FONT_BODY, UIComponents.TEXT_BLACK);
                return;
            }

            if (m.getPosterImage() != null) {
                g2.drawImage(m.getPosterImage(), boxX + 15, boxY + 15,
                        boxW - 30, boxH - 30, null);
            } else {
                UIComponents.drawCenteredText(g2, "No Poster", x, boxY + boxH / 2,
                        UIComponents.FONT_BODY, UIComponents.TEXT_BLACK);
            }

            int infoX = 60;
            int infoW = w - 2 * infoX;
            int topY = boxY + boxH + 35;

            UIComponents.drawCenteredText(g2, m.getName(), x, topY,
                    new Font("Arial", Font.BOLD, 20), UIComponents.TEXT_WHITE
            );

            int storyY = topY + 34;

            g2.setFont(UIComponents.FONT_BODY);
            g2.setColor(UIComponents.TEXT_WHITE);

            int storyEndY = drawWrappedReturnEndY(g2, m.getStory(), infoX, storyY,
                    infoW, 18);

            int metaY = storyEndY + 22;

            g2.setFont(UIComponents.FONT_BODY);
            g2.setColor(UIComponents.TEXT_WHITE_SOFT);

            g2.drawString(
                    "Genre: " + m.getGenre(),
                    infoX,
                    metaY
            );

            g2.drawString(
                    "Duration: " + m.getDuration(),
                    infoX,
                    metaY + 20
            );

            g2.drawString(
                    "Rating: " + m.getRating(),
                    infoX,
                    metaY + 40
            );

            int btnW = 145;
            int btnH = 55;
            int gap = 14;
            int btnY = 600;

            int total = btnW * 2 + gap;
            int startX = (w - total) / 2;

            otherBtnRect = new Rectangle(startX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, otherBtnRect, "Other", otherPressed);

            acceptBtnRect = new Rectangle(startX + btnW + gap, btnY, btnW, btnH);
            UIComponents.drawButton(g2, acceptBtnRect, "Accept", acceptPressed);
        }

        private int drawWrappedReturnEndY(
                Graphics2D g2,
                String text,
                int x,
                int y,
                int maxW,
                int lineH
        ) {

            FontMetrics fm = g2.getFontMetrics();

            if (text == null || text.isEmpty() || text.equals("N/A")) {
                text = "No overview available.";
            }

            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();

            int yy = y;

            for (String word : words) {

                String test =
                        line.isEmpty()
                                ? word
                                : line + " " + word;

                if (fm.stringWidth(test) > maxW) {

                    g2.drawString(line.toString(), x, yy);

                    line = new StringBuilder(word);

                    yy += lineH;

                } else {

                    line = new StringBuilder(test);
                }
            }

            if (!line.isEmpty()) {
                g2.drawString(line.toString(), x, yy);
            }

            return yy;
        }
    }
}