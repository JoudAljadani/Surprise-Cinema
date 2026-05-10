package GUI;
import codeImplementation.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;

public class Rate extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image

    //Clickable buttons
    private Rectangle backBtnRect;
    private Rectangle submitBtnRect;

    private boolean backPressed;
    private boolean submitPressed;

    //To draw the rate stars
    private Rectangle[] starRects = new Rectangle[5];

    //Selected rate
    private int rating = 0;

//------------------------------------------------------------

    public Rate(Appframe app) {
        this.app = app;

        //Background
        bg = new ImageIcon("resources/images/Background.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new RatePanel(), BorderLayout.CENTER);
    }

    class RatePanel extends JPanel {

        RatePanel() {

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    //if back button is pressed
                    if (backBtnRect != null && backBtnRect.contains(e.getPoint())) backPressed = true;

                    //if submit button is pressed
                    if (submitBtnRect != null && submitBtnRect.contains(e.getPoint())) submitPressed = true;

                    //Select rating by clicking stars
                    for (int i = 0; i < 5; i++) {
                        if (starRects[i] != null && starRects[i].contains(e.getPoint())) {
                            rating = i + 1;
                            break;
                        }
                    }
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    //if back, navigate to homepage
                    if (backPressed && backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
                    }

                    //if submit button, save rating then navigate to homepage
                    if (submitPressed && submitBtnRect != null && submitBtnRect.contains(e.getPoint())) {

                        boolean saved = AppManager.submitRating(rating);

                        if (saved) {
                            JOptionPane.showMessageDialog(null, "Rating submitted successfully");
                            app.showPage(Appframe.HOME_PAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Please book a ticket and select a rating first");
                        }
                    }
                    backPressed = false;
                    submitPressed = false;
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    backPressed = false;
                    submitPressed = false;
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

            if (Appframe.currentTicket == null && Appframe.currentUser != null) {
                Appframe.currentTicket = DatabaseQueries.getLatestTicketByEmail(Appframe.currentUser.getEmail());
            }
            //Background
            g2.drawImage(bg, 0, 0, w, h, null);

            //Back arrow
            int backSize = 36, backX = 18, backY = 32;
            backBtnRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backBtnRect, backPressed);

            //Title
            UIComponents.drawCenteredText(g2, "Rate Movie", w / 2, 56,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            //Big Card
            int cardX = 28, cardY = 120, cardW = w - 56, cardH = 420;
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 24, 24);

            //Movie poster placeholder box
            int posterX = cardX + 20, posterY = cardY + 18, posterW = cardW - 40, posterH = 170;
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(posterX, posterY, posterW, posterH, 14, 14);

            if (Appframe.currentTicket != null) {
                try {
                    String posterUrl = Appframe.currentTicket.getPosterUrl();

                    ImageIcon posterIcon = new ImageIcon(new java.net.URL(posterUrl));

                    Image posterImage = posterIcon.getImage();

                    g2.drawImage(posterImage, posterX, posterY, posterW, posterH, null);

                } catch (Exception e) {

                    UIComponents.drawCenteredText(g2, "Movie Poster", w / 2, posterY + posterH / 2 + 6,
                            UIComponents.FONT_BODY, UIComponents.TEXT_BLACK);
                }

            } else {

                UIComponents.drawCenteredText(g2, "Movie Poster", w / 2, posterY + posterH / 2 + 6,
                        UIComponents.FONT_BODY, UIComponents.TEXT_BLACK);
            }
            //"Tap to rate" text
            int movieY = posterY + posterH + 35;
            String movieText = "Tap a star to rate";

            if (Appframe.currentTicket != null) {
                movieText = "What do you think about " + Appframe.currentTicket.getMovieName() + "?";
            }

            UIComponents.drawCenteredText(g2, movieText, w / 2, movieY + 22,
                    UIComponents.FONT_SMALL, UIComponents.TEXT_GRAY);

            //Stars
            int starSize = 44;
            int gap = 14;
            int totalStarsW = starSize * 5 + gap * 4;
            int startX = (w - totalStarsW) / 2;
            int starsY = movieY + 55;

            for (int i = 0; i < 5; i++) {
                int sx = startX + i * (starSize + gap);
                starRects[i] = new Rectangle(sx, starsY - starSize + 10, starSize, starSize);//Click area
                boolean filled = (i + 1) <= rating;
                drawStar(g2, sx + starSize / 2, starsY, starSize / 2.1, starSize / 4.2, filled);
            }

            //Submit button
            int btnW = 220, btnH = 45, btnX = (w - btnW) / 2, btnY = starsY + 60;
            submitBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, submitBtnRect, "Submit Rating", submitPressed);
        }

        //Draw stars rating
        private void drawStar(Graphics2D g2, int cx, int cy, double outerR, double innerR, boolean filled) {

            Path2D star = new Path2D.Double();
            double angle = -Math.PI / 2;
            double step = Math.PI / 5;

            for (int i = 0; i < 10; i++) {
                double r = (i % 2 == 0) ? outerR : innerR;
                double x = cx + Math.cos(angle) * r;
                double y = cy + Math.sin(angle) * r;

                if (i == 0) star.moveTo(x, y);
                else star.lineTo(x, y);

                angle += step;
            }
            star.closePath();

            if (filled) {
                g2.setColor(new Color(255, 180, 0));
                g2.fill(star);
            } else {
                g2.setColor(new Color(200, 200, 200));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(star);
            }
        }
    }
}