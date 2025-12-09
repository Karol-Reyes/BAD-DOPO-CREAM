package test;

import domain.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class BananaTest {

    // Stub para Position
    private Position mockPos = new Position(3, 3);

    @Test
    public void testInitialProperties() {
        Banana banana = new Banana(mockPos);

        // Verificación de propiedades heredadas
        assertEquals(FruitType.banana, banana.getType());
        assertEquals(mockPos, banana.getPosition());
        
        // Verificación de propiedades específicas de Banana (configuradas en el super())
        assertEquals("Banana debe ser estática", 0, banana.getSpeed());
        assertTrue("Banana debe ser estática", banana.isStatic());
    }

    @Test
    public void testUpdateDoesNothing() {
        // Como el update no altera el estado de la Banana, no debería cambiar nada.
        Banana banana = new Banana(mockPos);
        Position initialPos = banana.getPosition();
        FruitState initialState = banana.getState();

        banana.update();

        assertEquals("El estado no debe cambiar", initialState, banana.getState());
        assertEquals("La posición no debe cambiar", initialPos, banana.getPosition());
        // El método update por defecto está vacío, solo verificamos que no introduzca efectos secundarios.
    }
}