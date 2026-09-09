package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import java.time.LocalDate;

public record Prestamo(LocalDate retiro, int padron, String socio,
                       String isbn, String titulo, LocalDate devolucion) {

    private static final int PLAZO_DIAS = 14;
    private static final int COSTO_MULTA_DIARIA = 150;
    private static final int TOPE_MULTA = 3000;

    /**
     * Constructor compacto para la validación de invariantes de la clase.
     * Garantiza que no pueda existir una instancia mal formada en memoria
     * 
     * @throws IllegalArgumentException Si algún campo es nulo, vacío, invalido o inconsistente
    */

    public Prestamo {
        if (retiro == null) {
            throw new IllegalArgumentException("La fecha de retiro no puede ser nula.");
        }
        if (padron <= 0) {
            throw new IllegalArgumentException("El padrón debe ser un entero positivo.");
        }
        if (socio == null || socio.isBlank()) {
            throw new IllegalArgumentException("El nombre del socio no puede ser nulo ni estar vacío.");
        }
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("El ISBN no puede ser nulo ni estar vacío.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título no puede ser nulo ni estar vacío.");
        }
        if (devolucion != null && devolucion.isBefore(retiro)) {
            throw new IllegalArgumentException("La devolución no puede ser anterior al retiro.");
        }
    }
    
    /**
     * Determina si el libro todavía está en poder del socio.
     * 
     * @return true si devolucion es null, false si ya fue entregado.
     */
    public boolean estaPendiente() {
        return this.devolucion == null;
     }

     /**
     * Calcula la fecha límite para devolver el libro sin incurrir en mora.
     * 
     * @return LocalDate resultante de sumar 14 días a la fecha de retiro.
     */
    public LocalDate vencimiento() { // retiro + 14 dias
        return this.retiro.plusDays(PLAZO_DIAS);
    }            

    /**
     * Calcula la cantidad de días de atraso acumulados.
     * 
     * @param corte Fecha de corte enviada al programa para evaluar préstamos pendientes.
     * @return Número de días en mora (siempre >= 0).
     * @throws IllegalArgumentException Si la fecha de corte es nula.
     */
    public int diasDeAtraso(LocalDate corte) { // siempre >= 0
        if (corte == null) {
            throw new IllegalArgumentException("La fecha de corte no puede ser nula.");
        }
        LocalDate fechaFin = estaPendiente() ? corte : this.devolucion;
        LocalDate limite = vencimiento();

        // Diferencia directa de días mediante época (sin importar ChronoUnit ni otras utilidades)
        long dias = fechaFin.toEpochDay() - limite.toEpochDay();

        if (dias < 0) {
            return 0;
        }

        return (int) dias;

    }
    /**
     * Calcula el monto acumulado de la multa aplicable al préstamo.
     * 
     * @param corte Fecha de corte empleada para calcular los días de mora.
     * @return Monto total de la multa en pesos ($150/día con tope máximo de $3000).
     */
    public int multa(LocalDate corte) { // 150 por dia, tope 3000
        int dias = diasDeAtraso(corte);
        int acumulado = dias * COSTO_MULTA_DIARIA;
        return Math.min(acumulado, TOPE_MULTA);
    }        
}
