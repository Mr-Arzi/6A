import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class LogicAppTest {

    @Test
    void mainGeneraElArchivoDeResultados() throws IOException {
        App.main(new String[0]);

        Path resultados = Path.of("resultados.txt");
        assertTrue(Files.exists(resultados), "resultados.txt deberia existir");

        String contenido = Files.readString(resultados);
        assertTrue(contenido.contains("0.55338"), "falta 0.55338");
        assertTrue(contenido.contains("1.75305"), "falta 1.75305");
        assertTrue(contenido.contains("4.60409"), "falta 4.60409");
    }
}
