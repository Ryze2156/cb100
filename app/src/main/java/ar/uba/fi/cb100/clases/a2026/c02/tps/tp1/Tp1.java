package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

/**
 * Clase principal que contiene el punto de entrada del programa.
 * Recibe por parámetro el archivo de entrada y la fecha de corte; 
 * si no se proveen, utiliza los valores por defecto.
 */
public class Tp1 {

    /** Ruta relativa por defecto del archivo de préstamos. */
    private static final String ENTRADA_POR_DEFECTO = "datos/prestamos.csv";
    
    /** Fecha de corte por defecto para el cálculo de atrasos. */
    private static final LocalDate CORTE_POR_DEFECTO = LocalDate.parse("2026-05-04");

    /**
     * Método principal de ejecución.
     *
     * @param args Argumentos de consola: args[0] archivo (opcional), args[1] fecha corte (opcional).
     * @throws IOException Si ocurre un error leyendo o escribiendo los archivos.
     */
    public static void main(String[] args) throws IOException {
        String archivoEntrada = args.length > 0 ? args[0] : ENTRADA_POR_DEFECTO;
        LocalDate fechaCorte = args.length > 1 ? LocalDate.parse(args[1]) : CORTE_POR_DEFECTO;

        Path rutaEntrada = Path.of(archivoEntrada);
        ResultadoDeCarga carga = LectorDePrestamos.cargar(rutaEntrada);

        // 1. Resumen de la carga por consola
        System.out.println("Resumen de la carga");
        int validas = carga.registro().cantidad();
        String[] errores = carga.errores();
        int descartadas = errores.length;
        
        System.out.println("Lineas de datos: " + carga.lineasDeDatos());
        System.out.println("validas: " + validas);
        System.out.println("descartadas: " + descartadas);

        for (int i = 0; i < descartadas; i++) {
            System.out.println(errores[i]);
        }

        // 2. Generación de las filas de reporte consolidadas
        FilaDeSocio[] filas = Reporteador.porSocio(carga.registro(), fechaCorte);

        // 3. Exportación a CSV
        ExportadorDeReporte expCsv = new ExportadorCsv();
        expCsv.exportar(filas, Path.of("reporte.csv"));

        // 4. Exportación a TXT (pasando la fechaCorte que requiere su constructor)
        ExportadorDeReporte expTxt = new ExportadorTxt(fechaCorte);
        Path rutaTxt = Path.of("reporte.txt");
        expTxt.exportar(filas, rutaTxt);

        // 5. Anexar ranking de los 3 títulos más pedidos al final del TXT
        String[] ranking = Reporteador.ranking(carga.registro(), 3);
        StringBuilder sbRanking = new StringBuilder();
        sbRanking.append("\nTITULOS MAS PEDIDOS\n");
        
        for (int i = 0; i < ranking.length; i++) {
            // El ranking ya viene formateado desde la lógica de la clase RegistroSobreArreglo
            sbRanking.append(ranking[i]).append("\n");
        }
        
        Files.writeString(rutaTxt, sbRanking.toString(), StandardOpenOption.APPEND);
    }
}