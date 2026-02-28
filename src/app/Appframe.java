package app;

import GUI.*;

import javax.swing.*;
import java.awt.*;

public class Appframe extends JFrame {

    public static final String SPLASH = "SPLASH";
    public static final String SIGNUP = "SIGNUP";
    public static final String SIGNIN = "SIGNIN";
    public static final String PREFERENCES  = "PREFERENCES";

    private final CardLayout layout = new CardLayout();
    private final JPanel root = new JPanel(layout);

    public Appframe() {
        setTitle("Surprise Cinema");
        setSize(390, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        root.add(new Splash(this), SPLASH);
        root.add(new SignUp(this), SIGNUP);
        root.add(new SignIn(this), SIGNIN);
        root.add(new Preferences(this), PREFERENCES);

        setContentPane(root);
        showPage(SPLASH);
    }

    public void showPage(String page) {
            switch (page) {
                case SPLASH:
                    setTitle("Splash");
                    break;
                case SIGNUP:
                    setTitle("Sign Up");
                    break;
                case SIGNIN:
                    setTitle("Sign In");
                    break;
                case PREFERENCES:
                    setTitle("Preferences");
                    break;
            }

            layout.show(root, page);
        }
    }
