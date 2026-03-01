package app;

import surpriseCinema.GUI.*;

import javax.swing.*;
import java.awt.*;

public class Appframe extends JFrame {

    //Card names for navigation between pages
    public static final String SPLASH = "SPLASH";
    public static final String SIGNUP = "SIGNUP";
    public static final String SIGNIN = "SIGNIN";
    public static final String PREFERENCES  = "PREFERENCES";
    public static final String MovieResult = "MovieResult";
    public static final String chooseTimePage = "chooseTimePage";
    public static final String TicketSuccess = "TicketSuccess";

    private final CardLayout layout = new CardLayout();
    private final JPanel root = new JPanel(layout);

    public Appframe() {

        //Window Settings
        setSize(390, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Add all pages and link it with card names
        root.add(new Splash(this), SPLASH);
        root.add(new SignUp(this), SIGNUP);
        root.add(new SignIn(this), SIGNIN);
        root.add(new Preferences(this), PREFERENCES);
        root.add(new MovieResult(this), MovieResult);
        root.add(new chooseTimePage(this), chooseTimePage);
        root.add(new TicketSuccess(this), TicketSuccess);

        //Use root panel as the main container of this frame
        setContentPane(root);
        showPage(MovieResult);
    }

    public void showPage(String page) {
            switch (page) {
                case SPLASH:
                    setTitle("Surprise Cinema");
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
                case MovieResult:
                    setTitle("MovieResult");
                    break;
                case chooseTimePage:
                    setTitle("chooseTimePage");
                    break;
                case TicketSuccess:
                    setTitle("TicketSuccess");
                    break;
            }

        //Display this page using CardLayout
        layout.show(root, page);
        }
    }
