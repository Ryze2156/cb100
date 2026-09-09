package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas automáticas para la exportación de reportes en formato CSV.
 */
class ExportadorCsvTest {

    @Test
    void exportarEscribeCorrectamenteElArchivoCsv(@TempDir Path tempDir) throws IOException {
        // Generamos la ruta para el archivo temporal
        Path destinoTemporal = tempDir.resolve("reporte_temp.csv");
        ExportadorDeReporte exportador = new ExportadorCsv();

        // Preparamos datos de prueba simulando dos socios
        FilaDeSocio[] filas = new FilaDeSocio[]{
            new FilaDeSocio(39876, "Bruno Ferrari", 4, 24, 3600, "CON_DEUDA"),
            new FilaDeSocio(42001, "Carla Nunez", 4, 0, 0, "AL_DIA")
        };

        // Ejecutamos la exportación
        exportador.exportar(filas, destinoTemporal);

        // Volvemos a leer el archivo y lo convertimos a arreglo 
        // para no usar java.util.List ni violar las restricciones del TP.
        String[] lineasLeidas = Files.readAllLines(destinoTemporal).toArray(new String[0]);

        // Verificamos el contenido esperado
        assertEquals(3, lineasLeidas.length, "El archivo debería tener 3 líneas: 1 cabecera y 2 de datos.");
        assertEquals("padron;socio;prestamos;dias_atraso;multa;estado", lineasLeidas[0]);
        assertEquals("39876;Bruno Ferrari;4;24;3600;CON_DEUDA", lineasLeidas[1]);
        assertEquals("42001;Carla Nunez;4;0;0;AL_DIA", lineasLeidas[2]);
    }
}