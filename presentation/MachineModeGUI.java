package presentation;

import java.awt.*;
import javax.swing.*;

public class MachineModeGUI extends ResourcesGUI {

    private Timer gifTimer;
    private String bot1 = null;
    private String bot2 = null;
    private PixelButton btnHB1;
    private PixelButton btnFB1;
    private PixelButton btnEB1;
    private PixelButton btnHB2;
    private PixelButton btnFB2;
    private PixelButton btnEB2;

    private final GameControl gameControl;

    public MachineModeGUI(GameControl gameControl) {
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
            backButton();
            main_Text();
            lampPost1();
            hungryButton1();
            fearfulButton1();
            expertButton1();
            textMachine1();
            textHungry1();
            textFearful1();
            textExpert1();
            lampPost2();
            hungryButton2();
            fearfulButton2();
            expertButton2();
            textMachine2();
            textHungry2();
            textFearful2();
            textExpert2();

        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }
    
    public void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/seleccionModo.png",
         320, 30, 175, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    public void lampPost1() {
        JLabel labelLampPost = ImageUtils.createScaledImageLabel("/Resources/inicio/perchero.png",
         150, 300, 400, 100);
        contentLabel.add(labelLampPost);
        contentLabel.setComponentZOrder(labelLampPost, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void hungryButton1() {
        btnHB1 = new PixelButton("/Resources/seleccion_jugador/hungry.png", 80, 100);
        btnHB1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHB1.setBounds(485, 60, 100, 120);
        btnHB1.setDisabledIcon(btnHB1.getIcon());
        btnHB1.addActionListener(e -> {
            gameControl.setCharacter1("Hungry");
            selectCharacter("Hungry1");
        });

        contentLabel.add(btnHB1);
        contentLabel.setComponentZOrder(btnHB1, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void fearfulButton1() {
        btnFB1 = new PixelButton("/Resources/seleccion_jugador/fearful.png", 80, 80);
        btnFB1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFB1.setBounds(375, 162, 80, 80);
        btnFB1.setDisabledIcon(btnFB1.getIcon());
        btnFB1.addActionListener(e -> {
            gameControl.setCharacter1("Fearful");
            selectCharacter("Fearful1");
        });

        contentLabel.add(btnFB1);
        contentLabel.setComponentZOrder(btnFB1, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
    
    private void expertButton1() {
        btnEB1 = new PixelButton("/Resources/seleccion_jugador/expert.png", 80, 80);
        btnEB1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEB1.setBounds(495, 268, 80, 80);
        btnEB1.setDisabledIcon(btnEB1.getIcon());
        btnEB1.addActionListener(e -> {
            gameControl.setCharacter1("Expert");
            selectCharacter("Expert1");
        });

        contentLabel.add(btnEB1);
        contentLabel.setComponentZOrder(btnEB1, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textMachine1() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/maquina.png",
         140, 30, 400, 405);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textHungry1() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/hungry.png",
         80, 30, 370, 120);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textFearful1() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/fearful.png",
         80, 30, 490, 220);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textExpert1() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/expert.png",
         80, 30, 370, 310);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void lampPost2() {
        JLabel labelLampPost = ImageUtils.createScaledImageLabel("/Resources/inicio/perchero.png",
         150, 300, 100, 100);
        contentLabel.add(labelLampPost);
        contentLabel.setComponentZOrder(labelLampPost, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void hungryButton2() {
        btnHB2 = new PixelButton("/Resources/seleccion_jugador/hungry.png", 80, 100);
        btnHB2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHB2.setBounds(185, 60, 100, 120);
        btnHB2.setDisabledIcon(btnHB2.getIcon());
        btnHB2.addActionListener(e -> {
            gameControl.setCharacter2("Hungry");
            selectCharacter("Hungry2");
        });

        contentLabel.add(btnHB2);
        contentLabel.setComponentZOrder(btnHB2, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void fearfulButton2() {
        btnFB2 = new PixelButton("/Resources/seleccion_jugador/fearful.png", 80, 80);
        btnFB2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFB2.setBounds(75, 162, 80, 80);
        btnFB2.setDisabledIcon(btnFB2.getIcon());
        btnFB2.addActionListener(e -> {
            gameControl.setCharacter2("Fearful");
            selectCharacter("Fearful2");
        });

        contentLabel.add(btnFB2);
        contentLabel.setComponentZOrder(btnFB2, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
    
    private void expertButton2() {
        btnEB2 = new PixelButton("/Resources/seleccion_jugador/expert.png", 80, 80);
        btnEB2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEB2.setBounds(195, 268, 80, 80);
        btnEB2.setDisabledIcon(btnEB2.getIcon());
        btnEB2.addActionListener(e -> {
            gameControl.setCharacter2("Expert");
            selectCharacter("Expert2");
        });

        contentLabel.add(btnEB2);
        contentLabel.setComponentZOrder(btnEB2, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textMachine2() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/maquina.png",
         140, 30, 100, 405);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textHungry2() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/hungry.png",
         80, 30, 70, 120);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textFearful2() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/fearful.png",
         80, 30, 190, 220);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textExpert2() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/expert.png",
         80, 30, 70, 310);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    } 
    
    private void selectCharacter(String character) {
        if (bot1 == null) {
            bot1 = character;
            disableButtons(character);
        } else if (bot2 == null) {
            bot2 = character;
            disableButtons(character);
        }
        if (bot1 != null && bot2 != null) {
            levelSelection();
        }
    }

     private void disableButtons(String character) {
        switch (character) {
            case "Hungry1" -> {
                btnHB1.setEnabled(false);
                btnFB1.setEnabled(false);
                btnEB1.setEnabled(false);     
            }
            case "Fearful1" -> {
                btnFB1.setEnabled(false);
                btnEB1.setEnabled(false);
                btnHB1.setEnabled(false);
            }
            case "Expert1" -> {
                btnEB1.setEnabled(false);
                btnHB1.setEnabled(false);
                btnFB1.setEnabled(false);
            }
            case "Hungry2" -> {
                btnHB2.setEnabled(false);
                btnFB2.setEnabled(false);
                btnEB2.setEnabled(false);
            }
            case "Fearful2" -> {
                btnFB2.setEnabled(false);
                btnEB2.setEnabled(false);
                btnHB2.setEnabled(false);
            }
            case "Expert2" -> {
                btnEB2.setEnabled(false);
                btnHB2.setEnabled(false);
                btnFB2.setEnabled(false);
            }
        }
    }


    private void levelSelection() {
        if (gifTimer != null && gifTimer.isRunning()) {
            gifTimer.stop();
        }

        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);

            LevelSelectionGUI levelSelectionPanel = new LevelSelectionGUI(gameControl);
            parent.add(levelSelectionPanel); 
            parent.revalidate();
            parent.repaint();
        }
    }
}