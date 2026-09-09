package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Contrato para la exportación de reportes a archivos de disco.
 */
public interface ExportadorDeReporte {

    /**
     * Escribe la información consolidada de los socios en el archivo especificado.
     *
     * @param filas   Arreglo con la información consolidada por socio.
     * @param destino Ruta del archivo de salida.
     * @throws IOException Si ocurre un error de lectura o escritura en disco.
     */
    void exportar(FilaDeSocio[] filas, Path destino) throws IOException;

    /**
     * Devuelve la extensión del tipo de archivo soportado.
     *
     * @return Extensión del formato (por ejemplo, "txt" o "csv").
     */
    String extension();
}