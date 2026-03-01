package App;

import surpriseCinema.GUI.*;

import javax.swing.*;
import java.awt.*;

public class Appframe extends JFrame {

    //Card names for navigation between pages
    public static final String Splash = "Splash";
    public static final String SignUp = "SignUp";
    public static final String SignIn = "SignIn";
    public static final String Preferences  = "Preferences";
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
        root.add(new Splash(this), Splash);
        root.add(new SignUp(this), SignUp);
        root.add(new SignIn(this), SignIn);
        root.add(new Preferences(this), Preferences);
        root.add(new MovieResult(this), MovieResult);
        root.add(new chooseTimePage(this), chooseTimePage);
        root.add(new TicketSuccess(this), TicketSuccess);

        //Use root panel as the main container of this frame
        setContentPane(root);
        showPage(Splash);
    }

    public void showPage(String page) {
            switch (page) {
                case Splash:
                    setTitle("Surprise Cinema");
                    break;
                case SignUp:
                    setTitle("Sign Up");
                    break;
                case SignIn:
                    setTitle("Sign In");
                    break;
                case Preferences:
                    setTitle("Preferences");
                    break;
                case MovieResult:
                    setTitle("Movie Result");
                    break;
                case chooseTimePage:
                    setTitle("Choose Movie Time ");
                    break;
                case TicketSuccess:
                    setTitle("TicketSuccess");
                    break;
            }

        //Display this page using CardLayout
        layout.show(root, page);
        }
    }
