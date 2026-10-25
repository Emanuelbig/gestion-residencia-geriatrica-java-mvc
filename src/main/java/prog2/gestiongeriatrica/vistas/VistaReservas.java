package prog2.gestiongeriatrica.vistas;

import prog2.gestiongeriatrica.controllers.ReservaController;
import prog2.gestiongeriatrica.dto.HabitacionDTO;
import prog2.gestiongeriatrica.excepciones.GeriatricoException;

/**
 * Pantalla para reservar y liberar habitaciones.
 *
 * @author Emanuel
 */
public class VistaReservas implements Vista {

    private final ReservaController controller;
    private final Consola consola;

    public VistaReservas(ReservaController controller, Consola consola) {
        this.controller = controller;
        this.consola = consola;
    }

    @Override
    public String getTitulo() {
        return "Reservar / liberar habitación";
    }

    @Override
    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("-- RESERVAS --");
            System.out.println("1. Reservar | 2. Liberar | 3. Volver");
            int opcion = consola.pedirEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1:
                        reservar();
                        break;
                    case 2:
                        liberar();
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

    private void reservar() {
        int idResidente = consola.pedirEntero("Id del residente: ");
        String codigo = consola.pedirConFormato("Código de habitación: ", HabitacionDTO.PATRON_CODIGO, "Formato: HAB-101.");
        controller.reservar(idResidente, codigo);
        System.out.println("Reservada.");
    }

    private void liberar() {
        String codigo = consola.pedirConFormato("Código de habitación: ", HabitacionDTO.PATRON_CODIGO, "Formato: HAB-101.");
        controller.liberar(codigo);
        System.out.println("Liberada.");
    }
}
