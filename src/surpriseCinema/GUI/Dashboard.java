package GUI;

import App.Appframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JPanel {

    private final Appframe app;
    private final Image bg;

    private Rectangle backBtnRect;
    private Rectangle closeBtnRect;

    private boolean backPressed;
    private boolean closePressed;

    // Demo data (Genres + counts)
    private final String[] genres = {"Fantasy", "Action", "Drama", "Horror", "Comedy"};
    private final int[] genreCounts = {6, 9, 4, 2, 3};

    // Demo data (Countries + counts)
    private final String[] countries = {"USA", "UK", "Turkey", "Korea", "Italy"};
    private final int[] countryCounts = {8, 5, 4, 3, 2};

    public Dashboard(Appframe app) {
        this.app = app;
        bg = new ImageIcon("resources/images/Background.png").getImage();

        setLayout(new BorderLayout());
        add(new DashPanel(), BorderLayout.CENTER);
    }

    class DashPanel extends JPanel {

        DashPanel() {
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
                        app.showPage(Appframe.HISTORY);
                    }

                    if (closePressed && closeBtnRect != null && closeBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HOME_PAGE);
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

            // Back arrow (UIComponents)
            int backSize = 36, backX = 18, backY = 32;
            backBtnRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backBtnRect, backPressed);

            // Title (UIComponents)
            UIComponents.drawCenteredText(g2, "Dashboard", w / 2, 56,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            // ===== BIG CARD =====
            int cardX = 28;
            int cardY = 120;
            int cardW = w - 56;
            int cardH = 390;

            drawCard(g2, new Rectangle(cardX, cardY, cardW, cardH));

            // ===== CHARTS AREA INSIDE CARD =====
            int innerX = cardX + 18;
            int innerW = cardW - 36;

            // Section 1: Genres
            int sec1TitleY = cardY + 38;
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            g2.drawString("Most watched genres", innerX, sec1TitleY);

            int chart1X = innerX;
            int chart1Y = sec1TitleY + 18;
            int chart1H = 120;
            drawBarChart(g2, chart1X, chart1Y, innerW, chart1H, genres, genreCounts);

            // Divider line
            int divY = chart1Y + chart1H + 22;
            g2.setColor(new Color(235, 235, 235));
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawLine(innerX, divY, innerX + innerW, divY);

            // Section 2: Countries
            int sec2TitleY = divY + 34;
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            g2.drawString("Most watched countries", innerX, sec2TitleY);

            int chart2X = innerX;
            int chart2Y = sec2TitleY + 18;
            int chart2H = 120;
            drawBarChart(g2, chart2X, chart2Y, innerW, chart2H, countries, countryCounts);

            // Close button (UIComponents)
            int btnW = 300, btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = h - 90;
            closeBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawPrimaryButton(g2, closeBtnRect, "Close", closePressed);
        }
    }

    private void drawCard(Graphics2D g2, Rectangle r) {
        // Shadow
        g2.setColor(new Color(0, 0, 0, 45));
        g2.fillRoundRect(r.x + 3, r.y + 5, r.width, r.height, 24, 24);

        // Base
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 24, 24);

        // Border
        g2.setColor(new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, 24, 24);
    }

    private void drawBarChart(Graphics2D g2, int x, int y, int w, int h, String[] labels, int[] values) {
        if (labels == null || values == null || labels.length == 0) return;

        int max = 1;
        for (int v : values) if (v > max) max = v;

        int bars = Math.min(labels.length, values.length);
        int gap = 12;
        int barW = (w - (gap * (bars - 1))) / bars;

        for (int i = 0; i < bars; i++) {
            int barH = (int) ((values[i] / (double) max) * h);
            int bx = x + i * (barW + gap);
            int by = y + (h - barH);

            // Bar (same red theme)
            g2.setColor(new Color(255, 60, 60, 180));
            g2.fillRoundRect(bx, by, barW, barH, 12, 12);

            // Value
            g2.setColor(new Color(80, 80, 80));
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String val = String.valueOf(values[i]);
            FontMetrics fmV = g2.getFontMetrics();
            g2.drawString(val, bx + (barW - fmV.stringWidth(val)) / 2, by - 6);

            // Label
            g2.setColor(new Color(120, 120, 120));
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            String lab = labels[i];
            FontMetrics fmL = g2.getFontMetrics();
            g2.drawString(lab, bx + (barW - fmL.stringWidth(lab)) / 2, y + h + 18);
        }
    }
}