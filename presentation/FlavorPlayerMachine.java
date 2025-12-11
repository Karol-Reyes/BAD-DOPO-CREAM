package presentation;

import java.awt.*;
import javax.swing.*;

public class FlavorPlayerMachine extends ResourcesGUI {
    private Timer gifTimer;
    private String player = null;
    private String bot = null;

    private PixelButton btnVH;
    private PixelButton btnFH;
    private PixelButton btnCH;
    private PixelButton btnHB;
    private PixelButton btnFB;
    private PixelButton btnEB;

    private final GameControl gameControl;

    public FlavorPlayerMachine(GameControl gameControl) {
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
            coneImage();
            lampPost();
            vanillaButton();
            strawberryButton();
            chocolateButton();
            hungryButton();
            fearfulButton();
            expertButton();
            textPlayer();
            textMachine();
            textHungry();
            textFearful();
            textExpert();
        });
        gifTimer.setRepeats(false);
        gifTimer.start();
    }

    private void main_Text() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/seleccionarSabor.png",
         320, 30, 175, 45);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void coneImage() {
        JLabel labelFlavors = ImageUtils.createScaledImageLabel("/Resources/seleccion_jugador/cono.png",
         265, 200, 50, 190);
        contentLabel.add(labelFlavors);
        contentLabel.setComponentZOrder(labelFlavors, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void vanillaButton() {
        btnVH = new PixelButton("/Resources/seleccion_jugador/vainilla.png", 100, 120);
        btnVH.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVH.setBounds(135, 110, 100, 120);
        btnVH.setDisabledIcon(btnVH.getIcon());
        btnVH.addActionListener(e -> {
            gameControl.setCharacter1("Vanilla");
            selectCharacter("Vanilla");
        });

        contentLabel.add(btnVH);
        contentLabel.setComponentZOrder(btnVH, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void strawberryButton() {
        btnFH = new PixelButton("/Resources/seleccion_jugador/fresa.png", 100, 120);
        btnFH.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFH.setBounds(215, 95, 100, 120);
        btnFH.setDisabledIcon(btnFH.getIcon());
        btnFH.addActionListener(e -> {
            gameControl.setCharacter1("Strawberry");
            selectCharacter("Strawberry");
        });

        contentLabel.add(btnFH);
        contentLabel.setComponentZOrder(btnFH, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void chocolateButton() {
        btnCH = new PixelButton("/Resources/seleccion_jugador/chocolate.png", 100, 120);
        btnCH.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCH.setBounds(50, 95, 100, 120);
        btnCH.setDisabledIcon(btnCH.getIcon());
        btnCH.addActionListener(e -> {
            gameControl.setCharacter1("Chocolate");
            selectCharacter("Chocolate");
        });

        contentLabel.add(btnCH);
        contentLabel.setComponentZOrder(btnCH, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void lampPost() {
        JLabel labelLampPost = ImageUtils.createScaledImageLabel("/Resources/inicio/perchero.png", 
        150, 300,400,100);
        contentLabel.add(labelLampPost);
        contentLabel.setComponentZOrder(labelLampPost, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void hungryButton() {
        btnHB = new PixelButton("/Resources/seleccion_jugador/hungry.png", 80, 100);
        btnHB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHB.setBounds(485, 60, 100, 120);
        btnHB.setDisabledIcon(btnHB.getIcon());
        btnHB.addActionListener(e -> {
            gameControl.setCharacter2("Hungry");
            selectCharacter("Hungry");
        });

        contentLabel.add(btnHB);
        contentLabel.setComponentZOrder(btnHB, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void fearfulButton() {
        btnFB = new PixelButton("/Resources/seleccion_jugador/fearful.png", 80, 80);
        btnFB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFB.setBounds(375, 162, 80, 80);
        btnFB.setDisabledIcon(btnFB.getIcon());
        btnFB.addActionListener(e -> {
            gameControl.setCharacter2("Fearful");
            selectCharacter("Fearful");
        });

        contentLabel.add(btnFB);
        contentLabel.setComponentZOrder(btnFB, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }
    
    private void expertButton() {
        btnEB = new PixelButton("/Resources/seleccion_jugador/expert.png", 80, 80);
        btnEB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEB.setBounds(495, 268, 80, 80);
        btnEB.setDisabledIcon(btnEB.getIcon());
        btnEB.addActionListener(e -> {
            gameControl.setCharacter2("Expert");
            selectCharacter("Expert");
        });

        contentLabel.add(btnEB);
        contentLabel.setComponentZOrder(btnEB, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textMachine() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/maquina.png",
         140, 30, 400, 405);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textPlayer() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/Jugador1.png",
         140, 30, 140, 405);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textHungry() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/hungry.png",
         80, 30, 370, 120);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textFearful() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/fearful.png",
         80, 30, 490, 220);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void textExpert() {
        JLabel textLabel = ImageUtils.createScaledImageLabel("/Resources/textos/expert.png",
         80, 30, 370, 310);
        contentLabel.add(textLabel);
        contentLabel.setComponentZOrder(textLabel, 0);
        contentLabel.revalidate();
        contentLabel.repaint();
    }

    private void selectCharacter(String character) {
        if (player == null) {
            player = character;
            disableButtons(character);
        } else if (bot == null) {
            bot = character;
            disableButtons(character);
        }
        if (player != null && bot != null) {
            levelSelection();
        }
    }

    private void disableButtons(String character) {
        switch (character) {
            case "Vanilla" -> {
                btnVH.setEnabled(false);
                btnFH.setEnabled(false);
                btnCH.setEnabled(false);
            }
            case "Strawberry" -> {
                btnVH.setEnabled(false);
                btnFH.setEnabled(false);
                btnCH.setEnabled(false);
            }
            case "Chocolate" -> {
                btnVH.setEnabled(false);
                btnFH.setEnabled(false);
                btnCH.setEnabled(false);
            }
            case "Hungry" -> {
                btnHB.setEnabled(false);
                btnFB.setEnabled(false);
                btnEB.setEnabled(false);
            }
            case "Fearful" -> {
                btnHB.setEnabled(false);
                btnFB.setEnabled(false);
                btnEB.setEnabled(false);
            }
            case "Expert" -> {
                btnHB.setEnabled(false);
                btnFB.setEnabled(false);
                btnEB.setEnabled(false);
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