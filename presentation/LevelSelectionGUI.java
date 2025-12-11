package presentation;
 
import java.awt.*;
import javax.swing.*;
 
public class LevelSelectionGUI extends ResourcesGUI {
    private Timer gifTimer;
    private static final int buttonWidth = 100;
    private static final int buttonHeight = 100;
    private static final int buttonY = 210;
 
    private final GameControl gameControl;
 
    public LevelSelectionGUI(GameControl gameControl) {
        this.gameControl = gameControl;
    }
 
    @Override
    protected void startSequence() {
        startChocolateGif();
 
        int chocolateDuration = 1700;
        gifTimer = new Timer(chocolateDuration, e -> {
            showBackground();
            addLevelBackground();
            addBackBackground();
            backButton();
            levelText();
            levelOneButton();
            levelTwoButton();
            levelThreeButton();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }
 
    public void addLevelBackground() {
        JLabel backgroundLabel = ImageUtils.createScaledImageLabel("/Resources/inicio/fondo_opciones.png", 640, 280, 20, 125);
        contentLabel.add(backgroundLabel);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
 
    private void levelText() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/seleccionNivel.png", 320, 30, 175, 135);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
 
    public void levelOneButton() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/botonUno.png", buttonWidth, buttonHeight);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(95, buttonY, buttonWidth, buttonHeight);
        btnStart.addActionListener(e -> {
            gameControl.setSelectedLevel(1);
            thefruits();
        });
 
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
 
    private void levelTwoButton() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/botonDos.png", buttonWidth, buttonHeight);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(290, buttonY, buttonWidth, buttonHeight);
        btnStart.addActionListener(e -> {
            gameControl.setSelectedLevel(2);
            thefruits();
        });
 
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
 
    private void levelThreeButton() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/botonTres.png", buttonWidth, buttonHeight);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(485, buttonY, buttonWidth, buttonHeight);
        btnStart.addActionListener(e -> {
            gameControl.setSelectedLevel(3);
            thefruits();
        });
 
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
 
    private void thefruits(){
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }
 
        Container parent = getParent();
 
        if (parent != null) {
            parent.remove(this);
 
            FruitsGUI chooseFruits = new FruitsGUI(gameControl);
            parent.add(chooseFruits);
 
            parent.revalidate();
            parent.repaint();
        }
    }
}