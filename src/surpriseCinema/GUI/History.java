package surpriseCinema.GUI;

import surpriseCinema.App.Appframe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class History extends JPanel {

    private final Appframe app;
    private final Image bg;

    private Rectangle backBtnRect;
    private Rectangle closeBtnRect;

    private boolean backPressed;
    private boolean closePressed;

    public History(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();

        setLayout(new BorderLayout());
        add(new HistoryPanel(), BorderLayout.CENTER);
    }

    class HistoryPanel extends JPanel {

        HistoryPanel() {

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (backBtnRect != null && backBtnRect.contains(e.getPoint())) backPressed = true;
                    if (closeBtnRect != null && closeBtnRect.contains(e.getPoint())) closePressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    if (backPressed && backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOMEPAGE);
                    }

                    if (closePressed && closeBtnRect != null && closeBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOMEPAGE);
                    }

                    backPressed = false;
                    closePressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                     backPressed = false;
                     closePressed = false;
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
            UIComponents.drawCenteredText(g2, "Last Bookings", w / 2, 56,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            // ONE BIG CARD
            int cardX = 28;
            int cardW = w - 56;
            int cardY = 95;
            int cardH = 360;

            Rectangle big = new Rectangle(cardX, cardY, cardW, cardH);
            drawBigBox(g2, big);

            // Close button
            int btnW = 300, btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = h - 90;
            closeBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawPrimaryButton(g2, closeBtnRect, "Close", closePressed);
        }

        private void drawBigBox(Graphics2D g2, Rectangle r) {

            // Card base
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 22, 22);

            // Border
            g2.setColor(new Color(220, 220, 220));
            g2.setStroke(new BasicStroke(1.6f));
            g2.drawRoundRect(r.x, r.y, r.width, r.height, 22, 22);

            // ONLY TEXT inside
            String text = "History Booking details are here";
            UIComponents.drawCenteredText(g2, text, r.x + r.width / 2,
                    r.y + r.height / 2, new Font("Arial", Font.BOLD, 16), Color.BLACK
            );
        }
    }

}