package presentation;
import domain.GameConfig;

/**
 * Clase que controla la configuración del juego.
 * Permite establecer y obtener las selecciones realizadas por el usuario
 * en cuanto a modo de juego, personajes, nivel, frutas, enemigos y objetos.
 */
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

    /**
     * Constructor de la clase GameControl.
     * Inicializa los atributos con valores predeterminados.
     */
    public String getGameMode() {
        return gameMode;
    } 

    /**
     * Establece el modo de juego seleccionado.
     */
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Obtiene el personaje 1 seleccionado.
     */
    public String getCharacter1() {
        return character1;
    }

    /**
     * Establece el personaje 1 seleccionado.
     */
    public void setCharacter1(String character1) {
        this.character1 = character1;
    }

    /**
     * Obtiene el personaje 2 seleccionado.
     */
    public String getCharacter2() {
        return character2;
    }

    /**
     * Establece el personaje 2 seleccionado.
     */
    public void setCharacter2(String character2) {
        this.character2 = character2;
    }

    /**
     * Obtiene el nivel seleccionado.
     */
    public int getSelectedLevel() {
        return selectedLevel;
    }

    /**
     * Establece el nivel seleccionado.
     */
    public void setSelectedLevel(int selectedLevel) {
        this.selectedLevel = selectedLevel;
    }

    /**
     * Obtiene la fruta 1 seleccionada.
     */
    public String getSelectedFruit1() {
        return selectedFruit1;
    }

    /**
     * Establece la fruta 1 seleccionada.
     */
    public void setSelectedFruit1(String selectedFruit1) {
        this.selectedFruit1 = selectedFruit1;
    }

    /**
     * Obtiene la fruta 2 seleccionada.
     */
    public String getSelectedFruit2() {
        return selectedFruit2;
    }

    /**
     * Establece la fruta 2 seleccionada.
     */
    public void setSelectedFruit2(String selectedFruit2) {
        this.selectedFruit2 = selectedFruit2;
    }

    /**
     * Obtiene el enemigo 1 seleccionado.
     */
    public String getSelectedEnemy1() {
        return selectedEnemy1;
    }

    /**
     * Establece el enemigo 1 seleccionado.
     */
    public void setSelectedEnemy1(String selectedEnemy1) {
        this.selectedEnemy1 = selectedEnemy1;
    }

    /**
     * Obtiene el enemigo 2 seleccionado.
     */
    public String getSelectedEnemy2() {
        return selectedEnemy2;
    }

    /**
     * Establece el enemigo 2 seleccionado.
     */
    public void setSelectedEnemy2(String selectedEnemy2) {
        this.selectedEnemy2 = selectedEnemy2;
    }

    /**
     * Obtiene el objeto seleccionado.
     */
    public String getSelectedObjet() {
        return selectedObjet;
    }

    /**
     * Establece el objeto seleccionado.
     */
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

    /**
     * Imprime en consola las selecciones actuales del juego.
     */
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