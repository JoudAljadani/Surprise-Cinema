package surpriseCinema.GUI;

import app.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Splash extends JPanel {

    private final Appframe app;

    private int phase = 0;
    private double time = 0;

    private float logoAlpha = 0f;
    private float titleAlpha = 0f;
    private float bottomAlpha = 0f;
    private float buttonAlpha = 0f;
    private float scale = 0.8f;

    private double logoY = 40;

    private final Image bg;
    private final Image logo;

    // clickable areas
    private Rectangle buttonRect;
    private Rectangle linkRect;

    private boolean hover = false;
    private boolean pressed = false;
    private boolean hoverLink = false;

    public Splash(Appframe app) {
        this.app = app;

        setFocusable(true);

        bg = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (buttonRect != null) hover = buttonRect.contains(e.getPoint());
                if (linkRect != null) hoverLink = linkRect.contains(e.getPoint());

                if (hoverLink || hover) setCursor(new Cursor(Cursor.HAND_CURSOR));
                else setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (buttonRect != null && buttonRect.contains(e.getPoint())) pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                // Get Started -> SignUp
                if (pressed && buttonRect != null && buttonRect.contains(e.getPoint())) {
                    app.showPage(Appframe.SIGNUP);
                }

                // I already have an account -> SignIn
                if (linkRect != null && linkRect.contains(e.getPoint())) {
                    app.showPage(Appframe.SIGNIN);
                }

                pressed = false;
                repaint();
            }
        });

        startAnimation();
    }

    private void startAnimation() {
        Timer timer = new Timer(16, e -> {

            time += 0.02;
            if (time > 1) time = 1;

            if (phase == 0) {
                logoAlpha = easeOut(time);
                scale = (float) lerp(0.8, 1.15, easeOut(time));
                logoY = lerp(40, -150, easeInOut(time));
                if (time >= 1) nextPhase();
            } else if (phase == 1) {
                scale = (float) lerp(1.15, 1.0, easeInOut(time));
                if (time > 0.4) titleAlpha = (float) ((time - 0.4) / 0.6);
                if (time >= 1) nextPhase();
            } else if (phase == 2) {
                bottomAlpha = easeOut(Math.min(1, time / 0.55));
                if (time > 0.65) buttonAlpha = easeOut((time - 0.65) / 0.35);
            }

            repaint();
        });

        timer.start();
    }

    private void nextPhase() {
        phase++;
        time = 0;
    }

    private double lerp(double a, double b, double p) {
        return a + (b - a) * p;
    }

    private float easeOut(double x) {
        return (float) (1 - Math.pow(1 - x, 3));
    }

    private float easeInOut(double x) {
        return (float) (x < 0.5
                ? 4 * x * x * x
                : 1 - Math.pow(-2 * x + 2, 3) / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.drawImage(bg, 0, 0, w, h, null);

        // ===== LOGO =====
        int logoSize = 200;
        int scaled = (int) (logoSize * scale);

        int x = (w - scaled) / 2;
        int centerY = (h - scaled) / 2;
        int y = (int) (centerY + logoY);

        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, logoAlpha));
        g2.drawImage(logo, x, y, scaled, scaled, null);

        // ===== TITLE =====
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, titleAlpha));

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 28));

        String title = "SURPRISE CINEMA";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (w - fm.stringWidth(title)) / 2;
        int titleY = y + scaled + 25;
        g2.drawString(title, titleX, titleY);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        String sub = "Feel The Story";
        FontMetrics fm2 = g2.getFontMetrics();
        int subX = (w - fm2.stringWidth(sub)) / 2;
        int subY = titleY + 22;
        g2.drawString(sub, subX, subY);

        // ===== BOTTOM HALF CIRCLE =====
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, bottomAlpha));

        int arcSize = 560;
        int arcX = (w - arcSize) / 2;
        int arcY = h - (arcSize / 2) + 55;

        g2.setColor(Color.WHITE);
        g2.fillArc(arcX, arcY, arcSize, arcSize, 0, 180);

        // ===== BUTTON + LINK =====
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, buttonAlpha));

        int btnW = 300;
        int btnH = 55;
        int btnX = (w - btnW) / 2;
        int btnY = h - 140;

        buttonRect = new Rectangle(btnX, btnY, btnW, btnH);
        drawButton(g2, buttonRect, "Get Started", hover, pressed);

        String linkText = "I already have an account";

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));

        FontMetrics fmLink = g2.getFontMetrics();
        int linkX = (w - fmLink.stringWidth(linkText)) / 2;
        int linkY = btnY + btnH + 30;

        g2.drawString(linkText, linkX, linkY);
        g2.drawLine(linkX, linkY + 2,
                linkX + fmLink.stringWidth(linkText), linkY + 2);

        linkRect = new Rectangle(
                linkX,
                linkY - fmLink.getAscent(),
                fmLink.stringWidth(linkText),
                fmLink.getHeight()
        );
    }

    private void drawButton(Graphics2D g2, Rectangle r,
                            String text, boolean hover, boolean pressed) {

        int radius = 50;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        if (hover) g2.setColor(new Color(255, 0, 0, 18));
        if (pressed) g2.setColor(new Color(200, 0, 0, 70));

        if (hover || pressed)
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        g2.setColor(new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));

        FontMetrics fm = g2.getFontMetrics();
        int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
        int ty = r.y + (r.height + fm.getAscent()) / 2 - 4;

        g2.drawString(text, tx, ty);
    }
}