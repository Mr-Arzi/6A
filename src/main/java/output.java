import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class output {

    // Escribe outText en el archivo outFile (si el archivo ya existe, lo sobrescribe)
    public void writeData(String outFile, String outText) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outFile))) {
            writer.print(outText);
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}