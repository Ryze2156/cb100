package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

/**
 * Representa una fila consolidada del reporte por socio.
 *
 * @param padron        Número de padrón del socio.
 * @param socio         Nombre del socio.
 * @param prestamos     Cantidad total de préstamos asociados al socio.
 * @param diasDeAtraso  Total acumulado de días de atraso en sus préstamos.
 * @param multa         Monto total de la multa calculada.
 * @param estado        Estado actual del socio ("CON_DEUDA" o "AL_DIA").
 */

public record FilaDeSocio(int padron, String socio, int prestamos, int diasDeAtraso, int multa, String estado) {
}