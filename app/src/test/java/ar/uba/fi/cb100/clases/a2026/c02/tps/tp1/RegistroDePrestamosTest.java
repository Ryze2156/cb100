package ar.uba.fi.cb100.clases.a2026.c02.tps.tp1;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas automáticas del contrato del TDA RegistroDePrestamos (Clase 2 del TP).
 */
class RegistroDePrestamosTest {

    @Test
    void registrarYObtenerRespetanElContrato() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        LocalDate fecha = LocalDate.of(2026, 3, 2);
        Prestamo p = new Prestamo(fecha, 41234, "Ana Gomez", "9789871234567", "Estructuras de Datos", null);

        registro.registrar(p);

        assertEquals(1, registro.cantidad());
        assertEquals(p, registro.obtener(0));
    }

    @Test
    void obtenerLanzaExcepcionSiIndiceEsInvalido() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();

        assertThrows(IndexOutOfBoundsException.class, () -> registro.obtener(0));
        assertThrows(IndexOutOfBoundsException.class, () -> registro.obtener(-1));
    }
    
    @Test
    void registrarVeintePrestamosHaceCrecerLaCapacidadDelArreglo() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        LocalDate fecha = LocalDate.of(2026, 3, 2);

        // Registramos 20 préstamos para forzar las redimensiones (superando la capacidad inicial de 8)
        for (int i = 0; i < 20; i++) {
            registro.registrar(new Prestamo(fecha, 1000 + i, "Socio " + i, "111", "Titulo " + i, null));
        }

        // Verificamos que se hayan cargado los 20 y que cantidad() devuelva el número correcto
        assertEquals(20, registro.cantidad(), "Debería haber exactamente 20 préstamos registrados.");
    }

    @Test
    void padronesDevuelveArregloSinRepetidosEnOrdenDeAparicion() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        LocalDate fecha = LocalDate.of(2026, 3, 2);

        registro.registrar(new Prestamo(fecha, 41234, "Ana Gomez", "9789871234567", "Estructuras de Datos", null));
        registro.registrar(new Prestamo(fecha, 39876, "Bruno Ferrari", "9780262033848", "Algorithms", null));
        registro.registrar(new Prestamo(fecha, 41234, "Ana Gomez", "9789871234567", "Clean Code", null));

        int[] padrones = registro.padrones();

        assertEquals(2, padrones.length);
        assertEquals(41234, padrones[0]);
        assertEquals(39876, padrones[1]);
    }

    @Test
void registroCreceMasAllaDeLaCapacidadInicial() {
    RegistroDePrestamos registro = new RegistroSobreArreglo();
    LocalDate fecha = LocalDate.of(2026, 3, 2);

    for (int i = 0; i < 20; i++) {
        registro.registrar(new Prestamo(fecha, 40000 + i, "Socio " + i,
                "9789871234567", "Libro " + i, null));
    }

    assertEquals(20, registro.cantidad());
}

    @Test
    void prestamosDeConPadronInexistenteDevuelveArregloVacio() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        LocalDate fecha = LocalDate.of(2026, 3, 2);

        registro.registrar(new Prestamo(fecha, 41234, "Ana Gomez", "9789871234567", "Estructuras de Datos", null));

        Prestamo[] prestamos = registro.prestamosDe(99999);

        assertNotNull(prestamos);
        assertEquals(0, prestamos.length);
    }

    @Test
    void titulosMasPedidosDesempataAlfabeticamente() {
        RegistroDePrestamos registro = new RegistroSobreArreglo();
        LocalDate fecha = LocalDate.of(2026, 3, 2);

        registro.registrar(new Prestamo(fecha, 1, "Socio 1", "111", "Introduction to Algorithms", null));
        registro.registrar(new Prestamo(fecha, 2, "Socio 2", "111", "Introduction to Algorithms", null));
        registro.registrar(new Prestamo(fecha, 3, "Socio 3", "222", "Clean Code", null));
        registro.registrar(new Prestamo(fecha, 4, "Socio 4", "222", "Clean Code", null));

        String[] ranking = registro.titulosMasPedidos(2);

        assertEquals(2, ranking.length);
        assertEquals("Clean Code", ranking[0]);
        assertEquals("Introduction to Algorithms", ranking[1]);
    }
}