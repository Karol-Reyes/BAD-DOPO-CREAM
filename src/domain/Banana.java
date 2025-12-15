package domain;

/**
 * Representa una banana dentro del mapa del juego.
 * La banana es estatica
 */
public class Banana extends Fruit{
    
    /**
     * Constructor de Banana.
     * @param position la posición inicial de la banana
     */
    public Banana(Position position) {
        super(FruitType.banana, position, 0, true);
        this.scoreValue = 100;
    }

    /**
     * @return el valor de puntuación de la banana
     */
    @Override
    public int getScore() {
        return scoreValue;
    }
}
