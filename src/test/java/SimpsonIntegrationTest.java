import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SimpsonIntegrationTest {

    // Corre todos los pasos de Simpson en orden y devuelve el valor de la integral
    private double integrate(int dof, double x, int n) {
        SimpsonIntegration s = new SimpsonIntegration(dof, x, 0.00001);
        s.computeW(n, x);
        s.computeXi(n);
        s.computeFirstBaseTerms(n, s.getTotXi(), dof);
        s.computeExponent(dof);
        s.computeCoefficient(dof);
        s.computeFxi(n);
        s.computeFinalTerms(n);
        s.computeFinalValue(n);
        return s.getFinalValue();
    }

    @Test
    void dof9ConDiezSegmentos() {
        assertEquals(0.3500589, integrate(9, 1.1, 10), 1e-5);
    }

    @Test
    void dof9ConVeinteSegmentos() {
        assertEquals(0.35005864, integrate(9, 1.1, 20), 1e-5);
    }

    @Test
    void dof10ConVeinteSegmentos() {
        assertEquals(0.36757, integrate(10, 1.1812, 20), 1e-5);
    }

    @Test
    void dof30ConVeinteSegmentos() {
        assertEquals(0.49500, integrate(30, 2.750, 20), 1e-5);
    }
        @Test
    void xIgualACeroDaCero() {
        assertEquals(0.0, integrate(9, 0.0, 10), 1e-12);
    }
}
