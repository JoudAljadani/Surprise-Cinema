package GUI;

import App.Appframe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SignIn extends JPanel {

    //Used to navigate between pages
    private final Appframe app;

    private final Image bg;
    private final Image logo;

    public SignIn(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new SignInPanel(), BorderLayout.CENTER);
    }

    //Draw rounded background
    private static void paintRoundedBackground(Graphics g, JComponent c, int radius) {

        //Create object and casting to 2D
        Graphics2D g2 = (Graphics2D) g.create();

        int w = c.getWidth();
        int h = c.getHeight();

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

        //Close
        g2.dispose();
    }

    //Settings of the text
    private static void styleTextField(JComponent c) {

        //Disable default background drawing
        c.setOpaque(false);

        //Add padding between text and border
        c.setBorder(new EmptyBorder(12, 14, 12, 14));

        c.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    //Email part
    static class RoundedEmailField extends JTextField {
        private final int radius;

        RoundedEmailField(int radius) {
            this.radius = radius;
            styleTextField(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            //Call the function to draw the rectangle
            paintRoundedBackground(g, this, radius);

            //Draw default content (text and caret) by calling the super class
            super.paintComponent(g);
        }
    }

    //Password part
    static class RoundedPasswordField extends JPasswordField {
        private final int radius;

        RoundedPasswordField(int radius) {
            this.radius = radius;
            styleTextField(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            //Call the function to draw the rectangle
            paintRoundedBackground(g, this, radius);

            //Draw default content (text and caret) by calling the super class
            super.paintComponent(g);
        }
    }

    class SignInPanel extends JPanel {

        private final int FIELD_RADIUS = 50;

        private final RoundedEmailField emailField = new RoundedEmailField(FIELD_RADIUS);
        private final RoundedPasswordField passwordField = new RoundedPasswordField(FIELD_RADIUS);

        //Place of the button
        Rectangle buttonRect;
        //Button initial state
        boolean pressed = false;

        SignInPanel() {

            //Disable default layout
            setLayout(null);

            //The place and size of the both fields
            int w = 260;
            int h = 42;
            int gap = 33;
            int y = 300;
            int x = 57;

            placeField("Email", emailField, x, y, w, h);
            placeField("Password", passwordField, x, y + (h + gap), w, h);

            addMouseListener(new MouseAdapter() {
                @Override
               //Check if the mouse press happened inside the Sign In button
                public void mousePressed(MouseEvent e) {
                    pressed = (buttonRect != null && buttonRect.contains(e.getPoint()));
                    repaint();
                }

                @Override
               //Check if the mouse was released inside the Sign In button
                public void mouseReleased(MouseEvent e) {
                    boolean validClick = pressed && buttonRect != null && buttonRect.contains(e.getPoint());
                    pressed = false;
                    repaint();

                    //If a valid click = true, perform Sign In action
                    if (validClick) {
                        String email = emailField.getText().trim();
                        String pass = new String(passwordField.getPassword());

                        //Show next page
                        app.showPage(Appframe.HOMEPAGE);

                    }
                }
            });
        }

        //Placed labels and the fields and display it on the screen
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

        //Draw the logo, background and setTitle and display on the screen
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            //To make corner smoother
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Get panel width
            int w = getWidth();

            g2.drawImage(bg, 0, 0, getWidth(), getHeight(), null);

            int logoSize = 200;
            int logoX = (w - logoSize) / 2;
            int logoY = 0;
            g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            String title = "SURPRISE CINEMA";

            //Title position
            FontMetrics fm = g2.getFontMetrics();
            int titleX = (w - fm.stringWidth(title)) / 2;
            int titleY = logoY + logoSize + 26;
            g2.drawString(title, titleX, titleY);

            //Set SignIn button settings and call the drawSignInButton function
            int btnW = 300;
            int btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 600;

            buttonRect = new Rectangle(btnX, btnY, btnW, btnH);
            drawSignInButton(g2, buttonRect, "Sign In", pressed);
        }

        //Draw SignIn button
        private void drawSignInButton(Graphics2D g2, Rectangle r, String text, boolean pressed) {
            int radius = 50;

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);

            if (pressed) {
                g2.setColor(new Color(200, 0, 0, 70));
                g2.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
            }

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(220, 220, 220));
            g2.drawRoundRect(r.x, r.y, r.width, r.height, radius, radius);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 18));

            //Center the Sign In text
            FontMetrics fm = g2.getFontMetrics();
            int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
            int ty = r.y + (r.height + fm.getAscent()) / 2 - 4;

            g2.drawString(text, tx, ty);
        }
    }
}