package test;

import domain.*;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class IceTest {

    private Ice iceBlock;
    private Position position;

    @Before
    public void setUp() {
        position = new Position(1, 1);
        iceBlock = new Ice(position);
    }

    @Test
    public void testEstadoInicial() {
        assertEquals("El estado inicial debe ser inactive", BoxState.inactive, iceBlock.getState());
        assertFalse("No debería estar creado al inicio", iceBlock.isCreated());
    }

    @Test
    public void testCanWalk() {
        // Si NO está creado (inactive), se puede caminar sobre él (es suelo)
        assertTrue("Debería poder caminarse si está inactivo", iceBlock.canWalk());

        // Si está creado (es un obstáculo), NO se puede caminar
        iceBlock.create();
        assertFalse("No debería poder caminarse si está creado", iceBlock.canWalk());
    }

    @Test
    public void testCreateAndDestroy() {
        // Probar creación
        assertTrue("Debería poder crearse si está inactivo", iceBlock.canBeCreated());
        iceBlock.create();
        assertEquals(BoxState.created, iceBlock.getState());
        
        // Probar destrucción
        assertTrue("Debería poder destruirse si está creado", iceBlock.canBeDestroyed());
        iceBlock.destroy();
        assertEquals(BoxState.destroyed, iceBlock.getState());
    }
    
    @Test
    public void testSpriteKey() {
        iceBlock.create();
        assertEquals("ice_created", iceBlock.getSpriteKey());
    }
}
