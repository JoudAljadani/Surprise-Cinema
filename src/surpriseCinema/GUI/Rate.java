package surpriseCinema.GUI;

import surpriseCinema.App.Appframe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;

public class Rate extends JPanel {

    private final Appframe app;
    private final Image bg;

    private Rectangle backBtnRect;
    private Rectangle submitBtnRect;

    private Rectangle[] starRects = new Rectangle[5];

    private boolean backPressed;
    private boolean submitPressed;

    private int rating = 0; // selected rating 1..5

    public Rate(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();

        setLayout(new BorderLayout());
        add(new RatePanel(), BorderLayout.CENTER);
    }

    class RatePanel extends JPanel {

        RatePanel() {


            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    if (backBtnRect != null && backBtnRect.contains(e.getPoint())) backPressed = true;
                    if (submitBtnRect != null && submitBtnRect.contains(e.getPoint())) submitPressed = true;

                    // Select rating by clicking stars (no hover effect)
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

                    if (backPressed && backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOMEPAGE);
                    }

                    // No popup, just go home on submit
                    if (submitPressed && submitBtnRect != null && submitBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOMEPAGE);
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
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Background
            g2.drawImage(bg, 0, 0, w, h, null);

            // Back arrow (top-left)
            int backSize = 36, backX = 18, backY = 32;
            backBtnRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backBtnRect, backPressed);

            // Title
            UIComponents.drawCenteredText(g2, "Rate Movie", w / 2, 56,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            // Card
            int cardX = 28;
            int cardY = 120;
            int cardW = w - 56;
            int cardH = 420;

            g2.setColor(new Color(0,0,0,50));
            g2.fillRoundRect(cardX + 3, cardY + 5, cardW, cardH, 24, 24);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 24, 24);

            g2.setColor(new Color(220,220,220));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(cardX, cardY, cardW, cardH, 24, 24);

            // Poster placeholder box (no image)
            int posterX = cardX + 20;
            int posterY = cardY + 18;
            int posterW = cardW - 40;
            int posterH = 170;

// box background (optional - خليها أبيض زي داخل الكارد)
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(posterX, posterY, posterW, posterH, 14, 14);

// border
            g2.setColor(new Color(200,200,200));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(posterX, posterY, posterW, posterH, 14, 14);

// centered text
            UIComponents.drawCenteredText(g2, "Movie Poster", w / 2,
                    posterY + posterH / 2 + 6, UIComponents.FONT_BODY, UIComponents.TEXT_BLACK);

// New anchor line after poster box
            int movieY = posterY + posterH + 35;  // keep same variable for the rest
            // Instruction
            UIComponents.drawCenteredText(g2, "Tap a star to rate", w / 2,
                    movieY + 22, UIComponents.FONT_SMALL, UIComponents.TEXT_GRAY);

            // Stars row
            int starSize = 44;
            int gap = 14;
            int totalStarsW = starSize * 5 + gap * 4;
            int startX = (w - totalStarsW) / 2;
            int starsY = movieY + 55;

            for (int i = 0; i < 5; i++) {
                int sx = startX + i * (starSize + gap);

                // Click area
                starRects[i] = new Rectangle(sx, starsY - starSize + 10, starSize, starSize);

                boolean filled = (i + 1) <= rating;

                drawStar(g2, sx + starSize / 2, starsY, starSize / 2.1, starSize / 4.2, filled);
            }
            // Submit button
            int btnW = 220, btnH = 45;
            int btnX = (w - btnW) / 2;
            int btnY = starsY + 60;
            submitBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawPrimaryButton(g2, submitBtnRect, "Submit Rating", submitPressed);
        }

        // Simple star drawing
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