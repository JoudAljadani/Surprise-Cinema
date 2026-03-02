package App;

import GUI.*;

import javax.swing.*;
import java.awt.*;

public class Appframe extends JFrame {

    //Card names for navigation between pages
    public static final String SPLASH = "SPLASH";
    public static final String SIGNUP = "SIGNUP";
    public static final String SIGNIN = "SIGNIN";
    public static final String PREFERENCES_GENRES = "PREFERENCES GENRES";
    public static final String PREFERENCES_COUNTRIES = "PREFERENCES COUNTRIES";
    public static final String MOVIERESULT  = "MOVIERESULT";
    public static final String CHOOSE_TIME = "CHOOSE_TIME";
    public static final String TICKET_SUCCESS = "TICKET_SUCCESS";
    public static final String HOME_PAGE = "HOME PAGE";
    public static final String MY_TICKET = "MY TICKET";
    public static final String HISTORY = "History";
    public static final String RATE = "Rate";
    public static final String DASHBOARD = "Dashboard";

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
        root.add(new PreferencesGenres(this), PREFERENCES_GENRES);
        root.add(new PreferencesCountries(this), PREFERENCES_COUNTRIES);
        root.add(new MovieResult(this), MOVIERESULT);
        root.add(new chooseTimePage(this), CHOOSE_TIME);
        root.add(new TicketSuccess(this), TICKET_SUCCESS);
        root.add(new HomePage(this), HOME_PAGE);
        root.add(new MyTicket(this), MY_TICKET);
        root.add(new History(this), HISTORY);
        root.add(new Rate(this), RATE);
        root.add(new Dashboard(this), DASHBOARD);



        //Use root panel as the main container of this frame
        setContentPane(root);
        showPage(SPLASH);
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
                case PREFERENCES_GENRES:
                    setTitle("Preferences");
                    break;
                case PREFERENCES_COUNTRIES:
                    setTitle("Preferences");
                    break;
                case MOVIERESULT:
                    setTitle("Movie Result");
                    break;
                case CHOOSE_TIME:
                    setTitle("Choose Time");
                    break;
                case TICKET_SUCCESS:
                    setTitle("Ticket Success");
                    break;
                case HOME_PAGE:
                    setTitle("Home");
                    break;
                case MY_TICKET:
                    setTitle("My Ticket");
                    break;
                case HISTORY:
                    setTitle("History");
                    break;
                case RATE:
                    setTitle("Rate");
                    break;
                case DASHBOARD:
                    setTitle("Dashboard");
                    break;
            }
        //Display this page using CardLayout
            layout.show(root, page);
        }

    }
