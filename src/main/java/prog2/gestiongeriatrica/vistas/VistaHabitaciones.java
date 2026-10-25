package prog2.gestiongeriatrica.vistas;

import java.util.List;
import prog2.gestiongeriatrica.controllers.HabitacionController;
import prog2.gestiongeriatrica.dto.HabitacionDTO;
import prog2.gestiongeriatrica.excepciones.GeriatricoException;

/**
 * Pantalla de alta, listado y baja de habitaciones.
 *
 * @author Emanuel
 */
public class VistaHabitaciones implements Vista {

    private final HabitacionController controller;
    private final Consola consola;

    public VistaHabitaciones(HabitacionController controller, Consola consola) {
        this.controller = controller;
        this.consola = consola;
    }

    @Override
    public String getTitulo() {
        return "Habitaciones";
    }

    @Override
    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("-- HABITACIONES --");
            System.out.println("1. Alta | 2. Listar | 3. Baja | 4. Volver");
            int opcion = consola.pedirEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1:
                        alta();
                        break;
                    case 2:
                        listar();
                        break;
                    case 3:
                        baja();
                        break;
                    case 4:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (GeriatricoException e) {
                System.out.println("No se pudo completar: " + e.getMessage());
            }
        }
    }

    private void alta() {
        controller.alta(consola.pedirConFormato("Código de habitación: ", HabitacionDTO.PATRON_CODIGO, "Formato: HAB-101."));
        System.out.println("Registrada.");
    }

    private void listar() {
        List<HabitacionDTO> habitaciones = controller.listar();
        if (habitaciones.isEmpty()) {
            System.out.println("No hay habitaciones.");
        }
        for (HabitacionDTO h : habitaciones) {
            String estado = h.isOcupada() ? "ocupada" : "libre";
            System.out.println(h.getCodigo() + " | " + estado);
        }
    }

    private void baja() {
        listar();
        controller.baja(consola.pedirConFormato("Código de habitación: ", HabitacionDTO.PATRON_CODIGO, "Formato: HAB-101."));
        System.out.println("Eliminada.");
    }
}
