package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.time.LocalDate;

/**
 * Clase encargada de procesar el registro de préstamos y generar la información
 * consolidada para los reportes de salida.
 */
public class Reporteador {

    private static final int ANCHO_TITULO = 31;

    /**
     * Genera la lista consolidada de socios con sus totales de préstamos, atrasos, multas y estado.
     * La lista se ordena por multa de forma descendente y, a igual multa,
     * alfabéticamente por el nombre del socio.
     *
     * @param r     Registro de préstamos a procesar.
     * @param corte Fecha límite para el cálculo de pendientes y atrasos.
     * @return Arreglo de filas ordenado según las reglas de negocio.
     */
    public static FilaDeSocio[] porSocio(RegistroDePrestamos r, LocalDate corte) {
        FilaDeSocio[] filas = armarFilas(r, corte);
        ordenarPorMultaYSocio(filas);
        return filas;
    }

    /**
     * Arma, sin ordenar todavía, una fila consolidada por cada padrón del registro.
     *
     * @param r     Registro de préstamos a procesar.
     * @param corte Fecha límite para el cálculo de pendientes y atrasos.
     * @return Arreglo de filas sin ordenar.
     */
    private static FilaDeSocio[] armarFilas(RegistroDePrestamos r, LocalDate corte) {
        int[] padrones = r.padrones();
        FilaDeSocio[] filas = new FilaDeSocio[padrones.length];

        for (int i = 0; i < padrones.length; i++) {
            int padron = padrones[i];
            Prestamo[] prestamos = r.prestamosDe(padron);

            String nombreSocio = prestamos[0].socio();
            int totalAtraso = 0;
            int totalMulta = 0;

            for (int j = 0; j < prestamos.length; j++) {
                totalAtraso += prestamos[j].diasDeAtraso(corte);
                totalMulta += prestamos[j].multa(corte);
            }

            String estado = totalMulta > 0 ? "CON_DEUDA" : "AL_DIA";
            filas[i] = new FilaDeSocio(padron, nombreSocio, prestamos.length, totalAtraso, totalMulta, estado);
        }

        return filas;
    }

    /**
     * Ordena las filas por multa descendente y, a igual multa, alfabéticamente por socio.
     * Utiliza el algoritmo de burbuja.
     *
     * @param filas Arreglo de filas a ordenar in place.
     */
    private static void ordenarPorMultaYSocio(FilaDeSocio[] filas) {
        for (int i = 0; i < filas.length - 1; i++) {
            for (int j = 0; j < filas.length - 1 - i; j++) {
                if (debeIntercambiar(filas[j], filas[j + 1])) {
                    FilaDeSocio temporal = filas[j];
                    filas[j] = filas[j + 1];
                    filas[j + 1] = temporal;
                }
            }
        }
    }

    /**
     * Determina si dos filas consecutivas deben intercambiarse según el criterio de orden.
     *
     * @param actual    Fila en la posición actual.
     * @param siguiente Fila en la posición siguiente.
     * @return true si deben intercambiarse.
     */
    private static boolean debeIntercambiar(FilaDeSocio actual, FilaDeSocio siguiente) {
        if (actual.multa() < siguiente.multa()) {
            return true;
        }
        if (actual.multa() == siguiente.multa()) {
            return actual.socio().compareTo(siguiente.socio()) > 0;
        }
        return false;
    }

    /**
     * Obtiene los 'n' títulos con mayor cantidad de préstamos, formateados con su
     * posición en el ranking y la cantidad de préstamos asociados.
     *
     * @param r Registro de préstamos a consultar.
     * @param n Cantidad de títulos a incluir en el ranking.
     * @return Arreglo con los títulos formateados para el reporte.
     */
    public static String[] ranking(RegistroDePrestamos r, int n) {
        String[] titulos = r.titulosMasPedidos(n);
        String[] resultado = new String[titulos.length];

        for (int i = 0; i < titulos.length; i++) {
            int conteo = contarPrestamosDelTitulo(r, titulos[i]);
            resultado[i] = String.format("%3d. %-" + ANCHO_TITULO + "s%3d", i + 1, titulos[i], conteo);
        }

        return resultado;
    }

    /**
     * Cuenta cuántos préstamos del registro corresponden a un título dado.
     *
     * @param r      Registro de préstamos a recorrer.
     * @param titulo Título a contar.
     * @return Cantidad de préstamos con ese título.
     */
    private static int contarPrestamosDelTitulo(RegistroDePrestamos r, String titulo) {
        int conteo = 0;
        for (int i = 0; i < r.cantidad(); i++) {
            if (r.obtener(i).titulo().equals(titulo)) {
                conteo++;
            }
        }
        return conteo;
    }
}   