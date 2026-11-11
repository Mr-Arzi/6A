import java.util.Locale;
import java.util.Scanner;

public class App {
    private static final int RUNS = 3;

    // Uso: App [dof1 p1 dof2 p2 dof3 p3]
    // Corre la Tabla 1 completa y después 3 pruebas con los valores recibidos como
    // argumentos o, si no hay argumentos, los pide por teclado.
    public static void main(String[] args) {
        Logic logic = new Logic();
        logic.logic1a();

        if (args.length != 0 && args.length != 2 * RUNS) {
            System.err.println("Uso: App [dof1 p1 dof2 p2 dof3 p3]");
            System.exit(2);
        }
        Scanner in = args.length == 0 ? new Scanner(System.in).useLocale(Locale.US) : null;

        for (int t = 1; t <= RUNS; t++) {
            System.out.println();
            try {
                int dof;
                double p;
                if (in != null) {
                    System.out.println("Prueba " + t + " de " + RUNS);
                    System.out.print("dof: ");
                    dof = Integer.parseInt(in.nextLine().trim());
                    System.out.print("p: ");
                    p = Double.parseDouble(in.nextLine().trim());
                } else {
                    dof = Integer.parseInt(args[2 * (t - 1)].trim());
                    p = Double.parseDouble(args[2 * (t - 1) + 1].trim());
                }
                logic.logicWithParams(t, dof, p);
            } catch (RuntimeException e) {
                System.err.println("Valores inválidos: " + e.getMessage());
                System.exit(2);
            }
        }
    }
}
