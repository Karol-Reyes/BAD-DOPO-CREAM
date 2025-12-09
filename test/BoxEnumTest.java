package test;

import domain.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class BoxEnumTest {

    @Test
    public void testBoxStates() {
        // Verificar que existen los estados requeridos
        assertEquals("created", BoxState.created.name());
        assertEquals("destroyed", BoxState.destroyed.name());
        assertEquals("inactive", BoxState.inactive.name());
        
        // Verificar que hay exactamente 3 estados
        assertEquals(3, BoxState.values().length);
    }

    @Test
    public void testBoxTypes() {
        // Verificar que existe el tipo ice
        assertEquals("ice", BoxType.ice.name());
        
        // Esto es útil si agregas más tipos en el futuro para asegurar que no rompes la cuenta
        // Por ahora solo hay 1
        assertTrue(BoxType.values().length >= 1);
    }
}
