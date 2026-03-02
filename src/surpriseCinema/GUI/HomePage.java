package surpriseCinema.GUI;

import surpriseCinema.App.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JPanel {

    private final Appframe app;
    private final Image bg;
    private final Image logo;

    // Icons
    private final Image ticketImg;
    private final Image prefsImg;
    private final Image historyImg;
    private final Image rateImg;

    // Click areas
    private Rectangle ticketRect, prefsRect, historyRect, rateRect;
    private Rectangle surpriseBtnRect;

    // Press state
    private enum Target { NONE, TICKET, PREFS, HISTORY, RATE }
    private Target pressedTarget = Target.NONE;

    private boolean surprisePressed;

    public HomePage(Appframe app) {
        this.app = app;

        bg   = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        ticketImg  = new ImageIcon("resources/images/ticket.png").getImage();
        prefsImg   = new ImageIcon("resources/images/prefs.png").getImage();
        historyImg = new ImageIcon("resources/images/history.png").getImage();
        rateImg    = new ImageIcon("resources/images/star.png").getImage();

        setLayout(new BorderLayout());
        add(new HomePanel(), BorderLayout.CENTER);
    }

    class HomePanel extends JPanel {

        HomePanel() {

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    // Surprise button press
                    if (surpriseBtnRect != null && surpriseBtnRect.contains(e.getPoint())) {
                        surprisePressed = true;
                    }

                    // Cards press
                    pressedTarget = getTargetAt(e.getPoint());

                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    // Surprise button click
                    if (surprisePressed && surpriseBtnRect != null && surpriseBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.MOVIERESULT);
                    }

                    // Card click:
                    // Only navigate if mouse is released on the SAME card that was pressed
                    Target releasedTarget = getTargetAt(e.getPoint());
                    if (pressedTarget != Target.NONE && pressedTarget == releasedTarget) {
                        if (pressedTarget == Target.TICKET)
                            app.showPage(Appframe.MYTICKET);
                        else if (pressedTarget == Target.PREFS)
                            app.showPage(Appframe.PREFERENCES);
                        else if (pressedTarget == Target.HISTORY)
                            app.showPage(Appframe.HISTORY);
                        else if (pressedTarget == Target.RATE)
                            app.showPage(Appframe.RATE);
                    }

                    surprisePressed = false;
                    pressedTarget = Target.NONE;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    surprisePressed = false;
                    pressedTarget = Target.NONE;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });
        }

        private Target getTargetAt(Point p) {
            if (ticketRect != null && ticketRect.contains(p)) return Target.TICKET;
            if (prefsRect != null && prefsRect.contains(p)) return Target.PREFS;
            if (historyRect != null && historyRect.contains(p)) return Target.HISTORY;
            if (rateRect != null && rateRect.contains(p)) return Target.RATE;
            return Target.NONE;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            g2.drawImage(bg, 0, 0, w, h, null);

            // Logo
            int logoSize = 200;
            int logoX = (w - logoSize) / 2;
            g2.drawImage(logo, logoX, 0, logoSize, logoSize, null);

            // Title
            int titleY = logoSize + 26;
            UIComponents.drawBrandTitle(g2, w, titleY);

            // Welcome
            int x = w/2, y= logoSize + 54;
            UIComponents.drawCenteredText(g2, "Welcome, Emma!", x, y,
                    UIComponents.FONT_SUBTITLE, UIComponents.TEXT_WHITE_SOFT2);

            // 4 cards grid
            int cardW = 130, cardH = 110;
            int gapX = 16, gapY = 16;
            int totalW = cardW * 2 + gapX;
            int startX = (w - totalW) / 2;
            int startY = logoSize + 80;

            // IMPORTANT: Rectangles are re-created every repaint (that's why we use Target enum)
            ticketRect = new Rectangle(startX, startY, cardW, cardH);
            prefsRect = new Rectangle(startX + cardW + gapX, startY, cardW, cardH);
            historyRect = new Rectangle(startX, startY + cardH + gapY, cardW, cardH);
            rateRect = new Rectangle(startX + cardW + gapX, startY + cardH + gapY, cardW, cardH);


            UIComponents.drawIconCard(g2, ticketRect, "Ticket", ticketImg, pressedTarget == Target.TICKET);

            UIComponents.drawIconCard(g2, prefsRect, "Edit Preferences", prefsImg, pressedTarget == Target.PREFS);

            UIComponents.drawIconCard(g2, historyRect, "History", historyImg, pressedTarget == Target.HISTORY);

            UIComponents.drawIconCard(g2, rateRect, "Rate Movie", rateImg, pressedTarget == Target.RATE);
            // Surprise button
            int btnW = 300, btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 575;
            surpriseBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawPrimaryButton(g2, surpriseBtnRect, "Surprise Me!", surprisePressed);
        }
    }

}
