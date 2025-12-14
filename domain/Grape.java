package domain;

/**
 * Representa una uva dentro del mapa del juego.
 * La uva es estatica
 */
public class Grape extends Fruit{
    
    /**
     * Constructor de Grape.
     * @param position la posición inicial de la uva
     */
    public Grape(Position position) {
        super(FruitType.grape, position, 0, true);
        this.scoreValue = 50;
    }

    /**
     * @return el valor de puntuación de la uva
     */
    @Override
    public int getScore() {
        return scoreValue;
    }
}