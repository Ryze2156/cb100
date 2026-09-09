package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Procesa el archivo de texto para instanciar los préstamos y registrarlos en la estructura.
 */
public class LectorDePrestamos {
    
    /**
     * Lee el archivo línea por línea, ignora comentarios y líneas en blanco, y registra los préstamos válidos.
     * Captura las líneas inválidas sin abortar la ejecución, redimensionando manualmente el arreglo de errores.
     * 
     * @param archivo Ruta del archivo de texto a procesar.
     * @return Objeto ResultadoDeCarga con el registro poblado, los errores detectados y la cantidad de líneas leídas.
     * @throws IOException Si ocurre un error de lectura o el archivo no existe.
     */
    
    public static ResultadoDeCarga cargar(Path archivo) throws IOException {
        String[] lineas = Files.readAllLines(archivo).toArray(new String[0]);
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        
        String[] errores = new String[8];
        int cantidadErrores = 0;
        int lineasDeDatos = 0;
        
        for (int i = 0; i < lineas.length; i++) {
            String linea = lineas[i];
            int numeroLineaReal = i + 1;
            
            if (linea.trim().isEmpty() || linea.startsWith("#")) {
                continue;
            }
            
            lineasDeDatos++;
            
            try {
                Prestamo p = procesarLinea(linea, numeroLineaReal);
                registro.registrar(p);
            } catch (LineaInvalidaException e) {
                if (cantidadErrores == errores.length) {
                    String[] nuevosErrores = new String[errores.length * 2];
                    for (int j = 0; j < errores.length; j++) {
                        nuevosErrores[j] = errores[j];
                    }
                    errores = nuevosErrores;
                }
                errores[cantidadErrores] = "linea " + e.numeroDeLinea() + ": " + e.getMessage();
                cantidadErrores++;
            }
        }
        
        String[] erroresFinales = new String[cantidadErrores];
        for (int i = 0; i < cantidadErrores; i++) {
            erroresFinales[i] = errores[i];
        }
        
        return new ResultadoDeCarga(registro, erroresFinales, lineasDeDatos);
    }

    /**
     * Parsea una única línea de texto, extrayendo sus 6 campos y delegando la creación al registro.
     * 
     * @param linea Cadena de texto correspondiente a la línea a parsear.
     * @param numeroLineaReal Posición real de la línea en el archivo original para el reporte de errores.
     * @return Una instancia válida de Prestamo.
     * @throws LineaInvalidaException Si la cantidad de campos es incorrecta o hay errores de formato en los datos.
     */
    private static Prestamo procesarLinea(String linea, int numeroLineaReal) {
        String[] campos = linea.split(";", -1); 
        
        if (campos.length != 6) {
            throw new LineaInvalidaException(numeroLineaReal, "se esperaban 6 campos y llegaron " + campos.length);
        }

        LocalDate retiro;
        try {
            retiro = LocalDate.parse(campos[0].trim());
        } catch (DateTimeParseException e) {
            throw new LineaInvalidaException(numeroLineaReal, "fecha invalida: " + campos[0].trim());
        }

        int padron;
        try {
            padron = Integer.parseInt(campos[1].trim());
        } catch (NumberFormatException e) {
            throw new LineaInvalidaException(numeroLineaReal, "padron no numerico: " + campos[1].trim());
        }

        String socio = campos[2].trim();
        String isbn = campos[3].trim();
        String titulo = campos[4].trim();

        LocalDate devolucion = null;
        if (!campos[5].trim().isEmpty()) {
            try {
                devolucion = LocalDate.parse(campos[5].trim());
            } catch (DateTimeParseException e) {
                throw new LineaInvalidaException(numeroLineaReal, "fecha invalida: " + campos[5].trim());
            }
        }

        try {
            return new Prestamo(retiro, padron, socio, isbn, titulo, devolucion);
        } catch (IllegalArgumentException e) {
            throw new LineaInvalidaException(numeroLineaReal, e.getMessage());
        }
    }
}