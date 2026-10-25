package prog2.gestiongeriatrica.vistas;

import prog2.gestiongeriatrica.controllers.AsignacionController;
import prog2.gestiongeriatrica.dto.HabitacionDTO;
import prog2.gestiongeriatrica.excepciones.GeriatricoException;

/**
 * Pantalla para asignar y quitar enfermeros de las habitaciones.
 *
 * @author Emanuel
 */
public class VistaAsignaciones implements Vista {

    private final AsignacionController controller;
    private final Consola consola;

    public VistaAsignaciones(AsignacionController controller, Consola consola) {
        this.controller = controller;
        this.consola = consola;
    }

    @Override
    public String getTitulo() {
        return "Asignar enfermero a habitación";
    }

    @Override
    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("-- ASIGNACIONES --");
            System.out.println("1. Asignar | 2. Quitar | 3. Volver");
            int opcion = consola.pedirEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1:
                        asignar();
                        break;
                    case 2:
                        quitar();
                        break;
                    case 3:
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

    private void asignar() {
        int idEnfermero = consola.pedirEntero("Id del enfermero: ");
        String codigo = consola.pedirConFormato("Código de habitación: ", HabitacionDTO.PATRON_CODIGO, "Formato: HAB-101.");
        controller.asignar(idEnfermero, codigo);
        System.out.println("Asignado.");
    }

    private void quitar() {
        String codigo = consola.pedirConFormato("Código de habitación: ", HabitacionDTO.PATRON_CODIGO, "Formato: HAB-101.");
        controller.quitar(codigo);
        System.out.println("Asignación quitada.");
    }
}
