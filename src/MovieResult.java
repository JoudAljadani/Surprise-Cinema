import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MovieResult extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image

    //Clickable buttons
    private Rectangle otherBtnRect;
    private Rectangle acceptBtnRect;

    private boolean otherPressed = false;
    private boolean acceptPressed = false;

    //Back arrow button
    private Rectangle backRect;
    private boolean backPressed = false;

//------------------------------------------------------------

    //Mock Movie (just for example in the GUI)
    private final Movie[] movies = new Movie[]{
            new Movie("Interstellar", "Sci-Fi / Drama", "PG-13", "8.6",
                    "A journey beyond the stars to save humanity."),
            new Movie("Parasite", "Thriller / Drama", "R", "8.5",
                    "A poor family infiltrates a wealthy household."),
            new Movie("Spirited Away", "Animation / Fantasy", "PG", "8.6",
                    "A girl enters a mysterious spirit world."),
            new Movie("The Dark Knight", "Action / Crime", "PG-13", "9.0",
                    "Batman faces chaos in Gotham City."),
            new Movie("La La Land", "Romance / Music", "PG-13", "8.0",
                    "Love and dreams collide in Los Angeles."),
            new Movie("The Conjuring", "Horror", "R", "7.5",
                    "Paranormal investigators face a terrifying case.")
    };

    private final Random rnd = new Random();//Random for choosing a movie index
    private int currentIndex = 0;

    public MovieResult(Appframe app) {
        this.app = app;

        //Background
        bg = new ImageIcon("resources/images/Background.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new MoviePanel(), BorderLayout.CENTER);

        //Start with random movie
        pickAnotherMovie();
    }

    private void pickAnotherMovie() {//To pick another movie (in a random way)
        int next = rnd.nextInt(movies.length);
        if (movies.length > 1) {
            while (next == currentIndex) next = rnd.nextInt(movies.length);
        }
        currentIndex = next;
        repaint();//Refresh after changing selection
    }

    class MoviePanel extends JPanel {

        MoviePanel() {

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {

                    //if back arrow is pressed
                    if (backRect != null && backRect.contains(e.getPoint())) {
                        backPressed = true;
                        repaint();
                        return;
                    }

                    //if other movie button is pressed
                    if (otherBtnRect != null && otherBtnRect.contains(e.getPoint())) {
                        otherPressed = true;
                        repaint();
                        return;
                    }

                    //if accept movie button is pressed
                    if (acceptBtnRect != null && acceptBtnRect.contains(e.getPoint())) {
                        acceptPressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    //if back arrow, navigate to homePage
                    if (backPressed && backRect != null && backRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
                    }
                    backPressed = false;

                    //if other button, call another movie and refresh the page
                    if (otherPressed && otherBtnRect != null && otherBtnRect.contains(e.getPoint())) {
                        pickAnotherMovie();
                    }

                    //if accept movie, navigate to choose time page
                    if (acceptPressed && acceptBtnRect != null && acceptBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.CHOOSE_TIME);
                    }

                    otherPressed = false;
                    acceptPressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    otherPressed = false;
                    acceptPressed = false;
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

            //Background
            g2.drawImage(bg, 0, 0, w, h, null);

            //Back arrow
            int backSize = 36, backX = 18, backY = 32;
            backRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backRect, backPressed);

            //Title
            int titleY = 80, x = w / 2;
            UIComponents.drawCenteredText(g2,"Your surprise movie is", x, titleY,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            //movie box
            int boxW = 250, boxH = 240, boxX = (w - boxW) / 2, boxY = titleY + 22;
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 22, 22);

            //Poster placeholder text
            UIComponents.drawCenteredText(g2,"Movie Poster", x, boxY + boxH / 2 + 6,
                    UIComponents.FONT_BODY, UIComponents.TEXT_BLACK);

            //Movie information
            Movie m = movies[currentIndex];

            int infoX = 60, infoW = w - 2 * infoX, topY = boxY + boxH + 28;

            //Movie name
            UIComponents.drawCenteredText(g2, m.name, x, topY,
                    new Font("Arial", Font.BOLD, 20), UIComponents.TEXT_WHITE);

            //Story
            int storyY = topY + 34;
            g2.setFont(UIComponents.FONT_BODY);
            g2.setColor(UIComponents.TEXT_WHITE);
            int storyEndY = drawWrappedReturnEndY(g2, m.story, infoX, storyY, infoW, 18);

            //Genre, Rated, IMDb
            int metaY = storyEndY + 22;
            g2.setFont(UIComponents.FONT_BODY);
            g2.setColor(UIComponents.TEXT_WHITE_SOFT);
            g2.drawString("Genre: " + m.genre, infoX, metaY);
            g2.drawString("Rated: " + m.rated, infoX, metaY + 20);
            g2.drawString("IMDb: " + m.imdb, infoX, metaY + 40);

            //Other and accept buttons
            int btnW = 145, btnH = 55, gap = 14, btnY = 600;
            int total = btnW * 2 + gap, startX = (w - total) / 2;

            otherBtnRect = new Rectangle(startX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, otherBtnRect, "Other", otherPressed);

            acceptBtnRect = new Rectangle(startX + btnW + gap, btnY, btnW, btnH);
            UIComponents.drawButton(g2, acceptBtnRect, "Accept", acceptPressed);
        }

        //Splits the text into words and displays it line by line
        private int drawWrappedReturnEndY(Graphics2D g2, String text, int x, int y, int maxW, int lineH) {
            FontMetrics fm = g2.getFontMetrics();
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            int yy = y;

            for (String w : words) {
                String test = line.isEmpty() ? w : line + " " + w;
                if (fm.stringWidth(test) > maxW) {
                    g2.drawString(line.toString(), x, yy);
                    line = new StringBuilder(w);
                    yy += lineH;
                } else {
                    line = new StringBuilder(test);
                }
            }
            if (!line.isEmpty()) {//Remaining text
                g2.drawString(line.toString(), x, yy);
            }
            return yy;//Last line
        }
    }

    //Temp data class
    static class Movie {
        String name, genre, rated, imdb, story;
        Movie(String name, String genre, String rated, String imdb, String story) {
            this.name = name;
            this.genre = genre;
            this.rated = rated;
            this.imdb = imdb;
            this.story = story;
        }
    }
}

