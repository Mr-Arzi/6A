import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LogicTest {

    private final Logic logic = new Logic();

    @Test
    void tabla1Prueba1() {
        assertEquals(0.55338, logic.findX(0.20, 6), 1e-5);
    }

    @Test
    void tabla1Prueba2() {
        assertEquals(1.75305, logic.findX(0.45, 15), 1e-5);
    }

    @Test
    void tabla1Prueba3() {
        assertEquals(4.60409, logic.findX(0.495, 4), 1e-5);
    }

    @Test
    void pChicoObligaAPasarPorXCero() {
        double x = logic.findX(0.001, 10);
        assertEquals(0.00257, x, 1e-5);
    }

    @Test
    void pCercanoAMedio() {
        // Con p cerca de 0.5 la integral solo se acerca cuando x es grande
        double x = logic.findX(0.4999, 30);
        assertTrue(x > 3.0);
    }

    @Test
    void pMayorAMedioSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> logic.findX(0.6, 6));
    }

    @Test
    void pNegativoOCeroSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> logic.findX(-0.1, 6));
        assertThrows(IllegalArgumentException.class, () -> logic.findX(0.0, 6));
    }

    @Test
    void dofInvalidoSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> logic.findX(0.2, 0));
    }
}