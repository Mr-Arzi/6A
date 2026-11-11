import java.util.Locale;

public class Logic {

    // Tolerancia de Simpson (diferencia entre dos corridas con num_seg y 2*num_seg)
    private static final double SIMPSON_E = 0.000000001;
    // Tolerancia de la búsqueda de x (|integral - p|)
    private static final double SEARCH_E = 0.00000001;
    // Límite de seguridad para evitar un ciclo infinito
    private static final int MAX_ITERATIONS = 1000;

    private int intNumSeg;
    private double dblE;
    private int intDOF;
    private double dblX;

    // Ejecuta las 3 pruebas de la Tabla 1 y guarda los resultados en un archivo
    public void logic1a() {
        double[] ps = {0.20, 0.45, 0.495};
        int[] dofs = {6, 15, 4};
        double[] expected = {0.55338, 1.75305, 4.60409};

        output out = new output();
        StringBuilder text = new StringBuilder();
        text.append("Test | p | dof | Expected x | Actual x | num_seg final\n");

        for (int t = 0; t < ps.length; t++) {
            double x = findX(ps[t], dofs[t]);

            String line = String.format(Locale.US,
                "%d | %.3f | %d | %.5f | %.5f | %d%n",
                t + 1, ps[t], dofs[t], expected[t], x, intNumSeg);
            text.append(line);
        }

        System.out.print(text);
        out.writeData("resultados.txt", text.toString());
    }

    // Busca x tal que la integral de la t-distribution de 0 a x sea p
    public double findX(double p, int dof) {
        if (p <= 0 || p > 0.5) {
            throw new IllegalArgumentException("p debe estar en (0, 0.5]");
        }
        if (dof < 1) {
            throw new IllegalArgumentException("dof debe ser >= 1");
        }

        intDOF = dof;
        double x = 1.0;     // valor de prueba inicial
        double d = 0.5;     // paso inicial

        double error = integrateTo(x) - p;
        int previousSign = Double.compare(error, 0.0);
        int iterations = 0;

        while (Math.abs(error) > SEARCH_E) {
            if (++iterations > MAX_ITERATIONS) {
                throw new IllegalStateException("La búsqueda no converge");
            }

            // Resultado muy bajo: x más grande. Muy alto: x más chica.
            if (error < 0) {
                x = x + d;
            } else {
                x = x - d;
            }

            error = integrateTo(x) - p;

            // Si el signo del error cambió, se divide d entre 2
            int sign = Double.compare(error, 0.0);
            if (sign != previousSign) {
                d = d / 2;
            }
            previousSign = sign;
        }

        dblX = x;
        return x;
    }

    // Integra la t-distribution de 0 a x, reiniciando num_seg y E
    private double integrateTo(double x) {
        dblX = x;
        dblE = SIMPSON_E;
        intNumSeg = 10;
        return integrate();
    }

    // Duplica num_seg hasta que la diferencia entre dos resultados sea menor que E
    private double integrate() {
        SimpsonIntegration s = new SimpsonIntegration(intDOF, dblX, dblE);

        int n = intNumSeg;
        double previous = simpsonValue(s, n);
        n = n * 2;
        double current = simpsonValue(s, n);

        while (Math.abs(current - previous) >= dblE) {
            previous = current;
            n = n * 2;
            current = simpsonValue(s, n);
        }

        intNumSeg = n;
        return current;
    }

    // Corre todos los pasos de Simpson con n segmentos y devuelve el valor final
    private double simpsonValue(SimpsonIntegration s, int n) {
        s.computeW(n, dblX);
        s.computeXi(n);
        s.computeFirstBaseTerms(n, s.getTotXi(), intDOF);
        s.computeExponent(intDOF);
        s.computeCoefficient(intDOF);
        s.computeFxi(n);
        s.computeFinalTerms(n);
        s.computeFinalValue(n);
        return s.getFinalValue();
    }
}