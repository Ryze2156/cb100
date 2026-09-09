package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

/**
 * Contenedor inmutable que agrupa los resultados del proceso de lectura del archivo de préstamos.
 * 
 * @param registro Estructura con todos los préstamos válidos cargados.
 * @param errores Arreglo con los mensajes de error de las líneas descartadas.
 * @param lineasDeDatos Cantidad total de líneas procesadas excluyendo blancos y comentarios.
 */

public record ResultadoDeCarga(RegistroDePrestamos registro, String[] errores, int lineasDeDatos) {
}
