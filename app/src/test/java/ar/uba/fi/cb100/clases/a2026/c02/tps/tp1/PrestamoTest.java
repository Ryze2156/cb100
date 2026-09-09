package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas automáticas de la clase Prestamo con JUnit 5.
 */
class PrestamoTest {

    @Test
    void constructorLanzaExcepcionSiFechaRetiroEsNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestamo(null, 41234, "Ana Gomez", "9789871234567", "Estructuras de Datos", null);
        });
    }

    @Test
    void constructorLanzaExcepcionSiPadronEsInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestamo(LocalDate.of(2026, 3, 2), 0, "Ana Gomez", "9789871234567", "Estructuras de Datos", null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestamo(LocalDate.of(2026, 3, 2), -10, "Ana Gomez", "9789871234567", "Estructuras de Datos", null);
        });
    }

    @Test
    void constructorLanzaExcepcionSiSocioEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestamo(LocalDate.of(2026, 3, 2), 41234, null, "9789871234567", "Estructuras de Datos", null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestamo(LocalDate.of(2026, 3, 2), 41234, "   ", "9789871234567", "Estructuras de Datos", null);
        });
    }

    @Test
    void constructorLanzaExcepcionSiIsbnEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestamo(LocalDate.of(2026, 3, 2), 41234, "Ana Gomez", null, "Estructuras de Datos", null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestamo(LocalDate.of(2026, 3, 2), 41234, "Ana Gomez", "", "Estructuras de Datos", null);
        });
    }

    @Test
    void constructorLanzaExcepcionSiTituloEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestamo(LocalDate.of(2026, 3, 2), 41234, "Ana Gomez", "9789871234567", null, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Prestamo(LocalDate.of(2026, 3, 2), 41234, "Ana Gomez", "9789871234567", " ", null);
        });
    }

    @Test
    void constructorLanzaExcepcionSiDevolucionEsAnteriorARetiro() {
        LocalDate retiro = LocalDate.of(2026, 4, 15);
        LocalDate devolucionAnterior = LocalDate.of(2026, 4, 1);

        assertThrows(IllegalArgumentException.class, () -> {
            new Prestamo(retiro, 43310, "Elena Sosa", "9789871234567", "Algoritmos", devolucionAnterior);
        });
    }

    @Test
    void vencimientoEsExactamenteCatorceDiasDespuesDelRetiro() {
        LocalDate retiro = LocalDate.of(2026, 3, 2);
        Prestamo prestamo = new Prestamo(retiro, 41234, "Ana Gomez", "9789871234567", "Estructuras de Datos", null);

        assertEquals(LocalDate.of(2026, 3, 16), prestamo.vencimiento());
    }

    @Test
    void diasDeAtrasoEsCeroSiSeDevolvioEnFecha() {
        LocalDate retiro = LocalDate.of(2026, 3, 2);
        LocalDate devolucion = LocalDate.of(2026, 3, 16);
        LocalDate corte = LocalDate.of(2026, 5, 4);

        Prestamo prestamo = new Prestamo(retiro, 41234, "Ana Gomez", "9789871234567", "Estructuras de Datos", devolucion);

        assertEquals(0, prestamo.diasDeAtraso(corte));
        assertEquals(0, prestamo.multa(corte));
    }

    @Test
    void diasDeAtrasoCalculaCorrectamentePrestamoPendiente() {
        LocalDate retiro = LocalDate.of(2026, 3, 11);
        LocalDate corte = LocalDate.of(2026, 5, 4);

        Prestamo prestamo = new Prestamo(retiro, 40555, "Diego Ruiz", "9788478290499", "El Lenguaje de Programacion C", null);

        assertTrue(prestamo.estaPendiente());
        assertEquals(40, prestamo.diasDeAtraso(corte));
    }

    @Test
    void multaRespetaElTopeMaximoDe3000Pesos() {
        LocalDate retiro = LocalDate.of(2026, 3, 11);
        LocalDate corte = LocalDate.of(2026, 5, 4);

        Prestamo prestamo = new Prestamo(retiro, 40555, "Diego Ruiz", "9788478290499", "El Lenguaje de Programacion C", null);

        assertEquals(3000, prestamo.multa(corte));
    }
}