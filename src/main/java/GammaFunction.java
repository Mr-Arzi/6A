public class GammaFunction {

    private double gammaValue;

    // Gamma para enteros: Γ(n) = (n-1)!
    public double computeIntGamma(int intValue) {
        if (intValue < 1) {
            throw new IllegalArgumentException("El valor debe ser >= 1");
        }
        double result = 1;
        for (int i = 2; i < intValue; i++) {
            result = result * i;
        }
        gammaValue = result;
        return gammaValue;
    }

    // Gamma para enteros o múltiplos de 0.5: Γ(x) = (x-1) * Γ(x-1)
    public double computeDblGamma(double doubleValue) {
        if (doubleValue < 0.5) {
            throw new IllegalArgumentException("El valor debe ser >= 0.5");
        }
        double result = 1.0;
        double x = doubleValue;
        while (x > 1.0) {
            x = x - 1.0;
            result = result * x;
        }
        if (x == 0.5) {
            result = result * Math.sqrt(Math.PI);   // Γ(1/2) = √π
        }
        gammaValue = result;
        return gammaValue;
    }
}