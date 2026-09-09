package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Implementación de {@link ExportadorDeReporte} para archivos de texto plano con columnas alineadas.
 */
public class ExportadorTxt implements ExportadorDeReporte {

    private static final int ANCHO_SEPARADOR = 68;

    private final LocalDate fechaCorte;

    /**
     * Construye el exportador de texto plano.
     *
     * @param fechaCorte Fecha de corte usada en el cálculo del reporte, impresa como encabezado.
     */
    public ExportadorTxt(LocalDate fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    /**
     * Exporta el reporte estructurado con cabecera, alineación fija y fila de totales a un archivo TXT.
     *
     * @param filas   Filas de socios procesadas.
     * @param destino Ruta donde se creará el archivo TXT.
     * @throws IOException Si ocurre un fallo en la escritura en disco.
     */
    @Override
    public void exportar(FilaDeSocio[] filas, Path destino) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("BIBLIOTECA FIUBA - REPORTE DE MULTAS\n");
        sb.append("Fecha de corte: ").append(fechaCorte).append("\n\n");
        sb.append(encabezado());
        sb.append(separador());

        int[] totales = agregarFilas(sb, filas);

        sb.append(separador());
        sb.append(filaTotales(totales[0], totales[1], totales[2]));

        Files.writeString(destino, sb.toString());
    }

    /**
     * Arma la línea de encabezado con los nombres de columna.
     *
     * @return Línea de encabezado formateada.
     */
    private String encabezado() {
        return String.format("%-9s%-19s%9s  %10s  %8s  %s%n",
                "Padron", "Socio", "Prestamos", "DiasAtraso", "Multa", "Estado");
    }

    /**
     * Arma la línea separadora de guiones.
     *
     * @return Línea de guiones.
     */
    private String separador() {
        return "-".repeat(ANCHO_SEPARADOR) + "\n";
    }

    /**
     * Agrega al StringBuilder una línea formateada por cada fila y acumula los totales.
     *
     * @param sb    StringBuilder donde se van agregando las líneas.
     * @param filas Filas de socios a imprimir.
     * @return Arreglo con [totalPrestamos, totalAtraso, totalMulta].
     */
    private int[] agregarFilas(StringBuilder sb, FilaDeSocio[] filas) {
        int totPrestamos = 0;
        int totAtraso = 0;
        int totMulta = 0;

        for (int i = 0; i < filas.length; i++) {
            FilaDeSocio f = filas[i];
            sb.append(String.format("%-9s%-19s%9d  %10d  %8d  %s%n",
                    f.padron(), f.socio(), f.prestamos(), f.diasDeAtraso(), f.multa(), f.estado()));

            totPrestamos += f.prestamos();
            totAtraso += f.diasDeAtraso();
            totMulta += f.multa();
        }

        return new int[]{totPrestamos, totAtraso, totMulta};
    }

    /**
     * Arma la línea de totales.
     *
     * @param totPrestamos Total de préstamos.
     * @param totAtraso    Total de días de atraso.
     * @param totMulta     Total de multa.
     * @return Línea de totales formateada.
     */
    private String filaTotales(int totPrestamos, int totAtraso, int totMulta) {
        return String.format("%-9s%-19s%9d  %10d  %8d%n", "TOTALES", "", totPrestamos, totAtraso, totMulta);
    }

    /**
     * Devuelve la extensión asociada al formato de texto plano.
     *
     * @return Cadena "txt".
     */
    @Override
    public String extension() {
        return "txt";
    }
}