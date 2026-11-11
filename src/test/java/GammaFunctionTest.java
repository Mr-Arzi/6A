import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class GammaFunctionTest {

    @Test
    void computeIntGammaDeCincoEsVeinticuatro() {
        GammaFunction gamma = new GammaFunction();
        assertEquals(24.0, gamma.computeIntGamma(5), 1e-9);
    }

    @Test
    void computeDblGammaDeCuatroYMedio() {
        GammaFunction gamma = new GammaFunction();
        assertEquals(11.63173, gamma.computeDblGamma(4.5), 1e-4);
    }

    @Test
    void computeDblGammaDeUnoEsUno() {
        GammaFunction gamma = new GammaFunction();
        assertEquals(1.0, gamma.computeDblGamma(1.0), 1e-9);
    }

    @Test
    void computeIntGammaDeCeroLanzaExcepcion() {
        GammaFunction gamma = new GammaFunction();
        assertThrows(IllegalArgumentException.class, () -> gamma.computeIntGamma(0));
    }

    @Test
    void computeDblGammaMenorQueMedioLanzaExcepcion() {
        GammaFunction gamma = new GammaFunction();
        assertThrows(IllegalArgumentException.class, () -> gamma.computeDblGamma(0.2));
    }
}
