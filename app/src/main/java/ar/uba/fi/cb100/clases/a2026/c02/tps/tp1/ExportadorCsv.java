package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementación de {@link ExportadorDeReporte} para formato de valores separados por punto y coma (CSV).
 */
public class ExportadorCsv implements ExportadorDeReporte {

    /**
     * Exporta la información consolidada de socios a un archivo CSV.
     *
     * @param filas   Filas de socios procesadas.
     * @param destino Ruta donde se creará el archivo CSV.
     * @throws IOException Si falla la escritura en el disco.
     */
    @Override
    public void exportar(FilaDeSocio[] filas, Path destino) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("padron;socio;prestamos;dias_atraso;multa;estado\n");

        for (int i = 0; i < filas.length; i++) {
            FilaDeSocio f = filas[i];
            sb.append(f.padron()).append(";")
              .append(f.socio()).append(";")
              .append(f.prestamos()).append(";")
              .append(f.diasDeAtraso()).append(";")
              .append(f.multa()).append(";")
              .append(f.estado()).append("\n");
        }
        Files.writeString(destino, sb.toString());
    }

    /**
     * Devuelve la extensión asociada al formato CSV.
     *
     * @return Cadena "csv".
     */
    @Override
    public String extension() {
        return "csv";
    }
}