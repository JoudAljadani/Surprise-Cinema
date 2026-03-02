package surpriseCinema.GUI;

import surpriseCinema.App.Appframe;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SignUp extends JPanel {

    //Used to navigate between pages
    private final Appframe app;

    private final Image bg;
    private final Image logo;

    public SignUp(Appframe app) {
        this.app = app;

        bg = new ImageIcon("resources/images/Background.png").getImage();
        logo = new ImageIcon("resources/images/Logo.png").getImage();

        //Set layout and add main panel to center
        setLayout(new BorderLayout());
        add(new SignUpPanel(), BorderLayout.CENTER);
    }

    //Draw rounded white background
    private static void paintRoundedBackground(Graphics g, JComponent c, int radius) {

        //Create object and casting to 2D
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = c.getWidth();
        int h = c.getHeight();

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

        //Close
        g2.dispose();
    }

    //Styling for text and password fields
    private static void styleTextField(JComponent c) {

        //Disable default background drawing
        c.setOpaque(false);

        //Add padding between text and border
        c.setBorder(new EmptyBorder(12, 14, 12, 14));

        c.setFont(new Font("Arial", Font.PLAIN, 16));
    }

    //Name Email Age fields
    static class RoundedField extends JTextField {
        private final int radius;

        RoundedField(int radius) {
            this.radius = radius;
            styleTextField(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            paintRoundedBackground(g, this, radius);
            super.paintComponent(g);
        }
    }

    //Password field
    static class RoundedPasswordField extends JPasswordField {
        private final int radius;

        RoundedPasswordField(int radius) {
            this.radius = radius;
            styleTextField(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            paintRoundedBackground(g, this, radius);
            super.paintComponent(g);
        }
    }

    //Gender dropdown menu
    static class RoundedComboBox extends JComboBox<String> {
        private final int radius;

        RoundedComboBox(String[] items, int radius) {

            //Send values to the super class
            super(items);

            this.radius = radius;

            //Disable default background
            setOpaque(false);

            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            setFont(new Font("Arial", Font.PLAIN, 16));
            setBorder(new EmptyBorder(8, 14, 8, 14));


       }

        @Override
        //Call paintRoundedBackground method to draw rounded white background
        protected void paintComponent(Graphics g) {
            paintRoundedBackground(g, this, radius);
            super.paintComponent(g);
        }
    }

    class SignUpPanel extends JPanel {

        private final int FIELD_RADIUS = 50;

        private final RoundedField nameField = new RoundedField(FIELD_RADIUS);
        private final RoundedField emailField = new RoundedField(FIELD_RADIUS);
        private final RoundedPasswordField passwordField = new RoundedPasswordField(FIELD_RADIUS);
        private final RoundedField ageField = new RoundedField(FIELD_RADIUS);
        private final RoundedComboBox genderField = new RoundedComboBox(new String[]{"Male", "Female"}, FIELD_RADIUS);

        //Place of the button
        Rectangle buttonRect;
        //Button initial state
        boolean pressed = false;

        SignUpPanel() {
            //Disable default layout
            setLayout(null);

            //The place and size of the fields
            int w = 260;
            int h = 42;
            int gap = 26;
            int y = 257;
            int x = 57;

            placeField("Name", nameField, x, y, w, h);
            placeField("Email", emailField, x, y + (h + gap), w, h);
            placeField("Password", passwordField, x, y + 2 * (h + gap), w, h);
            placeField("Age", ageField, x, y + 3 * (h + gap), w, h);
            placeField("Gender", genderField, x, y + 4 * (h + gap), w, h);

            addMouseListener(new MouseAdapter() {
                @Override
                //Check if the mouse press happened inside the Sign Up button
                public void mousePressed(MouseEvent e) {
                    pressed = (buttonRect != null && buttonRect.contains(e.getPoint()));
                    repaint();
                }

                @Override
                //Check if the mouse press and released was inside the Sign Up button
                public void mouseReleased(MouseEvent e) {
                    boolean validClick = pressed && buttonRect != null && buttonRect.contains(e.getPoint());
                    pressed = false;
                    repaint();

                    //If a valid click = true, perform Sign Up action
                    if (validClick) {
                        String name = nameField.getText().trim();
                        String email = emailField.getText().trim();
                        String pass = new String(passwordField.getPassword());
                        String age = ageField.getText().trim();
                        String gender = (String) genderField.getSelectedItem();

                        //Show next page
                        app.showPage(Appframe.PREFERENCES);
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
            //Add to panel
            add(l);

            comp.setBounds(x, y, w, h);
            //Add to panel
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

            int titleY = logoY + logoSize + 26;
            UIComponents.drawBrandTitle(g2, w, titleY);

            //Set SignUn button settings and call the drawSignUpButton function
            int btnW = 300;
            int btnH = 55;
            int btnX = (w - btnW) / 2;
            int btnY = 600;

            buttonRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawPrimaryButton(g2, buttonRect, "Sign Up", pressed);
        }

    }
}