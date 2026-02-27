package surpriseCinema.GUI;

import app.Appframe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SignIn extends JPanel {

    private final Appframe app;

    private final Image bg;
    private final Image logo;

    public SignIn(Appframe app) {
        this.app = app;

        setLayout(new BorderLayout());

        bg = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        add(new SignInPanel(), BorderLayout.CENTER);
    }

    static class RoundedField extends JTextField {
        private final int radius;
        private final Color borderColor;

        RoundedField(int radius, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
            setOpaque(false);
            setBorder(new EmptyBorder(12, 14, 12, 14));
            setFont(new Font("Arial", Font.PLAIN, 16));
            setForeground(new Color(30, 30, 30));
            setCaretColor(new Color(30, 30, 30));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

            g2.setStroke(new BasicStroke(1.2f));
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, w - 1, h - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedPasswordField extends JPasswordField {
        private final int radius;
        private final Color borderColor;

        RoundedPasswordField(int radius, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
            setOpaque(false);
            setBorder(new EmptyBorder(12, 14, 12, 14));
            setFont(new Font("Arial", Font.PLAIN, 16));
            setForeground(new Color(30, 30, 30));
            setCaretColor(new Color(30, 30, 30));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

            g2.setStroke(new BasicStroke(1.2f));
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, w - 1, h - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    class SignInPanel extends JPanel {

        private final Color subtleStroke = new Color(242, 242, 242);
        private final int FIELD_RADIUS = 50;

        private final RoundedField emailField = new RoundedField(FIELD_RADIUS, subtleStroke);
        private final RoundedPasswordField passwordField = new RoundedPasswordField(FIELD_RADIUS, subtleStroke);

        Rectangle buttonRect;
        boolean hover = false;
        boolean pressed = false;

        SignInPanel() {
            setLayout(null);

            int fieldW = 260;
            int fieldH = 42;
            int gap = 33;
            int startY = 300;
            int x = 57;

            placeField("Email", emailField, x, startY, fieldW, fieldH);
            placeField("Password", passwordField, x, startY + (fieldH + gap), fieldW, fieldH);

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    if (buttonRect != null) hover = buttonRect.contains(e.getPoint());
                    setCursor(new Cursor(hover ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    if (buttonRect != null && buttonRect.contains(e.getPoint())) pressed = true;
                    repaint();
                }

                @Override public void mouseReleased(MouseEvent e) {
                    if (pressed && buttonRect != null && buttonRect.contains(e.getPoint())) {
                        String email = emailField.getText().trim();
                        String pass  = new String(passwordField.getPassword());

                        // TODO: تحقق من الدخول
                        // System.out.println(email + " " + pass);

                        pressed = false;
                        repaint();
                        return;
                    }

                    pressed = false;
                    repaint();
                }
            });
        }

        private void placeField(String label, JComponent comp, int x, int y, int w, int h) {
            JLabel l = new JLabel(label);
            l.setFont(new Font("Arial", Font.BOLD, 12));
            l.setForeground(Color.WHITE);

            int labelOffset = 15;
            l.setBounds(x + labelOffset, y - 16, w, 16);
            add(l);

            comp.setBounds(x, y, w, h);
            add(comp);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();

            g2.drawImage(bg, 0, 0, getWidth(), getHeight(), null);

            int logoSize = 200;
            int logoX = (w - logoSize) / 2;
            int logoY = 0;
            g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            String title = "SURPRISE CINEMA";
            FontMetrics fm = g2.getFontMetrics();
            int titleX = (w - fm.stringWidth(title)) / 2;
            int titleY = logoY + logoSize + 26;
            g2.drawString(title, titleX, titleY);

            int btnW = 300;
            int btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 600;

            buttonRect = new Rectangle(btnX, btnY, btnW, btnH);
            drawButton(g2, buttonRect, "Sign In", hover, pressed);
        }

        private void drawButton(Graphics2D g2, Rectangle r, String text, boolean hover, boolean pressed) {
            int radius = 50;

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

            if (hover) {
                g2.setColor(new Color(255, 0, 0, 18));
                g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
            }

            if (pressed) {
                g2.setColor(new Color(200, 0, 0, 70));
                g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
            }

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(220, 220, 220));
            g2.drawRoundRect(r.x, r.y, r.width, r.height, radius, radius);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 18));

            FontMetrics fm = g2.getFontMetrics();
            int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
            int ty = r.y + (r.height + fm.getAscent()) / 2 - 4;

            g2.drawString(text, tx, ty);
        }
    }
}