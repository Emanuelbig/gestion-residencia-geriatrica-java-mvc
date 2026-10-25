package prog2.gestiongeriatrica.vistas;

import java.util.List;
import prog2.gestiongeriatrica.controllers.HabitacionController;
import prog2.gestiongeriatrica.dto.HabitacionDTO;
import prog2.gestiongeriatrica.dto.HabitacionDetalleDTO;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.excepciones.GeriatricoException;

/**
 * Pantalla de alta, listado, consulta y baja de habitaciones.
 * No hay "modificar": el código es la identidad de la habitación y el estado se calcula con las reservas.
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
            System.out.println("1. Alta | 2. Listar | 3. Consultar | 4. Baja | 5. Volver");
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
                        consultar();
                        break;
                    case 4:
                        baja();
                        break;
                    case 5:
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
        String codigo = controller.alta();
        System.out.println("Registrada la habitación " + codigo + ".");
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

    private void consultar() {
        listar();
        HabitacionDetalleDTO h = controller.buscar(pedirCodigo());
        System.out.println("Habitación " + h.getCodigo() + ":");
        if (h.isOcupada()) {
            PersonaDTO r = h.getResidente();
            System.out.println("  Ocupada por " + r.getNombreCompleto() + " (id " + r.getId() + ").");
        } else {
            System.out.println("  Libre.");
        }
        if (h.tieneEnfermero()) {
            PersonaDTO e = h.getEnfermero();
            System.out.println("  La atiende " + e.getNombreCompleto() + " (id " + e.getId() + ").");
        } else {
            System.out.println("  Sin enfermero asignado.");
        }
    }

    private void baja() {
        listar();
        controller.baja(pedirCodigo());
        System.out.println("Eliminada.");
    }

    private String pedirCodigo() {
        return consola.pedirConFormato("Código de habitación: ", HabitacionDTO.PATRON_CODIGO, "Formato: HAB-101.");
    }
}
