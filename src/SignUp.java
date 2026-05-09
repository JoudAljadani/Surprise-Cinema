import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;


public class SignUp extends JPanel {

    //Variables
    private final Appframe app;//Used to navigate between pages
    private final Image bg;//Background image
    private final Image logo;//Logo image

    Rectangle buttonRect;//Place of the button
    boolean pressed = false;//Button initial state

    private Rectangle backRect;//Back arrow
    private boolean backPressed = false;//Button initial state

    //------------------------------------------------------------

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

        //Get panel width and height
        int w = c.getWidth(), h = c.getHeight();

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

            super(items);//Send values to the super class
            this.radius = radius;
            setOpaque(false);//Disable default background
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            setFont(new Font("Arial", Font.PLAIN, 16));
            setBorder(new EmptyBorder(8, 14, 8, 14));
       }

        @Override
        protected void paintComponent(Graphics g) {
            //Call paintRoundedBackground method to draw rounded white background
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


        SignUpPanel() {
            setLayout(null);//Disable default layout

            //The place and size of the fields
            int w = 260, h = 42, gap = 26, y = 257, x = 57;
            placeField("Name", nameField, x, y, w, h);
            placeField("Email", emailField, x, y + (h + gap), w, h);
            placeField("Password", passwordField, x, y + 2 * (h + gap), w, h);
            placeField("Age", ageField, x, y + 3 * (h + gap), w, h);
            placeField("Gender", genderField, x, y + 4 * (h + gap), w, h);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    //Check if the press (Back arrow)
                    if (backRect != null && backRect.contains(e.getPoint())) {
                        backPressed = true;
                        repaint();
                        return;
                    }
                    //Check if the press (Sign Up)
                    pressed = (buttonRect != null && buttonRect.contains(e.getPoint()));
                    repaint();
                }

            //----------------------------------------------------------------------------
            //-----------------------SignUp Implementation HERE---------------------------
            //----------------------------------------------------------------------------
            public void mouseReleased(MouseEvent e) {
                    //if back arrow, navigate to Splash page
                    if (backPressed && backRect != null && backRect.contains(e.getPoint())) {
                        app.showPage(Appframe.SPLASH);
                    }

                    backPressed = false;

                    //if Sign Up = true, perform Sign Up action
                    if (pressed && buttonRect != null && buttonRect.contains(e.getPoint())) {

                        String name = nameField.getText().trim();
                        String email = emailField.getText().trim();
                        String pass = new String(passwordField.getPassword());
                        String age = ageField.getText().trim();
                        String gender = (String) genderField.getSelectedItem();

                        try {

                                // Check empty fields
                                if (ValidationManager.isSignUpEmpty(name, email, pass, age)) {
                                    JOptionPane.showMessageDialog(null, "Please fill all fields");
                                    return;
                                }

                                // Check email format
                                if (!ValidationManager.isValidEmail(email)) {
                                    JOptionPane.showMessageDialog(null, "Please enter a valid email address");
                                    return;
                                }

                                // Check password length
                                if (!ValidationManager.isValidPassword(pass)) {
                                    JOptionPane.showMessageDialog(null, "Password must be at least 9 characters");
                                    return;
                                }

                                // Check age
                                if (!ValidationManager.isValidAge(age)) {
                                    JOptionPane.showMessageDialog(null, "Age must be a number");
                                    return;
                                }

                                int ageNumber = ValidationManager.convertAge(age);

                                // Check if email already exists
                                if (DatabaseQueries.emailExists(email)) {
                                    JOptionPane.showMessageDialog(null, "Email already exists");
                                    return;
                                }

                                User user = new User(
                                        name,
                                        email,
                                        pass,
                                        ageNumber,
                                        gender
                                );

                                DatabaseQueries.addUser(user);

                                Appframe.currentUser = user;

                                JOptionPane.showMessageDialog(null, "Account created successfully");

                                app.showPage(Appframe.PREFERENCES_GENRES);

                            } catch (Exception ex) {

                                JOptionPane.showMessageDialog(null, "Sign up error: " + ex.getMessage());
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
            add(l);//Add to panel

            comp.setBounds(x, y, w, h);
            add(comp);//Add to panel
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
            int logoX = (w - logoSize) / 2;
            int logoY = 0;
            g2.drawImage(logo, logoX, logoY, logoSize, logoSize, null);

            //Title
            int titleY = logoY + logoSize + 26;
            UIComponents.drawTitle(g2, w, titleY);

            //Set SignUn button settings and draw it using ui components
            int btnW = 300, btnH = 55, btnX = (w - btnW) / 2, btnY = 600;
            buttonRect = new Rectangle(btnX, btnY, btnW, btnH);
            UIComponents.drawButton(g2, buttonRect, "Sign Up", pressed);
        }
    }
}