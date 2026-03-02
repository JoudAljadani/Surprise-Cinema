package surpriseCinema.GUI;

import java.awt.*;

public class UIComponents {

    // ===== BUTTON THEME =====
    public static final int BUTTON_RADIUS = 50;
    public static final int BUTTON_FONT_SIZE = 18;

    public static final Color BUTTON_BG = Color.WHITE;
    public static final Color BUTTON_BORDER = new Color(220, 220, 220);
    public static final Color BUTTON_TEXT = Color.BLACK;
    public static final Color BUTTON_PRESSED_OVERLAY = new Color(200, 0, 0, 70);

    // ===== CARD THEME =====
    public static final int CARD_RADIUS = 22;
    public static final Color CARD_BG = Color.WHITE;
    public static final Color CARD_BORDER = new Color(242, 242, 242);
    public static final Color CARD_SELECTED_OVERLAY = new Color(200, 0, 0, 70);

    // ===== FONTS (Arial) =====
    public static final Font FONT_BRAND = new Font("Arial", Font.BOLD, 24);     // SURPRISE CINEMA
    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 22);     // Page title
    public static final Font FONT_SUBTITLE = new Font("Arial", Font.PLAIN, 16); // Subtitle / question
    public static final Font FONT_BODY = new Font("Arial", Font.PLAIN, 14);     // body text
    public static final Font FONT_SMALL = new Font("Arial", Font.PLAIN, 12);    // small hints

    // ===== TEXT COLORS =====
    public static final Color TEXT_WHITE = Color.WHITE;
    public static final Color TEXT_WHITE_SOFT = new Color(255, 255, 255, 210);
    public static final Color TEXT_WHITE_SOFT2 = new Color(255, 255, 255, 180);
    public static final Color TEXT_BLACK = Color.BLACK;
    public static final Color TEXT_GRAY = new Color(130, 130, 130);


    private UIComponents() {} // prevent instantiation

    // ===== PRIMARY BUTTON (NO SHADOW) =====
    public static void drawPrimaryButton(
            Graphics2D g2,
            Rectangle r,
            String text,
            boolean pressed
    ) {
        // Base
        g2.setColor(BUTTON_BG);
        g2.fillRoundRect(r.x, r.y, r.width, r.height,
                BUTTON_RADIUS, BUTTON_RADIUS);

        // Pressed overlay
        if (pressed) {
            g2.setColor(BUTTON_PRESSED_OVERLAY);
            g2.fillRoundRect(r.x, r.y, r.width, r.height,
                    BUTTON_RADIUS, BUTTON_RADIUS);
        }

        // Border
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(BUTTON_BORDER);
        g2.drawRoundRect(r.x, r.y, r.width, r.height,
                BUTTON_RADIUS, BUTTON_RADIUS);

        // Text
        g2.setColor(BUTTON_TEXT);
        g2.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));

        FontMetrics fm = g2.getFontMetrics();
        int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
        int ty = r.y + (r.height + fm.getAscent()) / 2 - 4;

        g2.drawString(text, tx, ty);
    }


    // ===== TEXT BACK ARROW "<" =====
    public static void drawTextBackArrow(
            Graphics2D g2,
            Rectangle r,
            boolean pressed
    ) {
        g2.setColor(pressed ? new Color(255, 255, 255, 160) : Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 26));

        FontMetrics fm = g2.getFontMetrics();

        String arrow = "<";

        int tx = r.x + (r.width - fm.stringWidth(arrow)) / 2;
        int ty = r.y + fm.getAscent();

        g2.drawString(arrow, tx, ty);
    }

    public static void drawSelectableCard(
            Graphics2D g2,
            Rectangle r,
            String icon,
            String label,
            boolean selected
    ) {
        // base
        g2.setColor(CARD_BG);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, CARD_RADIUS, CARD_RADIUS);

        // selected overlay
        if (selected) {
            g2.setColor(CARD_SELECTED_OVERLAY);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, CARD_RADIUS, CARD_RADIUS);
        }

        // border
        g2.setStroke(new BasicStroke(2.3f));
        g2.setColor(CARD_BORDER);
        g2.drawRoundRect(r.x, r.y, r.width, r.height, CARD_RADIUS, CARD_RADIUS);

        // icon (emoji)
        if (icon != null && !icon.isEmpty()) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
            FontMetrics fi = g2.getFontMetrics();
            int ix = r.x + (r.width - fi.stringWidth(icon)) / 2;
            int iy = r.y + 34;
            g2.drawString(icon, ix, iy);
        }

        // label
        if (label != null && !label.isEmpty()) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics ft = g2.getFontMetrics();
            int tx = r.x + (r.width - ft.stringWidth(label)) / 2;
            int ty = r.y + 62;
            g2.drawString(label, tx, ty);
        }
    }

    public static void drawIconCard(
            Graphics2D g2,
            Rectangle r,
            String label,
            Image icon,
            boolean pressed
    ) {
        int radius = 20;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        if (pressed) {
            g2.setColor(new Color(200, 0, 0, 60));
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
        }

        g2.setColor(new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, radius, radius);

        int imgSize = 70;
        int imgX = r.x + (r.width - imgSize) / 2;
        int imgY = r.y + 10;
        if (icon != null) g2.drawImage(icon, imgX, imgY, imgSize, imgSize, null);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(label,
                r.x + (r.width - fm.stringWidth(label)) / 2,
                r.y + r.height - 14);
    }

    // ===== CENTERED TEXT HELPERS =====
    public static void drawCenteredText(Graphics2D g2, String text, int centerX, int y, Font font, Color color) {
        g2.setFont(font);
        g2.setColor(color);
        FontMetrics fm = g2.getFontMetrics();
        int x = centerX - fm.stringWidth(text) / 2;
        g2.drawString(text, x, y);
    }

    public static void drawBrandTitle(Graphics2D g2, int panelWidth, int y) {
        drawCenteredText(g2, "SURPRISE CINEMA", panelWidth / 2, y, FONT_BRAND, TEXT_WHITE);
    }
}