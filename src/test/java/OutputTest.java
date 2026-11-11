import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class OutputTest {

    @TempDir
    Path tempDir;

    @Test
    void writeDataEscribeElTextoEnElArchivo() throws IOException {
        Path salida = tempDir.resolve("salida.txt");
        String contenido = "hola\nmundo\n";

        output out = new output();
        out.writeData(salida.toString(), contenido);

        assertEquals(contenido, Files.readString(salida));
    }

    @Test
    void writeDataSobreUnDirectorioNoLanzaExcepcion() {
        output out = new output();
        assertDoesNotThrow(() -> out.writeData(tempDir.toString(), "texto"));
    }
}
