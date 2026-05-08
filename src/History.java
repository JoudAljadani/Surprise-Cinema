import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class History extends JPanel {

    private final Appframe app;
    private final Image bg;

    private Rectangle backRect;
    private Rectangle dashboardBtnRect;
    private Rectangle openBtnRect;

    private boolean backPressed;
    private boolean dashboardPressed;
    private boolean openPressed;

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
                    if (backRect != null && backRect.contains(e.getPoint())) {
                        backPressed = true;
                        repaint();
                        return;
                    }

                    if (dashboardBtnRect != null && dashboardBtnRect.contains(e.getPoint())) {
                        dashboardPressed = true;
                    }

                    if (openBtnRect != null && openBtnRect.contains(e.getPoint())) {
                        openPressed = true;
                    }

                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (backPressed && backRect != null && backRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
                    }

                    if (dashboardPressed && dashboardBtnRect != null && dashboardBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.DASHBOARD);
                    }

                    if (openPressed && openBtnRect != null && openBtnRect.contains(e.getPoint())) {
                        TicketFileManager.openTicketHistoryFile(Appframe.currentUser.getEmail());
                    }

                    backPressed = false;
                    dashboardPressed = false;
                    openPressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backPressed = false;
                    dashboardPressed = false;
                    openPressed = false;
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

            //Back arrow
            int backSize = 36, backX = 18, backY = 32;
            backRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backRect, backPressed);

            //Title
            UIComponents.drawCenteredText(g2, "Last Bookings", w / 2,
                    70, UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            //Card box
            int cardX = 28;
            int cardW = w - 56;
            int cardY = 115;
            int cardH = 360;

            Rectangle big = new Rectangle(cardX, cardY, cardW, cardH);
            drawBigBox(g2, big);

            //Buttons
            int btnW = 145;
            int btnH = 55;
            int gap = 14;
            int total = btnW * 2 + gap;
            int startX = (w - total) / 2;
            int btnY = h - 90;

            dashboardBtnRect = new Rectangle(startX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, dashboardBtnRect, "Dashboard", dashboardPressed);

            openBtnRect = new Rectangle(startX + btnW + gap, btnY, btnW, btnH);
            UIComponents.drawButton(g2, openBtnRect, "Open File", openPressed);
        }

        private void drawBigBox(Graphics2D g2, Rectangle r) {
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 22, 22);

            String history = TicketFileManager.readTicketHistory(Appframe.currentUser.getEmail());

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            int textX = r.x + 18;
            int textY = r.y + 30;
            int lineGap = 18;

            String[] lines = history.split("\n");
            int maxLines = 16;

            for (int i = 0; i < lines.length && i < maxLines; i++) {
                g2.drawString(lines[i], textX, textY + (i * lineGap));
            }

            if (lines.length > maxLines) {
                g2.drawString("... Click Open File to view full history",
                        textX, textY + (maxLines * lineGap));
            }
        }
    }
}