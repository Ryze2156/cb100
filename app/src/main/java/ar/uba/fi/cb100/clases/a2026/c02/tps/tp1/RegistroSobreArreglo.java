package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.util.Arrays;

/**
 * Implementación del TDA RegistroDePrestamos sobre un arreglo dinámico con redimensionamiento manual.
 */
public class RegistroSobreArreglo implements RegistroDePrestamos {
    private static final int CAPACIDAD_INICIAL = 8;
    private static final int FACTOR_CRECIMIENTO = 2;

    private Prestamo[] prestamos;
    private int cantidad;

    /**
     * Inicializa la estructura con una capacidad de 8 elementos.
     */
    public RegistroSobreArreglo() {
        this.prestamos = new Prestamo[CAPACIDAD_INICIAL];
        this.cantidad = 0;
    }

    /**
     * Registra un nuevo préstamo en la estructura.
     * Si el arreglo se encuentra lleno, duplica su capacidad.
     * 
     * @param p Instancia de Prestamo a registrar.
     * @throws IllegalArgumentException Si el préstamo es nulo.
     */
    @Override
    public void registrar(Prestamo p) {
        if (p == null) {
            throw new IllegalArgumentException("El préstamo no puede ser nulo.");
        }
        if (this.cantidad == this.prestamos.length) {
            redimensionar();
        }
        this.prestamos[this.cantidad] = p;
        this.cantidad++;
    }

    /**
     * Devuelve la cantidad de préstamos efectivamente registrados.
     * 
     * @return Número de elementos guardados.
     */
    @Override
    public int cantidad() {
        return this.cantidad;
    }

    /**
     * Obtiene el préstamo ubicado en un determinado índice.
     * 
     * @param i Índice del elemento a recuperar.
     * @return Prestamo en el índice especificado.
     * @throws IndexOutOfBoundsException Si el índice es menor a cero o mayor/igual a la cantidad.
     */
    @Override
    public Prestamo obtener(int i) {
        if (i < 0 || i >= this.cantidad) {
            throw new IndexOutOfBoundsException("Índice inválido: " + i);
        }
        return this.prestamos[i];
    }

    /**
     * Retorna los padrones sin duplicados en su orden original de aparición.
     * 
     * @return Arreglo de enteros con los padrones únicos.
     */
    @Override
    public int[] padrones() {
        int[] temporales = new int[this.cantidad];
        int unicos = 0;

        for (int i = 0; i < this.cantidad; i++) {
            int padronActual = this.prestamos[i].padron();
            boolean yaExiste = false;

            for (int j = 0; j < unicos && !yaExiste; j++) {
                if (temporales[j] == padronActual) {
                    yaExiste = true;
                }
            }

            if (!yaExiste) {
                temporales[unicos] = padronActual;
                unicos++;
            }
        }

        return Arrays.copyOf(temporales, unicos);
    }

    /**
     * Retorna todos los préstamos asociados a un padrón de socio específico.
     * 
     * @param padron Número de padrón a consultar.
     * @return Arreglo con los préstamos encontrados o arreglo vacío si no posee ninguno.
     */
    @Override
    public Prestamo[] prestamosDe(int padron) {
        int coincidencias = 0;
        for (int i = 0; i < this.cantidad; i++) {
            if (this.prestamos[i].padron() == padron) {
                coincidencias++;
            }
        }

        Prestamo[] resultado = new Prestamo[coincidencias];
        int idx = 0;
        for (int i = 0; i < this.cantidad; i++) {
            if (this.prestamos[i].padron() == padron) {
                resultado[idx] = this.prestamos[i];
                idx++;
            }
        }

        return resultado;
    }

    /**
     * Genera el ranking de los n títulos más pedidos desempatando alfabéticamente en caso de empate.
     * 
     * @param n Cantidad máxima de títulos a retornar.
     * @return Arreglo de Strings con los n títulos más solicitados.
     */
    @Override
    public String[] titulosMasPedidos(int n) {
        if (n <= 0 || this.cantidad == 0) {
            return new String[0];
        }

        String[] titulosUnicos = new String[this.cantidad];
        int[] conteos = new int[this.cantidad];
        int totalUnicos = 0;

        for (int i = 0; i < this.cantidad; i++) {
            String tituloActual = this.prestamos[i].titulo();
            int pos = -1;

            for (int j = 0; j < totalUnicos && pos == -1; j++) {
                if (titulosUnicos[j].equals(tituloActual)) {
                    pos = j;
                }
            }

            if (pos != -1) {
                conteos[pos]++;
            } else {
                titulosUnicos[totalUnicos] = tituloActual;
                conteos[totalUnicos] = 1;
                totalUnicos++;
            }
        }

        // Ordenamiento por Selección
        for (int i = 0; i < totalUnicos - 1; i++) {
            int mejorIdx = i;
            for (int j = i + 1; j < totalUnicos; j++) {
                if (conteos[j] > conteos[mejorIdx]) {
                    mejorIdx = j;
                } else if (conteos[j] == conteos[mejorIdx]) {
                    if (titulosUnicos[j].compareTo(titulosUnicos[mejorIdx]) < 0) {
                        mejorIdx = j;
                    }
                }
            }

            if (mejorIdx != i) {
                int tempConteo = conteos[i];
                conteos[i] = conteos[mejorIdx];
                conteos[mejorIdx] = tempConteo;

                String tempTitulo = titulosUnicos[i];
                titulosUnicos[i] = titulosUnicos[mejorIdx];
                titulosUnicos[mejorIdx] = tempTitulo;
            }
        }

        int limite = Math.min(n, totalUnicos);
        return Arrays.copyOf(titulosUnicos, limite);
    }

    /**
     * Duplica la capacidad interna del arreglo dinámico.
     */
    private void redimensionar() {
        int nuevaCapacidad = this.prestamos.length * FACTOR_CRECIMIENTO;
        this.prestamos = Arrays.copyOf(this.prestamos, nuevaCapacidad);
    }
}