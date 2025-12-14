package domain;

/**
 * Representa una posición en una cuadrícula mediante fila y columna.
 */
public class Position {

    private int row;
    private int col;

    /**
     * Crea una nueva posición en la fila y columna indicadas.
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Obtiene la fila de la posición.
     */
    public int getRow() { return row; }

    /**
     * Obtiene la columna de la posición.
     */
    public int getCol() { return col; }

    /**
     * Establece la fila de la posición.
     * @param row nueva fila
     */
    public void setRow(int row) { this.row = row; }

    /**
     * Establece la columna de la posición.
     * @param col nueva columna
     */
    public void setCol(int col) { this.col = col; }

    /**
     * Establece la posición a la fila y columna indicadas.
     * @param row nueva fila
     * @param col nueva columna
     */
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position translated(Direction dir) {
        return switch (dir) {
            case UP -> new Position(row - 1, col);
            case DOWN -> new Position(row + 1, col);
            case LEFT -> new Position(row, col - 1);
            case RIGHT -> new Position(row, col + 1);
            default -> new Position(row, col);
        };
    }

    /**
     * Compara esta posición con otro objeto.
     * @param obj otro objeto
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }

    /**
     * Genera un código hash para la posición.
     */
    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}