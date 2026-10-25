package prog2.gestiongeriatrica.vistas;

import java.util.List;
import prog2.gestiongeriatrica.controllers.ReservaController;
import prog2.gestiongeriatrica.dto.HabitacionDTO;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;
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

    // Antes de pedir los datos muestra qué se puede elegir, así el usuario no tiene que adivinar
    private void reservar() {
        List<PersonaDTO> residentes = controller.residentesSinHabitacion();
        List<String> libres = controller.habitacionesLibres();
        if (residentes.isEmpty()) {
            System.out.println("Todos los residentes ya tienen habitación.");
        } else if (libres.isEmpty()) {
            System.out.println("No hay habitaciones libres.");
        } else {
            System.out.println("Residentes sin habitación:");
            for (PersonaDTO r : residentes) {
                System.out.println("  " + r.getId() + " | " + r.getNombreCompleto());
            }
            System.out.println("Habitaciones libres: " + String.join(", ", libres));
            int idResidente = consola.pedirEntero("Id del residente: ");
            controller.reservar(idResidente, pedirCodigo());
            System.out.println("Reservada.");
        }
    }

    private void liberar() {
        List<PersonaHabitacionDTO> ocupadas = controller.listar();
        if (ocupadas.isEmpty()) {
            System.out.println("No hay habitaciones ocupadas.");
        } else {
            System.out.println("Habitaciones ocupadas:");
            for (PersonaHabitacionDTO o : ocupadas) {
                System.out.println("  " + o.getCodigoHabitacion() + " | " + o.getPersona().getNombreCompleto());
            }
            controller.liberar(pedirCodigo());
            System.out.println("Liberada.");
        }
    }

    private String pedirCodigo() {
        return consola.pedirConFormato("Código de habitación: ", HabitacionDTO.PATRON_CODIGO, "Formato: HAB-101.");
    }
}
