public class SimpsonIntegration {

    private int intNumSeg;
    private double dblW;
    private double dblE;
    private int intDOF;
    private double dblX;
    private double[] dblTotXi;
    private double[] dblFirstBaseTerms;
    private double dblExponent;
    private double dblCoeff;
    private double[] dblFxi;
    private double[] dblFinalTerms;
    private double dblFinalValue;

    public SimpsonIntegration(int intDOF, double dblX, double dblE) {
        this.intDOF = intDOF;
        this.dblX = dblX;
        this.dblE = dblE;
    }

    // W = x / num_seg (ancho de cada segmento)
    public void computeW(int intNumSeg, double dblX) {
        this.intNumSeg = intNumSeg;
        this.dblX = dblX;
        dblW = dblX / intNumSeg;
    }

    // xi = i * W, para i = 0 hasta num_seg
    public void computeXi(int intNumSeg) {
        dblTotXi = new double[intNumSeg + 1];
        for (int i = 0; i <= intNumSeg; i++) {
            dblTotXi[i] = i * dblW;
        }
    }

    // Columna "1 + xi^2 / dof"
    public void computeFirstBaseTerms(int intNumSeg, double[] dblTotXi, int intDOF) {
        dblFirstBaseTerms = new double[intNumSeg + 1];
        for (int i = 0; i <= intNumSeg; i++) {
            dblFirstBaseTerms[i] = 1 + (dblTotXi[i] * dblTotXi[i]) / intDOF;
        }
    }

    // Exponente: -(dof + 1) / 2
    public void computeExponent(int intDOF) {
        dblExponent = -(intDOF + 1) / 2.0;
    }

    // Coeficiente: Γ((dof+1)/2) / ( (dof*π)^(1/2) * Γ(dof/2) )
    public void computeCoefficient(int intDOF) {
        GammaFunction gamma = new GammaFunction();
        double numerator = gamma.computeDblGamma((intDOF + 1) / 2.0);
        double denominator = Math.sqrt(intDOF * Math.PI) * gamma.computeDblGamma(intDOF / 2.0);
        dblCoeff = numerator / denominator;
    }

    // F(xi) = coeficiente * (1 + xi^2/dof)^exponente
    public void computeFxi(int intNumSeg) {
        dblFxi = new double[intNumSeg + 1];
        for (int i = 0; i <= intNumSeg; i++) {
            dblFxi[i] = dblCoeff * Math.pow(dblFirstBaseTerms[i], dblExponent);
        }
    }

    // Término = (W/3) * multiplicador * F(xi)
    // Multiplicador: 1 en los extremos, 4 en i impar, 2 en i par
    public void computeFinalTerms(int intNumSeg) {
        dblFinalTerms = new double[intNumSeg + 1];
        for (int i = 0; i <= intNumSeg; i++) {
            int multiplier;
            if (i == 0 || i == intNumSeg) {
                multiplier = 1;
            } else if (i % 2 == 1) {
                multiplier = 4;
            } else {
                multiplier = 2;
            }
            dblFinalTerms[i] = (dblW / 3) * multiplier * dblFxi[i];
        }
    }

    // Suma de todos los términos = valor de la integral
    public void computeFinalValue(int intNumSeg) {
        dblFinalValue = 0;
        for (int i = 0; i <= intNumSeg; i++) {
            dblFinalValue += dblFinalTerms[i];
        }
    }

    public double getFinalValue() {
        return dblFinalValue;
    }

        public double[] getTotXi() {
        return dblTotXi;
    }

}