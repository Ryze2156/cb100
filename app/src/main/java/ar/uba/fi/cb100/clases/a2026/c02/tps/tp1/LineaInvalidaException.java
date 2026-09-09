package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;


/**
 * Excepción personalizada para representar una línea con errores de formato o validación en el archivo.
 */

public class LineaInvalidaException extends RuntimeException {
    private final int numeroDeLinea;

    /**
     * Construye una nueva excepción de línea inválida.
     * 
     * @param numeroDeLinea Número de línea real en el archivo de texto.
     * @param motivo Descripción específica del error encontrado.
     */

    public LineaInvalidaException(int numeroDeLinea, String motivo) {
        super(motivo);
        this.numeroDeLinea = numeroDeLinea;
    }

    /**
     * Retorna el número de línea donde se originó el error.
     * 
     * @return El número de la línea inválida.
     */
    
    public int numeroDeLinea() {
        return this.numeroDeLinea;
    }
}
