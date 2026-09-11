package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas automáticas para Reporteador.porSocio: orden por multa descendente
 * y, a igual multa, por nombre de socio alfabético.
 */
class ReporteadorTest {

    @Test
    void porSocioOrdenaPorMultaDescendenteYDesempataAlfabeticamente() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        LocalDate corte = LocalDate.of(2026, 2, 4);

        // Zulema y Ana quedan con la misma multa (tope de $3000: 20 dias de atraso cada una).
        registro.registrar(new Prestamo(LocalDate.of(2026, 1, 1), 300, "Zulema Diaz",
                "111", "Libro Z", null));
        registro.registrar(new Prestamo(LocalDate.of(2026, 1, 1), 100, "Ana Lopez",
                "222", "Libro A", null));
        // Bruno devuelve en fecha: sin atraso ni multa.
        registro.registrar(new Prestamo(LocalDate.of(2026, 1, 1), 200, "Bruno Perez",
                "333", "Libro B", LocalDate.of(2026, 1, 15)));

        FilaDeSocio[] filas = Reporteador.porSocio(registro, corte);

        assertEquals(3, filas.length);

        // Empate en $3000: alfabeticamente, Ana antes que Zulema.
        assertEquals("Ana Lopez", filas[0].socio());
        assertEquals(3000, filas[0].multa());

        assertEquals("Zulema Diaz", filas[1].socio());
        assertEquals(3000, filas[1].multa());

        // Sin multa: queda ultimo.
        assertEquals("Bruno Perez", filas[2].socio());
        assertEquals(0, filas[2].multa());
        assertEquals("AL_DIA", filas[2].estado());
    }
}
