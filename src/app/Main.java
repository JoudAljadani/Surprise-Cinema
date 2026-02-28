package app;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        //From here the program start execution
        SwingUtilities.invokeLater(() -> new Appframe().setVisible(true));
    }
}