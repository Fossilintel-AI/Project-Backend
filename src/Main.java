import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(MainWindow::new);
        System.out.println("Hello world!");
    }
}