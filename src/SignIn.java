import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SignIn extends JPanel {

    //Variables
    private final Appframe app;//To navigate between pages
    private final Image bg;//Background image
    private final Image logo;//Logo image

    Rectangle buttonRect;//Place of the button
    boolean pressed = false;//Button initial state

    private Rectangle backRect;//Back arrow
    private boolean backPressed = false;//Button initial state

    //------------------------------------------------------------

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

        //Get panel width and height
        int w = c.getWidth(), h = c.getHeight();

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

            super.paintComponent(g);
        }
    }

    class SignInPanel extends JPanel {

        private final int FIELD_RADIUS = 50;

        private final RoundedEmailField emailField = new RoundedEmailField(FIELD_RADIUS);
        private final RoundedPasswordField passwordField = new RoundedPasswordField(FIELD_RADIUS);

        SignInPanel() {

            //Disable default layout
            setLayout(null);

            //The place and size of the both fields
            int w = 260, h = 42, gap = 33, y = 300, x = 57;

            placeField("Email", emailField, x, y, w, h);
            placeField("Password", passwordField, x, y + (h + gap), w, h);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {

                    //if back arrow is pressed
                    if (backRect != null && backRect.contains(e.getPoint())) {
                        backPressed = true;
                        repaint();
                        return;
                    }

                    //if signIn button is pressed
                    if (buttonRect != null && buttonRect.contains(e.getPoint())) {
                        pressed = true;
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //Navigate to Splash page
                    if (backPressed && backRect != null && backRect.contains(e.getPoint())) {
                        app.showPage(Appframe.SPLASH);
                    }
                    backPressed = false;

                    //Sign In
                    if (pressed && buttonRect != null && buttonRect.contains(e.getPoint())) {
                        //String email = emailField.getText().trim();
                        //String pass = new String(passwordField.getPassword());
                        //Navigate to next page (homepage)
                        //app.showPage(Appframe.HOME_PAGE);

                        //----------------------------------------------------------------------------
                        //-----------------------SignIn Implementation HERE---------------------------
                        //----------------------------------------------------------------------------
                        String email = emailField.getText().trim();
                        String pass = new String(passwordField.getPassword());

                        if (email.isEmpty() || pass.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Please enter email and password");
                            return;
                        }

                        boolean validUser = UserQueries.loginUser(email, pass);

                        if (validUser) {
                            app.showPage(Appframe.HOME_PAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Wrong email or password");
                        }
                    }
                    pressed = false;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    pressed = false;
                    backPressed = false;
                    repaint();
                }
            });
        }

        //Placed labels, fields and display it on the screen
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
        //Draw UI elements
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            //To make corner smoother
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Get panel width
            int w = getWidth();

            //Background
            g2.drawImage(bg, 0, 0, getWidth(), getHeight(), null);

            //Back arrow
            int backSize = 36, backX = 18, backY = 32;
            backRect = new Rectangle(backX, backY, backSize, backSize);
            UIComponents.drawTextBackArrow(g2, backRect, backPressed);

            //Logo
            int logoSize = 200;
            int logoX = (w - logoSize) / 2, logoY = 0;
            g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);

            //Title
            int titleY = logoY + logoSize + 26;
            UIComponents.drawTitle(g2, w, titleY);

            //SignIn button
            int btnW = 300, btnH = 55, btnX = (w - btnW) / 2, btnY = 600;
            buttonRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, buttonRect, "Sign In", pressed);
        }
    }
}