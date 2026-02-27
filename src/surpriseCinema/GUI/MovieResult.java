package surpriseCinema.GUI;

import app.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MovieResult extends JPanel {

    private final Appframe app;

    private final Image bg;

    private final Color subtleStroke = new Color(242, 242, 242);

    // clickable areas
    private Rectangle otherBtnRect;
    private Rectangle acceptBtnRect;

    private boolean otherHover = false;
    private boolean otherPressed = false;

    private boolean acceptHover = false;
    private boolean acceptPressed = false;

    // ===== MOCK MOVIES (GUI ONLY) =====
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

    private final Random rnd = new Random();
    private int currentIndex = 0;

    public MovieResult(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();

        setLayout(new BorderLayout());
        add(new MoviePanel(), BorderLayout.CENTER);

        // start with random movie
        pickAnotherMovie();
    }

    private void pickAnotherMovie() {
        int next = rnd.nextInt(movies.length);
        if (movies.length > 1) {
            while (next == currentIndex) next = rnd.nextInt(movies.length);
        }
        currentIndex = next;
        repaint();
    }

    // ===========================
    // MAIN PANEL
    // ===========================
    class MoviePanel extends JPanel {

        MoviePanel() {

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {

                    otherHover = (otherBtnRect != null && otherBtnRect.contains(e.getPoint()));
                    acceptHover = (acceptBtnRect != null && acceptBtnRect.contains(e.getPoint()));

                    boolean hand = otherHover || acceptHover;
                    setCursor(new Cursor(hand ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {

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

                    // LEFT: OTHER -> new movie
                    if (otherPressed && otherBtnRect != null && otherBtnRect.contains(e.getPoint())) {
                        pickAnotherMovie();
                    }

                    // RIGHT: ACCEPT -> next page
                    if (acceptPressed && acceptBtnRect != null && acceptBtnRect.contains(e.getPoint())) {
                        //will continue when next page create
                    }

                    otherPressed = false;
                    acceptPressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    otherHover = false;
                    acceptHover = false;
                    otherPressed = false;
                    acceptPressed = false;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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

            // ===== PAGE TITLE =====
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 22));

            String pageTitle = "Your surprise movie is";
            FontMetrics fmT = g2.getFontMetrics();

            int titleX = (w - fmT.stringWidth(pageTitle)) / 2;
            int titleY = 80;

            g2.drawString(pageTitle, titleX, titleY);

            // ===== BIG MOVIE BOX (poster placeholder) =====
            int boxW = 250;
            int boxH = 240;
            int boxX = (w - boxW) / 2;
            int boxY = titleY + 22;

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 22, 22);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(220, 220, 220));
            g2.drawRoundRect(boxX, boxY, boxW, boxH, 22, 22);

            // poster placeholder text
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String ph = "Movie Poster";
            FontMetrics fmPH = g2.getFontMetrics();
            int phX = boxX + (boxW - fmPH.stringWidth(ph)) / 2;
            int phY = boxY + boxH / 2;
            g2.drawString(ph, phX, phY);

            // ===== MOVIE INFO =====
            Movie m = movies[currentIndex];

            int infoX = 60;
            int infoW = w - 2 * infoX;
            int topY = boxY + boxH + 28;

// 1) Movie name centered
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics fmName = g2.getFontMetrics();
            int nameX = (w - fmName.stringWidth(m.name)) / 2;
            g2.drawString(m.name, nameX, topY);

// 2) Story left aligned under it
            int storyY = topY + 34;
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.setColor(Color.WHITE);

            int storyEndY = drawWrappedReturnEndY(
                    g2,
                    m.story,
                    infoX,
                    storyY,
                    infoW,
                    18
            );


// 3) Genre / Rated / IMDb lighter under story
            int metaY = storyEndY + 22;

            g2.setFont(new Font("Arial", Font.PLAIN, 13)); // أخف
            g2.setColor(new Color(255, 255, 255, 210));    // أبيض أخف شوي (شفافية)

            g2.drawString("Genre: " + m.genre, infoX, metaY);
            g2.drawString("Rated: " + m.rated, infoX, metaY + 20);
            g2.drawString("IMDb: " + m.imdb, infoX, metaY + 40);

            // ===== BUTTONS (BOTTOM) =====
            int btnW = 145;
            int btnH = 55;
            int gap = 14;

            int total = btnW * 2 + gap;
            int startX = (w - total) / 2;
            int btnY = 600;

            otherBtnRect = new Rectangle(startX, btnY, btnW, btnH);
            acceptBtnRect = new Rectangle(startX + btnW + gap, btnY, btnW, btnH);

            drawButton(g2, otherBtnRect, "Other", otherHover, otherPressed);
            drawButton(g2, acceptBtnRect, "Accept", acceptHover, acceptPressed);
        }

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

            if (!line.isEmpty()) {
                g2.drawString(line.toString(), x, yy);
            }

            return yy; // last line
        }
    }

    // ===========================
    // BUTTON (same style)
    // ===========================
    private void drawButton(Graphics2D g2, Rectangle r, String text, boolean hover, boolean pressed) {

        int radius = 50;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        if (hover) {
            g2.setColor(new Color(255, 0, 0, 18));
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
        }

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

