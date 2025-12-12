package domain;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Mapa lógico del juego, compuesto por capas de bloques, frutas, enemigos y jugadores.
 */
public class GameMap {

    private final int rows;
    private final int cols;
    private final int cellSize;

    private final Boxy[][] blocks;
    private final Fruit[][] fruits;
    private final Enemy[][] enemies;
    private final IceCream[][] players;
    private final BoxState[][] initialBlockStates;

    /**
     * Crea un mapa con el número dado de filas y columnas.
     * @param rows cantidad de filas
     * @param cols cantidad de columnas
     */
    public GameMap(int rows, int cols) {

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int side = Math.min(screen.width, screen.height);

        this.rows = Math.max(3, rows);
        this.cols = Math.max(3, cols);
        this.cellSize = Math.max(8, side / 18);

        blocks = new Boxy[this.rows][this.cols];
        fruits = new Fruit[this.rows][this.cols];
        enemies = new Enemy[this.rows][this.cols];
        players = new IceCream[this.rows][this.cols];
        initialBlockStates = new BoxState[this.rows][this.cols];

        /*
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                Position pos = new Position(r, c);
                blocks[r][c] = new Ice(pos, BoxState.inactive);
            }
        }
        */
    }

    /**

    * Reemplaza un bloque en una posición específica
    * @param pos posición del bloque
    * @param block nuevo bloque a colocar
    */
    public void setBlock(Position pos, Boxy block) {
        if (isValid(pos)) {
            blocks[pos.getRow()][pos.getCol()] = block;
        }
    }

    /**
     * Verifica si una posición está dentro del mapa.
     * @param p posición a evaluar
     * @return true si es válida
     */
    public boolean isValid(Position p) {
        int r = p.getRow(), c = p.getCol();
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    /**
     * Indica si una posición está bloqueada por un bloque creado.
     * @param p posición a evaluar
     * @return true si no se puede atravesar
     */
    public boolean isBlocked(Position p) {
        Boxy b = blocks[p.getRow()][p.getCol()];
    
        if (b != null && (b.getType() == BoxType.bonfire || b.getType() == BoxType.fire)) {
            return false; // Se puede caminar sobre ellas
        }
        return (b != null && b.isCreated()) || (b != null && !b.canWalk());
    }

    /**
     * Indica si una posición contiene un enemigo.
     * @param p posición a evaluar
     * @return true si hay un enemigo
     */
    public boolean hasEnemy(Position p) {
        return enemies[p.getRow()][p.getCol()] != null;
    }

    /**
     * Indica si una posición contiene una fruta.
     * @param p posición a evaluar
     * @return true si hay una fruta
     */
    public boolean hasFruit(Position p) {
        return fruits[p.getRow()][p.getCol()] != null;
    }

    /**
     * Indica si una posición contiene un jugador.
     * @param p posición a evaluar
     * @return true si hay un jugador vivo
     */
    public boolean hasPlayer(Position p) {
        IceCream pl = players[p.getRow()][p.getCol()];
        return pl != null && pl.isAlive();
    }

    /**
     * Mueve un enemigo en la dirección indicada.
     * @param e enemigo a mover
     * @param d dirección de movimiento
     * @return true si se movió
     */
    public boolean moveEnemy(Enemy e, Direction d) {

        Position oldP = e.getPosition();
        Position newP = new Position(
            oldP.getRow() + d.getRowDelta(),
            oldP.getCol() + d.getColDelta()
        );

        if (!isValid(newP)) return false;
        if (isBlocked(newP)) return false;
        if (hasEnemy(newP)) return false;
        if (hasPlayer(newP)) {
            IceCream p = getPlayer(newP);
            p.die();
            /*
            enemies[oldP.getRow()][oldP.getCol()] = null;
            enemies[newP.getRow()][newP.getCol()] = e;
        
            e.setPosition(newP);
            return true;
            */
        }

        enemies[oldP.getRow()][oldP.getCol()] = null;
        enemies[newP.getRow()][newP.getCol()] = e;

        e.setPosition(newP);
        return true;
    }

    /**
     * Mueve un jugador en la dirección indicada.
     * @param p jugador a mover
     * @param d dirección de movimiento
     * @return true si se movió
     */
    public boolean movePlayer(IceCream p, Direction d) {

        Position oldP = p.getPosition();
        Position newP = new Position(
            oldP.getRow() + d.getRowDelta(),
            oldP.getCol() + d.getColDelta()
        );

        if (!isValid(newP)) return false;
        if (isBlocked(newP)) return false;

        players[oldP.getRow()][oldP.getCol()] = null;
        players[newP.getRow()][newP.getCol()] = p;

        p.setPosition(newP);
        return true;
    }

    /**
     * Crea un bloque en la posición especificada.
     * @param pos posición del bloque
     */
    public void placeWall(Position pos) {
        if (!isValid(pos)) return;
        Boxy b = blocks[pos.getRow()][pos.getCol()];
        if (b != null && b.canBeCreated()) b.create();
    }

    /**
     * Destruye un bloque en la posición especificada.
     * @param pos posición del bloque
     */
    public void clearBlock(Position pos) {
        if (!isValid(pos)) return;
        Boxy b = blocks[pos.getRow()][pos.getCol()];
        if (b != null && b.canBeDestroyed()) b.destroy();
    }

    /**
     * Inserta un enemigo en el mapa.
     * @param e enemigo a agregar
     */
    public void addEnemy(Enemy e) {
        Position pos = e.getPosition();
        if (isValid(pos)) {
            enemies[pos.getRow()][pos.getCol()] = e;
        }
    }

    /**
     * Inserta una fruta en el mapa.
     * @param f fruta a agregar
     */
    public void addFruit(Fruit f) {
        Position pos = f.getPosition();
        if (isValid(pos)) {
            fruits[pos.getRow()][pos.getCol()] = f;
        }
    }

    /**
     * Inserta un jugador en el mapa.
     * @param p jugador a agregar
     */
    public void addPlayer(IceCream p) {
        Position pos = p.getPosition();
        if (isValid(pos)) {
            players[pos.getRow()][pos.getCol()] = p;
        }
    }

    /**
     * Quita una fruta del mapa.
     * @param pos posición de la fruta
     */
    public void removeFruit(Position pos) {
        if (isValid(pos)) {
            fruits[pos.getRow()][pos.getCol()] = null;
        }
    }

    /**
     * Limpia todas las entidades dinámicas del mapa.
     */
    public void clearEntities() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                enemies[r][c] = null;
                fruits[r][c] = null;
                players[r][c] = null;
            }
        }
    }

    /**
     * Restaura los bloques al estado original del nivel.
     */
    public void resetBlocks() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Boxy b = blocks[r][c];
                if (b != null) {
                    BoxState originalState = initialBlockStates[r][c];
                    if (originalState == BoxState.created && !b.isCreated()) {
                        b.create();
                    } else if (originalState != BoxState.created && b.isCreated()) {
                        b.destroy();
                    }
                }
            }
        }
    }

    /**
     * Guarda el estado inicial de los bloques del nivel.
     */
    public void saveInitialBlockStates() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Boxy b = blocks[r][c];
                initialBlockStates[r][c] = (b != null) ? b.getState() : BoxState.inactive;
            }
        }
    }

    /** @return número de filas. */
    public int getRows() { return rows; }

    /** @return número de columnas. */
    public int getCols() { return cols; }

    /** @return tamaño de cada celda del mapa. */
    public int getCellSize() { return cellSize; }

    /** @return bloque en la posición dada. */
    public Boxy getBlock(Position pos) { return blocks[pos.getRow()][pos.getCol()]; }

    /** @return fruta en la posición dada. */
    public Fruit getFruit(Position pos) { return fruits[pos.getRow()][pos.getCol()]; }

    /** @return enemigo en la posición dada. */
    public Enemy getEnemy(Position pos) { return enemies[pos.getRow()][pos.getCol()]; }

    /** @return jugador en la posición dada. */
    public IceCream getPlayer(Position pos) { return players[pos.getRow()][pos.getCol()]; }
    
}