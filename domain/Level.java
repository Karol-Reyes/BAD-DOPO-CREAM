package domain;

public abstract class Level {

    protected int rows;
    protected int cols;
    protected GameMap map;

    public Level(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.map = new GameMap(rows, cols);
    }

    /** Crea la estructura física del nivel (bordes, bloques, áreas). */
    protected abstract void buildStructure();

    /** Devuelve el mapa ya construido. */
    public GameMap getMap() {
        return map;
    }
}
