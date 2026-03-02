package GUI;

import App.Appframe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class History extends JPanel {

    private final Appframe app;
    private final Image bg;

    private Rectangle closeBtnRect;
    private Rectangle dashBtnRect;

    private boolean closePressed;
    private boolean dashPressed;

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
                    if (dashBtnRect != null && dashBtnRect.contains(e.getPoint())) dashPressed = true;
                    if (closeBtnRect != null && closeBtnRect.contains(e.getPoint())) closePressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    if (dashPressed && dashBtnRect != null && dashBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.DASHBOARD);
                    }

                    if (closePressed && closeBtnRect != null && closeBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
                    }

                    dashPressed = false;
                    closePressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    dashPressed = false;
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

            // ===== BUTTONS (BOTTOM) =====
            int btnW = 145, btnH = 55, gap = 14;
            int total = btnW * 2 + gap;
            int startX = (w - total) / 2;
            int btnY = h - 90;

            // Left: Dashboard
            dashBtnRect = new Rectangle(startX, btnY, btnW, btnH);
            // Right: Close
            closeBtnRect = new Rectangle(startX + btnW + gap, btnY, btnW, btnH);

            UIComponents.drawPrimaryButton(g2, dashBtnRect, "Dashboard", dashPressed);
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
            UIComponents.drawCenteredText(g2, text,
                    r.x + r.width / 2,
                    r.y + r.height / 2,
                    new Font("Arial", Font.BOLD, 16),
                    Color.BLACK
            );
        }
    }
}