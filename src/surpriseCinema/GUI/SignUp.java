package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SignUp extends JFrame {

    private static final int W = 390;
    private static final int H = 720;

    private final Image bg;
    private final Image logo;

    public SignUp() {
        setSize(W, H);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        bg = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        setContentPane(new SignUpPanel());
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

    static class RoundedComboBox extends JComboBox<String> {

        private final int radius;
        private final Color borderColor;

        RoundedComboBox(String[] items, int radius, Color borderColor) {
            super(items);
            this.radius = radius;
            this.borderColor = borderColor;

            setOpaque(false);
            setFont(new Font("Arial", Font.PLAIN, 16));
            setForeground(Color.BLACK);
            setBackground(Color.WHITE);
            setBorder(new EmptyBorder(8, 14, 8, 14));

            setFocusable(false);
            UIManager.put("ComboBox.focus", new Color(220, 220, 220));
            UIManager.put("ComboBox.selectionBackground", new Color(235, 235, 235));
            UIManager.put("ComboBox.selectionForeground", Color.BLACK);

            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {

                    JLabel label = (JLabel) super.getListCellRendererComponent(
                            list, value, index, isSelected, cellHasFocus);

                    label.setForeground(Color.BLACK);
                    label.setBackground(isSelected ? new Color(235, 235, 235) : Color.WHITE);
                    return label;
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

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

    class SignUpPanel extends JPanel {

        private final Color subtleStroke = new Color(242, 242, 242);
        private final int FIELD_RADIUS = 50;

        private final RoundedField nameField = new RoundedField(FIELD_RADIUS, subtleStroke);
        private final RoundedField emailField = new RoundedField(FIELD_RADIUS, subtleStroke);
        private final RoundedPasswordField passwordField = new RoundedPasswordField(FIELD_RADIUS, subtleStroke);
        private final RoundedField ageField = new RoundedField(FIELD_RADIUS, subtleStroke);

        private final RoundedComboBox genderField =
                new RoundedComboBox(new String[]{"Male", "Female"}, FIELD_RADIUS, subtleStroke);

        Rectangle buttonRect;
        boolean hover = false;
        boolean pressed = false;

        SignUpPanel() {
            setLayout(null);

            int x = 45;
            int fieldW = 300;
            int fieldH = 46;
            int gap = 26;
            int startY = 268;

            placeField("Name", nameField, x, startY, fieldW, fieldH);
            placeField("Email", emailField, x, startY + (fieldH + gap), fieldW, fieldH);
            placeField("Password", passwordField, x, startY + 2*(fieldH + gap), fieldW, fieldH);
            placeField("Age", ageField, x, startY + 3*(fieldH + gap), fieldW, fieldH);
            placeField("Gender", genderField, x, startY + 4*(fieldH + gap), fieldW, fieldH);

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    if (buttonRect != null)
                        hover = buttonRect.contains(e.getPoint());
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    if (buttonRect != null && buttonRect.contains(e.getPoint()))
                        pressed = true;
                    repaint();
                }

                @Override public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();
                }
            });
        }

        private void placeField(String label, JComponent comp,
                                int x, int y, int w, int h) {

            JLabel l = new JLabel(label);
            l.setFont(new Font("Arial", Font.BOLD, 12));
            l.setForeground(Color.WHITE);
            l.setBounds(x + 6, y - 16, w, 16);
            add(l);

            comp.setBounds(x, y, w, h);
            add(comp);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

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
            int btnY = 640;

            buttonRect = new Rectangle(btnX, btnY, btnW, btnH);
            drawButton(g2, buttonRect, "Sign Up", hover, pressed);
        }

        private void drawButton(Graphics2D g2, Rectangle r,
                                String text, boolean hover, boolean pressed) {

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUp().setVisible(true));
    }
}