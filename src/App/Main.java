package App;
import codeImplementation.*;
import GUI.*;
import javax.swing.SwingUtilities;


//This class is to run the gui program
public class Main {
    public static void main(String[] args) {

        //Create DB + tables
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
        //prepare MoviesSystem
        AppManager.prepareMovieSystem();

        //From here the program start execution
        SwingUtilities.invokeLater(() -> new Appframe().setVisible(true));
    }
}