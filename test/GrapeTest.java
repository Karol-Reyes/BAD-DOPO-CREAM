package test;

import  domain.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class GrapeTest {

    // Stub para Position
    private Position mockPos = new Position(4, 4);

    @Test
    public void testInitialProperties() {
        Grape grape = new Grape(mockPos);

        // Verificación de propiedades heredadas
        assertEquals(FruitType.grape, grape.getType());
        assertEquals(mockPos, grape.getPosition());
        
        // Verificación de propiedades específicas de Grape (configuradas en el super())
        assertEquals("Grape debe ser estática", 0, grape.getSpeed());
        assertTrue("Grape debe ser estática", grape.isStatic());
    }

    @Test
    public void testUpdateDoesNothing() {
        // Como el update no altera el estado de la Grape, no debería cambiar nada.
        Grape grape = new Grape(mockPos);
        Position initialPos = grape.getPosition();
        FruitState initialState = grape.getState();

        grape.update();

        assertEquals("El estado no debe cambiar", initialState, grape.getState());
        assertEquals("La posición no debe cambiar", initialPos, grape.getPosition());
    }
}
