package test;

import domain.*;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class BoxFactoryTest {

    private Position pos;

    @Before
    public void setUp() {
        // Asumiendo que Position tiene un constructor simple (x, y)
        // Si Position es un Mock, instáncialo aquí.
        pos = new Position(0, 0); 
    }

    @Test
    public void testCreateBoxIce() {
        // 1. Arrange & 2. Act
        Box caja = BoxFactory.createBox(BoxType.ice, pos);

        // 3. Assert
        assertNotNull("La caja no debería ser nula", caja);
        assertEquals("La clase concreta debería ser Ice", Ice.class, caja.getClass());
        assertEquals("El tipo debería ser ICE", BoxType.ice, caja.getType());
        assertEquals("La posición debería ser la asignada", pos, caja.getPosition());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBoxNullType() {
        // Debería lanzar excepción si el tipo es null
        BoxFactory.createBox(null, pos);
    }
}