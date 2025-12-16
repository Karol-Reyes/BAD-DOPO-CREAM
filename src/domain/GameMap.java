package domain;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Representa el mapa lógico del juego y gestiona bloques, frutas,
 * enemigos y jugadores.
 */
public class GameMap {

    private final int rows;
    private final int cols;
    private final int cell;
    private final Boxy[][] blocks;
    private final Fruit[][] fruits;
    private final Enemy[][] enemies;
    private final IceCream[][] players;
    private final BoxState[][] baseStates;

    /**
     * Crea un mapa con el tamaño indicado.
     * @param rows número de filas
     * @param cols número de columnas
     */
    public GameMap(int rows, int cols) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int side = Math.min(screen.width, screen.height);

        this.rows = Math.max(3, rows);
        this.cols = Math.max(3, cols);
        this.cell = Math.max(8, side / 18);

        blocks = new Boxy[this.rows][this.cols];
        fruits = new Fruit[this.rows][this.cols];
        enemies = new Enemy[this.rows][this.cols];
        players = new IceCream[this.rows][this.cols];
        baseStates = new BoxState[this.rows][this.cols];
    }

    /**
     * Asigna un bloque a una posición del mapa.
     * @param pos posición destino
     * @param block bloque a colocar
     */
    public void setBlock(Position pos, Boxy block) {
        if (isValid(pos)) {
            blocks[pos.getRow()][pos.getCol()] = block;
        }
    }

    /** 
     * Guarda el estado inicial de los bloques del nivel. 
     */ 
    public void saveInitialBlockStates() { 
        for (int r = 0; r < rows; r++) { 
            for (int c = 0; c < cols; c++) { 
                Boxy b = blocks[r][c]; 
                baseStates[r][c] = (b != null) ? b.getState() : BoxState.inactive; 
            } 
        } 
    }

    /**
     * Elimina todas las frutas del mapa.
     */
    public void removeAllFruits() { 
        for (int r = 0; r < rows; r++) { 
            for (int c = 0; c < cols; c++) { 
                fruits[r][c] = null; 
            } 
        } 
    }

    /**
     * Verifica si una posición pertenece al mapa.
     * @param pos posición a validar
     * @return true si es válida
     */
    public boolean isValid(Position pos) {
        int r = pos.getRow();
        int c = pos.getCol();
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    /**
     * Indica si una posición está bloqueada.
     * @param pos posición a evaluar
     * @return true si no se puede atravesar
     */
    public boolean isBlocked(Position pos) {
        Boxy b = blocks[pos.getRow()][pos.getCol()];
        if (b == null) return false;
        if (b.getType() == BoxType.fire || b.getType() == BoxType.bonfire) return false;
        return b.isCreated() || !b.canWalk();
    }

    /**
     * Indica si hay un enemigo en la posición.
     * @param pos posición a evaluar
     * @return true si existe un enemigo
     */
    public boolean hasEnemy(Position pos) {
        return enemies[pos.getRow()][pos.getCol()] != null;
    }

    /**
     * Indica si hay una fruta en la posición.
     * @param pos posición a evaluar
     * @return true si existe una fruta
     */
    public boolean hasFruit(Position pos) {
        return fruits[pos.getRow()][pos.getCol()] != null;
    }

    /**
     * Indica si hay un jugador vivo en la posición.
     * @param pos posición a evaluar
     * @return true si existe un jugador vivo
     */
    public boolean hasPlayer(Position pos) {
        IceCream p = players[pos.getRow()][pos.getCol()];
        return p != null && p.isAlive();
    }

    /**
     * Mueve un enemigo en una dirección.
     * @param e enemigo a mover
     * @param d dirección de movimiento
     * @return true si el movimiento fue exitoso
     */
    public boolean moveEnemy(Enemy e, Direction d) {
        Position from = e.getPosition();
        Position to = new Position(
                from.getRow() + d.getRowDelta(),
                from.getCol() + d.getColDelta()
        );

        if (!isValid(to)) return false;
        if (isBlocked(to)) return false;
        if (hasEnemy(to)) return false;

        if (hasPlayer(to)) {
            IceCream p = getPlayer(to);
            p.die();
        }

        enemies[from.getRow()][from.getCol()] = null;
        enemies[to.getRow()][to.getCol()] = e;
        e.setPosition(to);

        return true;
    }

    /**
     * Mueve un jugador en una dirección.
     * @param p jugador a mover
     * @param d dirección de movimiento
     * @return true si el movimiento fue exitoso
     */
    public boolean movePlayer(IceCream p, Direction d) {
        Position from = p.getPosition();
        Position to = new Position(
                from.getRow() + d.getRowDelta(),
                from.getCol() + d.getColDelta()
        );

        if (!isValid(to)) return false;
        if (isBlocked(to)) return false;

        players[from.getRow()][from.getCol()] = null;
        players[to.getRow()][to.getCol()] = p;
        p.setPosition(to);

        return true;
    }

    /**
     * Crea un bloque en una posición.
     * @param pos posición del bloque
     */
    public void placeBlock(Position pos) {
        if (!isValid(pos)) return;
        Boxy b = blocks[pos.getRow()][pos.getCol()];
        if (b != null && b.canBeCreated()) b.create();
    }

    /**
     * Destruye un bloque en una posición.
     * @param pos posición del bloque
     */
    public void clearBlock(Position pos) {
        if (!isValid(pos)) return;
        Boxy b = blocks[pos.getRow()][pos.getCol()];
        if (b != null && b.canBeDestroyed()) b.destroy();
    }

    /**
     * Agrega un enemigo al mapa.
     * @param e enemigo a agregar
     */
    public void addEnemy(Enemy e) {
        Position p = e.getPosition();
        if (isValid(p)) enemies[p.getRow()][p.getCol()] = e;
    }

    /**
     * Agrega una fruta al mapa.
     * @param f fruta a agregar
     */
    public void addFruit(Fruit f) {
        Position p = f.getPosition();
        if (isValid(p)) fruits[p.getRow()][p.getCol()] = f;
    }

    /**
     * Agrega un jugador al mapa.
     * @param p jugador a agregar
     */
    public void addPlayer(IceCream p) {
        Position pos = p.getPosition();
        if (isValid(pos)) players[pos.getRow()][pos.getCol()] = p;
    }

    /**
     * Elimina una fruta del mapa.
     * @param pos posición de la fruta
     */
    public void removeFruit(Position pos) {
        if (isValid(pos)) fruits[pos.getRow()][pos.getCol()] = null;
    }

    /**
     * Elimina todas las entidades dinámicas.
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
     * Restaura los bloques a su estado inicial.
     */
    public void resetBlocks() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Boxy b = blocks[r][c];
                if (b == null) continue;
                BoxState s = baseStates[r][c];
                if (s == BoxState.created && !b.isCreated()) b.create();
                if (s != BoxState.created && b.isCreated()) b.destroy();
            }
        }
    }

    /**
     * Guarda el estado inicial de los bloques.
     */
    public void saveBaseBlocks() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Boxy b = blocks[r][c];
                baseStates[r][c] = b != null ? b.getState() : BoxState.inactive;
            }
        }
    }

    /**
     * Obtiene el número de filas.
     * @return filas del mapa
     */
    public int getRows() {
        return rows;
    }

    /**
     * Obtiene el número de columnas.
     * @return columnas del mapa
     */
    public int getCols() {
        return cols;
    }

    /**
     * Obtiene el tamaño de una celda.
     * @return tamaño de celda
     */
    public int getCell() {
        return cell;
    }

    /**
     * Obtiene el bloque en una posición.
     * @param pos posición solicitada
     * @return bloque correspondiente
     */
    public Boxy getBlock(Position pos) {
        return blocks[pos.getRow()][pos.getCol()];
    }

    /**
     * Obtiene la fruta en una posición.
     * @param pos posición solicitada
     * @return fruta correspondiente
     */
    public Fruit getFruit(Position pos) {
        return fruits[pos.getRow()][pos.getCol()];
    }

    /**
     * Obtiene el enemigo en una posición.
     * @param pos posición solicitada
     * @return enemigo correspondiente
     */
    public Enemy getEnemy(Position pos) {
        return enemies[pos.getRow()][pos.getCol()];
    }

    /**
     * Obtiene el jugador en una posición.
     * @param pos posición solicitada
     * @return jugador correspondiente
     */
    public IceCream getPlayer(Position pos) {
        return players[pos.getRow()][pos.getCol()];
    }

    /**
     * Elimina todas las frutas del mapa.
     */
    public void clearFruits() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                fruits[r][c] = null;
            }
        }
    }   
}