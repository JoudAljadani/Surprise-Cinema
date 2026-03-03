package GUI;

import App.Appframe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Splash extends JPanel {

    private final Appframe app;

    private final Image bg;
    private final Image logo;

    private Rectangle buttonRect;
    private Rectangle linkRect;

    //Initial state
    private boolean pressed = false;

    public Splash(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (buttonRect != null && buttonRect.contains(e.getPoint()))
                    pressed = true;
                repaint(); //Redraw the screen
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //Get Started (SignUp)
                if (pressed && buttonRect != null && buttonRect.contains(e.getPoint())) {
                    app.showPage(Appframe.SIGNUP);
                }

                //I already have an account (SignIn)
                if (linkRect != null && linkRect.contains(e.getPoint())) {
                    app.showPage(Appframe.SIGNIN);
                }

                pressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        //Make corner smoother
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Get panel width and height
        int w = getWidth();
        int h = getHeight();

        //Background
        g2.drawImage(bg, 0, 0, w, h, null);

        //Logo
        int logoSize = 200;
        int logoX = (w - logoSize) / 2;
        int logoY = ((h - logoSize) / 2) - 140;

        g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);

        // Title
        int titleY = logoY + logoSize + 25, x = w / 2;
        UIComponents.drawCenteredText(g2, "SURPRISE CINEMA", x, titleY,
                UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

// Subtitle
        int subY = titleY + 22;
        UIComponents.drawCenteredText(g2, "Feel The Story", x, subY,
                UIComponents.FONT_BODY, UIComponents.TEXT_BLACK);

        //Bottom half circle
        int circleSize = 560;
        int circleX = (w - circleSize) / 2;
        int circleY = h - (circleSize / 2) + 55;

        g2.setColor(Color.WHITE);
        g2.fillArc(circleX, circleY, circleSize, circleSize, 0, 180);

        //Button + link
        int btnW = 300;
        int btnH = 55;
        int btnX = (w - btnW) / 2;
        int btnY = h - 140;

        buttonRect = new Rectangle(btnX, btnY, btnW, btnH);
        UIComponents.drawPrimaryButton(g2, buttonRect, "Get Started!", pressed);

        String linkText = "I already have an account";

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));

        //Position
        FontMetrics fmLink = g2.getFontMetrics();
        int linkX = (w - fmLink.stringWidth(linkText)) / 2;
        int linkY = btnY + btnH + 30;

        g2.drawString(linkText, linkX, linkY);

        //Underline
        g2.drawLine(linkX, linkY + 2, linkX + fmLink.stringWidth(linkText), linkY + 2);

        //Invisible Rectangle around the text
        linkRect = new Rectangle(linkX, linkY - fmLink.getAscent(), fmLink.stringWidth(linkText), fmLink.getHeight()
        );
    }


}