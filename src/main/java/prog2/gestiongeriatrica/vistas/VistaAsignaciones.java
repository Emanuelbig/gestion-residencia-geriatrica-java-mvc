package prog2.gestiongeriatrica.vistas;

import java.util.List;
import prog2.gestiongeriatrica.controllers.AsignacionController;
import prog2.gestiongeriatrica.controllers.PersonaController;
import prog2.gestiongeriatrica.dto.HabitacionDTO;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;
import prog2.gestiongeriatrica.excepciones.GeriatricoException;

/**
 * Pantalla para asignar y quitar enfermeros de las habitaciones.
 *
 * @author Emanuel
 */
public class VistaAsignaciones implements Vista {

    private final AsignacionController controller;
    private final PersonaController enfermeros;   // reusa el listado de enfermeros, no lo duplica
    private final Consola consola;

    public VistaAsignaciones(AsignacionController controller, PersonaController enfermeros, Consola consola) {
        this.controller = controller;
        this.enfermeros = enfermeros;
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

    // Antes de pedir los datos muestra qué se puede elegir, así el usuario no tiene que adivinar
    private void asignar() {
        List<PersonaDTO> lista = enfermeros.listar();
        List<String> sinEnfermero = controller.habitacionesSinEnfermero();
        if (lista.isEmpty()) {
            System.out.println("No hay enfermeros cargados.");
        } else if (sinEnfermero.isEmpty()) {
            System.out.println("Todas las habitaciones ya tienen enfermero.");
        } else {
            System.out.println("Enfermeros:");
            for (PersonaDTO e : lista) {
                System.out.println("  " + e.getId() + " | " + e.getNombreCompleto());
            }
            System.out.println("Habitaciones sin enfermero: " + String.join(", ", sinEnfermero));
            int idEnfermero = consola.pedirEntero("Id del enfermero: ");
            controller.asignar(idEnfermero, pedirCodigo());
            System.out.println("Asignado.");
        }
    }

    private void quitar() {
        List<PersonaHabitacionDTO> asignadas = controller.listar();
        if (asignadas.isEmpty()) {
            System.out.println("No hay habitaciones con enfermero.");
        } else {
            System.out.println("Habitaciones con enfermero:");
            for (PersonaHabitacionDTO a : asignadas) {
                System.out.println("  " + a.getCodigoHabitacion() + " | " + a.getPersona().getNombreCompleto());
            }
            controller.quitar(pedirCodigo());
            System.out.println("Asignación quitada.");
        }
    }

    private String pedirCodigo() {
        return consola.pedirConFormato("Código de habitación: ", HabitacionDTO.PATRON_CODIGO, "Formato: HAB-101.");
    }
}
