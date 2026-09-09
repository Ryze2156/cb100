package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas automáticas para la lectura y validación de archivos de préstamos.
 */
class LectorDePrestamosTest {

    /**
     * Valida que el lector procese correctamente 18 líneas válidas y capture 4 errores
     * específicos sin abortar la ejecución.
     * 
     * @param tempDir Directorio temporal inyectado por JUnit 5 para crear el archivo de prueba.
     * @throws IOException Si ocurre un error al escribir el archivo temporal.
     */
    
    @Test
    void cargarProcesaArchivoCorrectamenteYDetectaErrores(@TempDir Path tempDir) throws IOException {
        Path archivoPrueba = tempDir.resolve("prestamos_prueba.csv");
        
        StringBuilder contenido = new StringBuilder();
        contenido.append("# Biblioteca FIUBA registro de prestamos\n");
        contenido.append("# formato: fechaRetiro; padron; socio; isbn; titulo; fechaDevolucion\n");
        contenido.append("\n"); // Línea en blanco que debe ser ignorada
        
        // Se generan 18 líneas válidas
        for (int i = 0; i < 18; i++) {
            contenido.append("2026-03-02;").append(1000 + i)
                     .append(";Socio ").append(i)
                     .append(";9789871234567;Libro ").append(i)
                     .append(";2026-03-16\n");
        }
        
        // 4 líneas con los errores específicos descritos en el enunciado
        // 1. Faltan campos (4 de 6)
        contenido.append("2026-04-30;41234; Ana Gomez; 9789871234567\n");
        
        // 2. Fecha inválida (mes 13)
        contenido.append("2026-13-02;39876; Bruno Ferrari; 9780262033848; Algorithms; \n");
        
        // 3. Padrón no numérico
        contenido.append("2026-04-18;CUARENTA; Carla Nunez; 9789871234567; Estructuras; \n");
        
        // 4. Devolución anterior al retiro
        contenido.append("2026-04-15;43310; Elena Sosa; 111; Titulo; 2026-04-01\n");
        
        Files.writeString(archivoPrueba, contenido.toString());
        
        ResultadoDeCarga resultado = LectorDePrestamos.cargar(archivoPrueba);
        
        // Validaciones requeridas por el TP
        assertEquals(18, resultado.registro().cantidad(), "Debería haber registrado 18 préstamos válidos.");
        assertEquals(22, resultado.lineasDeDatos(), "Debería haber procesado 22 líneas de datos (18 válidas + 4 con errores).");
        assertEquals(4, resultado.errores().length, "Debería haber detectado exactamente 4 errores.");
        
        // Validar que los motivos de error sean los correctos buscando palabras clave
        assertTrue(resultado.errores()[0].contains("se esperaban 6 campos"));
        assertTrue(resultado.errores()[1].contains("fecha invalida"));
        assertTrue(resultado.errores()[2].contains("padron no numerico"));
        assertTrue(resultado.errores()[3].contains("anterior al retiro"));
    }
}