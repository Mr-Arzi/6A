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

    // Corre una sola prueba con dof y p dados; el resultado esperado se calcula solo
    public double logicWithParams(int test, int dof, double p) {
        double expected = expectedX(p, dof);
        double x = findX(p, dof);
        String text = String.format(Locale.US,
            "Prueba %d%ndof = %d%np = %.5f%nResultado esperado = %.5f%nResultado obtenido = %.5f%nnum_seg final = %d%n",
            test, dof, p, expected, x, intNumSeg);
        System.out.print(text);
        new output().writeData("resultados_parametros_" + test + ".txt", text);
        return x;
    }

    // Valor esperado: si es un caso de la Tabla 1 se usa ese valor; si no, se calcula
    // de forma independiente (función beta incompleta + bisección).
    static double expectedX(double p, int dof) {
        if (dof == 6 && p == 0.20) return 0.55338;
        if (dof == 15 && p == 0.45) return 1.75305;
        if (dof == 4 && p == 0.495) return 4.60409;

        double lo = 0, hi = 1;
        while (halfCdf(hi, dof) < p) hi *= 2;
        for (int i = 0; i < 200; i++) {
            double mid = (lo + hi) / 2;
            if (halfCdf(mid, dof) < p) lo = mid; else hi = mid;
        }
        return (lo + hi) / 2;
    }

    // Integral de la t-distribution de 0 a x
    private static double halfCdf(double x, int dof) {
        return 0.5 - 0.5 * betaInc(dof / 2.0, 0.5, dof / (dof + x * x));
    }

    private static double betaInc(double a, double b, double x) {
        if (x <= 0) return 0;
        if (x >= 1) return 1;
        double bt = Math.exp(lgamma(a + b) - lgamma(a) - lgamma(b)
            + a * Math.log(x) + b * Math.log(1 - x));
        if (x < (a + 1) / (a + b + 2)) {
            return bt * betaCf(a, b, x) / a;
        }
        return 1 - bt * betaCf(b, a, 1 - x) / b;
    }

    private static double betaCf(double a, double b, double x) {
        double tiny = 1e-300;
        double c = 1, d = 1 - (a + b) * x / (a + 1);
        if (Math.abs(d) < tiny) d = tiny;
        d = 1 / d;
        double h = d;
        for (int m = 1; m <= 500; m++) {
            int m2 = 2 * m;
            double aa = m * (b - m) * x / ((a + m2 - 1) * (a + m2));
            d = 1 + aa * d; if (Math.abs(d) < tiny) d = tiny;
            c = 1 + aa / c; if (Math.abs(c) < tiny) c = tiny;
            d = 1 / d; h *= d * c;
            aa = -(a + m) * (a + b + m) * x / ((a + m2) * (a + m2 + 1));
            d = 1 + aa * d; if (Math.abs(d) < tiny) d = tiny;
            c = 1 + aa / c; if (Math.abs(c) < tiny) c = tiny;
            d = 1 / d;
            double del = d * c;
            h *= del;
            if (Math.abs(del - 1) < 1e-15) break;
        }
        return h;
    }

    // log(gamma(x)) con la aproximación de Lanczos
    private static double lgamma(double x) {
        double[] g = {76.18009172947146, -86.50532032941677, 24.01409824083091,
            -1.231739572450155, 0.1208650973866179e-2, -0.5395239384953e-5};
        double y = x, tmp = x + 5.5;
        tmp -= (x + 0.5) * Math.log(tmp);
        double ser = 1.000000000190015;
        for (double c : g) ser += c / ++y;
        return -tmp + Math.log(2.5066282746310005 * ser / x);
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