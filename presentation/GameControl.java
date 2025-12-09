package presentation;
import domain.GameConfig;

public class GameControl {
    private String gameMode;
    private String character1;
    private String character2;
    private int selectedLevel;
    private String selectedFruit1;
    private String selectedFruit2;
    private String selectedEnemy1;
    private String selectedEnemy2;
    private String selectedObjet;

    public String getGameMode() {
        return gameMode;
    } 
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getCharacter1() {
        return character1;
    }
    public void setCharacter1(String character1) {
        this.character1 = character1;
    }

    public String getCharacter2() {
        return character2;
    }
    public void setCharacter2(String character2) {
        this.character2 = character2;
    }

    public int getSelectedLevel() {
        return selectedLevel;
    }
    public void setSelectedLevel(int selectedLevel) {
        this.selectedLevel = selectedLevel;
    }

    public String getSelectedFruit1() {
        return selectedFruit1;
    }
    public void setSelectedFruit1(String selectedFruit1) {
        this.selectedFruit1 = selectedFruit1;
    }

    public String getSelectedFruit2() {
        return selectedFruit2;
    }
    public void setSelectedFruit2(String selectedFruit2) {
        this.selectedFruit2 = selectedFruit2;
    }

    public String getSelectedEnemy1() {
        return selectedEnemy1;
    }
    public void setSelectedEnemy1(String selectedEnemy1) {
        this.selectedEnemy1 = selectedEnemy1;
    }

    public String getSelectedEnemy2() {
        return selectedEnemy2;
    }
    public void setSelectedEnemy2(String selectedEnemy2) {
        this.selectedEnemy2 = selectedEnemy2;
    }

    public String getSelectedObjet() {
        return selectedObjet;
    }
    public void setSelectedObjet(String selectedObjet) {
        this.selectedObjet = selectedObjet;
    }

    /**
     * Convierte la configuración de presentation a domain
     */
    public GameConfig toGameConfig() {
        return new GameConfig(
            this.gameMode,
            this.character1,
            this.character2,
            this.selectedLevel,
            this.selectedFruit1,
            this.selectedFruit2,
            this.selectedEnemy1,
            this.selectedEnemy2,
            this.selectedObjet
        );
    }

    public void printSelections() {
        System.out.println("===== CONFIGURACIÓN DEL JUEGO =====");
        System.out.println("Modo de juego: " + gameMode);
        System.out.println("Personaje 1: " + character1);
        System.out.println("Personaje 2: " + character2);
        System.out.println("Nivel seleccionado: " + selectedLevel);
        System.out.println("Fruta 1: " + selectedFruit1);
        System.out.println("Fruta 2: " + selectedFruit2);
        System.out.println("Enemigo 1: " + selectedEnemy1);
        System.out.println("Enemigo 2: " + selectedEnemy2);
        System.out.println("Objeto seleccionado: " + selectedObjet);
        System.out.println("===================================");
    }
}
