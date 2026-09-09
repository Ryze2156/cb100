package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

public interface RegistroDePrestamos {
 /**
     * Registra un nuevo préstamo.
     * 
     * @param p Instancia de Prestamo a agregar.
     */
    void registrar(Prestamo p);

    /**
     * Devuelve la cantidad de préstamos registrados.
     * 
     * @return Número de elementos guardados.
     */
    
    int cantidad();

    /**
     * Obtiene el préstamo en la posición especificada.
     * 
     * @param i Índice del elemento a recuperar.
     * @return El préstamo ubicado en el índice i.
     * @throws IndexOutOfBoundsException Si el índice i es inválido.
     */
    Prestamo obtener(int i);

    /**
     * Retorna los padrones de los socios sin repetidos y en su orden de aparición original.
     * 
     * @return Arreglo de enteros con los padrones únicos.
     */
    int[] padrones();

    /**
     * Retorna todos los préstamos pertenecientes a un padrón específico.
     * 
     * @param padron Número de padrón a consultar.
     * @return Arreglo con los préstamos del socio (arreglo vacío si no tiene ninguno).
     */
    Prestamo[] prestamosDe(int padron);

    /**
     * Genera el ranking de los n títulos más pedidos, aplicando desempate alfabético.
     * 
     * @param n Cantidad máxima de títulos a incluir en el ranking.
     * @return Arreglo de Strings con los n títulos más requeridos.
     */
    String[] titulosMasPedidos(int n);
}
