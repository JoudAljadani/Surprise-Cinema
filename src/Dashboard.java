import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class Dashboard extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image

    //Clickable buttons
    private Rectangle backBtnRect;
    private Rectangle closeBtnRect;

    private boolean backPressed;
    private boolean closePressed;

    private String[] genres;
    private int[] genreCounts;

    //------------------------------------------------------------

    public Dashboard(Appframe app) {
        this.app = app;
        //Background image
        bg = new ImageIcon("resources/images/Background.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new DashPanel(), BorderLayout.CENTER);
    }

    class DashPanel extends JPanel {

        DashPanel() {
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    //if back button is pressed
                    if (backBtnRect != null && backBtnRect.contains(e.getPoint())) backPressed = true;
                    //if close button is pressed
                    if (closeBtnRect != null && closeBtnRect.contains(e.getPoint())) closePressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                    //if back, navigate to history page
                    if (backPressed && backBtnRect != null && backBtnRect.contains(e.getPoint())) {
                        app.showPage(Appframe.HISTORY);
                    }

                    //if close, navigate to homepage
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
        //Draw UI elements
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            //Make corner smoother
           g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Get panel width and height
            int w = getWidth(), h = getHeight();

            if (Appframe.currentUser != null) {
                genres = DashboardManager.getGenreLabels(Appframe.currentUser.getEmail());
                genreCounts = DashboardManager.getGenreCounts(Appframe.currentUser.getEmail());
            }

            //Background
            g2.drawImage(bg, 0, 0, w, h, null);

            //Back arrow
            int backSize = 36, backX = 18, backY = 32;
            backBtnRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backBtnRect, backPressed);

            //Title
            UIComponents.drawCenteredText(g2, "Dashboard", w / 2, 56,
                    UIComponents.FONT_TITLE, UIComponents.TEXT_WHITE);

            //Card box
            int cardX = 28, cardY = 120, cardW = w - 56, cardH = 250;
            drawCard(g2, new Rectangle(cardX, cardY, cardW, cardH));

            //Charts in the card
            int innerX = cardX + 18;
            int innerW = cardW - 36;

            //First genres
            int sec1TitleY = cardY + 38;
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(Color.BLACK);
            g2.drawString("Most watched genres", innerX, sec1TitleY);

            int chart1X = innerX;
            int chart1Y = sec1TitleY + 18;
            int chart1H = 120;
            drawBarChart(g2, chart1X, chart1Y, innerW, chart1H, genres, genreCounts);

            //Line between sections
            int divY = chart1Y + chart1H + 20;
            g2.setColor(new Color(235, 235, 235));
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawLine(innerX, divY, innerX + innerW, divY);

            //Close button
            int btnW = 300, btnH = 55, btnX = (w - btnW) / 2, btnY = h - 90;
            closeBtnRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, closeBtnRect, "Close", closePressed);
        }
    }

    //Draw cards
    private void drawCard(Graphics2D g2, Rectangle r) {
        //Base card
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 24, 24);

    }

    //Draw barCharts
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

            //Bar
            g2.setColor(new Color(255, 60, 60, 180));
            g2.fillRoundRect(bx, by, barW, barH, 12, 12);

            //Value
            g2.setColor(new Color(80, 80, 80));
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String val = String.valueOf(values[i]);
            FontMetrics fmV = g2.getFontMetrics();
            g2.drawString(val, bx + (barW - fmV.stringWidth(val)) / 2, by - 6);

            //Lable
            g2.setColor(new Color(120, 120, 120));
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            String lab = labels[i];
            FontMetrics fmL = g2.getFontMetrics();
            g2.drawString(lab, bx + (barW - fmL.stringWidth(lab)) / 2, y + h + 18);
        }
    }
}