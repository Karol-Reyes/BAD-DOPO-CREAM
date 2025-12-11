package presentation;

import java.awt.*;
import javax.swing.*;

public class BadDOPOCreamGUI extends JFrame {

    private JPanel mainPanel;
    private JLabel gifLabel;
    private final IntroGUI intro;   
    private static final int SCREEN_WIDTH = 690;
    private static final int SCREEN_HEIGHT = 690;

    public BadDOPOCreamGUI() {
        super("badDOPOCream");
        prepareElements();
        prepareActions();

        intro = new IntroGUI(gifLabel, () -> showMenu());
        intro.startIntro();
    }

    private void prepareElements() {
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);  
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        gifLabel = new JLabel();
        gifLabel.setHorizontalAlignment(JLabel.CENTER);
        gifLabel.setVerticalAlignment(JLabel.CENTER);

        mainPanel.add(gifLabel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void prepareActions() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void showMenu() {
        mainPanel.removeAll();
        MenuGUI menu = new MenuGUI();
        mainPanel.add(menu, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
        
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BadDOPOCreamGUI window = new BadDOPOCreamGUI();
            window.setVisible(true);
        });
    }
}
