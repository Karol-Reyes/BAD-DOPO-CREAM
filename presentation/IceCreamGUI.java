package presentation;

import java.awt.*;
import javax.swing.*;

public class IceCreamGUI extends ResourcesGUI {

    private Timer gifTimer;
    private final GameControl gameControl;
    
    public IceCreamGUI(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    @Override
    protected void startSequence() {
        startChocolateGif();
        
        int chocolateDuration = 1700;
        gifTimer = new Timer(chocolateDuration, e -> {
            showBackground();
            addIceCreamBackground();
            addBackBackground();
            buttonSolo();
            buttonTwoPlayers();
            buttonBot();
            backButton();
            mainText();
            textPlayer();
            text1v1();
            textMachine();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }
    
    public void buttonSolo() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/One.png", 120, 300);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(50, 90, 120, 300);
        btnStart.addActionListener(e -> {
            gameControl.setGameMode("Solo");
            chooseFlavors();
        });
 
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void buttonTwoPlayers() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/Two.png", 200, 300);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(245, 90, 200, 300);
        btnStart.addActionListener(e -> {
            select1v1();
        });

        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void buttonBot() {
        PixelButton btnStart = new PixelButton("/Resources/inicio/Trolly.png", 110, 300);
        btnStart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStart.setBounds(520, 90, 110, 300);

        btnStart.addActionListener(e -> {
            gameControl.setGameMode("Machine");
            machineMode();
        });
        
        contentLabel.add(btnStart);
        contentLabel.setComponentZOrder(btnStart, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void textPlayer() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/jugador.png", 100, 30, 60, 400);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void text1v1() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/1v1.png", 60, 30, 315, 400);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void textMachine() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/maquina.png", 90, 30, 525, 395);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
    
    private void chooseFlavors() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }
        
        Container parent = getParent();
        
        if (parent != null) {
            parent.remove(this);
            
            FlavorPlayerGUI flavorPanel = new FlavorPlayerGUI(gameControl);
            parent.add(flavorPanel);
            
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private void select1v1() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);

            Mode1v1 mode1v1 = new Mode1v1(gameControl);
            parent.add(mode1v1);

            parent.revalidate();
            parent.repaint();
        }
    }

    private void machineMode() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();

        if (parent != null) {
            parent.remove(this);

            MachineModeGUI machineMode = new MachineModeGUI(gameControl);
            parent.add(machineMode);

            parent.revalidate();
            parent.repaint();
        }
    }
}
