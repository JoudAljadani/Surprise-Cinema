package GUI;
import codeImplementation.*;
import javax.swing.*;
import java.awt.*;

public class Appframe extends JFrame {

    // These static variables store the current session data. They are static because different pages need to access the same shared data.
    public static User currentUser; // stores the logged-in user.
    public static Movie currentMovie; // stores the movie selected for the user.
    public static Ticket currentTicket; // stores the latest ticket after booking.

    // These constants are the page names used by CardLayout.
    // Instead of writing the page name as a normal String every time,
    // I use constants to make navigation easier and reduce spelling mistakes.
    public static final String SPLASH = "SPLASH";
    public static final String SIGNUP = "SIGNUP";
    public static final String SIGNIN = "SIGNIN";
    public static final String PREFERENCES_GENRES = "PREFERENCES GENRES";
    public static final String MOVIERESULT  = "MOVIERESULT";
    public static final String CHOOSE_TIME = "CHOOSE_TIME";
    public static final String TICKET_SUCCESS = "TICKET_SUCCESS";
    public static final String HOME_PAGE = "HOME PAGE";
    public static final String MY_TICKET = "MY TICKET";
    public static final String HISTORY = "History";
    public static final String RATE = "Rate";
    public static final String DASHBOARD = "Dashboard";

    private final CardLayout layout = new CardLayout(); // used to switch between pages inside the same JFrame.
    private final JPanel root = new JPanel(layout); // The root panel holds all pages, and CardLayout decides which page is visible.

    // This page is private because only Appframe needs to control it.
    // I store it as a variable so Appframe can call pickAnotherMovie(). whenever the MovieResult page is opened.
    private MovieResult movieResultPage;


    // This constructor prepares the main application window.
    // It sets the window size, centers it on the screen, and adds all pages to the CardLayout.
    public Appframe() {
        setSize(390, 720);// Set the size of the application window.
        setLocationRelativeTo(null); // Open the window in the center of the screen.
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Close the whole program when the user closes the window.

        movieResultPage = new MovieResult(this);

        // Add all pages to the root panel.
        // Each page is connected with a constant name, so showPage() can display it later.
        root.add(new Splash(this), SPLASH);
        root.add(new SignUp(this), SIGNUP);
        root.add(new SignIn(this), SIGNIN);
        root.add(new PreferencesGenres(this), PREFERENCES_GENRES);
        root.add(movieResultPage, MOVIERESULT);
        root.add(new chooseTimePage(this), CHOOSE_TIME);
        root.add(new TicketSuccess(this), TICKET_SUCCESS);
        root.add(new HomePage(this), HOME_PAGE);
        root.add(new MyTicket(this), MY_TICKET);
        root.add(new History(this), HISTORY);
        root.add(new Rate(this), RATE);
        root.add(new Dashboard(this), DASHBOARD);

        setContentPane(root); // Set the root panel as the main content of the JFrame.
        showPage(SPLASH); // Start the application from the Splash page
    }

    // This method is responsible for page navigation.
    // It changes the window title based on the page,
    // then tells CardLayout to show the selected page.
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

            case MOVIERESULT:
                setTitle("Movie Result");
                movieResultPage.pickAnotherMovie();  // Pick a new movie every time the MovieResult page is shown (supports the Surprise Me feature)

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

        layout.show(root, page); // Show the selected page inside the root panel.

    }
}