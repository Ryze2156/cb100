package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas automáticas para la exportación de reportes en formato de texto plano.
 */
class ExportadorTxtTest {

    @Test
    void exportarEscribeCabeceraFilasYTotalesEnElArchivoTxt(@TempDir Path tempDir) throws IOException {
        Path destinoTemporal = tempDir.resolve("reporte_temp.txt");
        LocalDate corte = LocalDate.of(2026, 5, 4);
        ExportadorDeReporte exportador = new ExportadorTxt(corte);

        // Mismos datos que ExportadorCsvTest, para poder comparar ambos formatos.
        FilaDeSocio[] filas = new FilaDeSocio[]{
            new FilaDeSocio(39876, "Bruno Ferrari", 4, 24, 3600, "CON_DEUDA"),
            new FilaDeSocio(42001, "Carla Nunez", 4, 0, 0, "AL_DIA")
        };

        exportador.exportar(filas, destinoTemporal);

        String[] lineas = Files.readAllLines(destinoTemporal).toArray(new String[0]);

        assertEquals(9, lineas.length, "Cabecera (2) + linea en blanco + titulos + separador + 2 filas + separador + totales.");
        assertEquals("BIBLIOTECA FIUBA - REPORTE DE MULTAS", lineas[0]);
        assertEquals("Fecha de corte: 2026-05-04", lineas[1]);
        assertEquals("", lineas[2]);
        assertEquals("Padron   Socio              Prestamos  DiasAtraso     Multa  Estado", lineas[3]);
        assertEquals("-".repeat(68), lineas[4]);
        assertEquals("39876    Bruno Ferrari              4          24      3600  CON_DEUDA", lineas[5]);
        assertEquals("42001    Carla Nunez                4           0         0  AL_DIA", lineas[6]);
        assertEquals("-".repeat(68), lineas[7]);
        assertEquals("TOTALES                             8          24      3600", lineas[8]);
    }

    @Test
    void extensionDevuelveTxt() {
        assertEquals("txt", new ExportadorTxt(LocalDate.of(2026, 5, 4)).extension());
    }
}
