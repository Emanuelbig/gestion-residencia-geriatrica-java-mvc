package prog2.gestiongeriatrica.vistas;

import java.util.List;
import java.util.Optional;
import prog2.gestiongeriatrica.controllers.ReporteController;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.excepciones.GeriatricoException;

/**
 * Pantalla de consultas que cruzan residentes, enfermeros y habitaciones.
 * @author Emanuel
 */
public class VistaReportes implements Vista {

    private final ReporteController controller;
    private final Consola consola;

    public VistaReportes(ReporteController controller, Consola consola) {
        this.controller = controller;
        this.consola = consola;
    }

    @Override
    public String getTitulo() {
        return "Consultas";
    }

    @Override
    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("-- CONSULTAS --");
            System.out.println("1. Habitación de un residente");
            System.out.println("2. Residentes de un enfermero");
            System.out.println("3. Enfermero de un residente");
            System.out.println("4. Volver");
            int opcion = consola.pedirEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1: 
                        habitacionDeResidente();
                        break;
                    case 2: 
                        residentesDeEnfermero();
                        break;
                    case 3: 
                        enfermeroDeResidente();
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

    private void habitacionDeResidente() {
        int id = consola.pedirEntero("Id del residente: ");
        Optional<String> codigo = controller.habitacionDeResidente(id);
        if (codigo.isPresent()) {
            System.out.println("Está en la habitación " + codigo.get() + ".");
        } else {
            System.out.println("No tiene habitación asignada.");
        }
    }

    private void residentesDeEnfermero() {
        int id = consola.pedirEntero("Id del enfermero: ");
        List<PersonaDTO> residentes = controller.residentesDeEnfermero(id);
        if (residentes.isEmpty()) {
            System.out.println("No atiende a ningún residente.");
        }
        for (PersonaDTO r : residentes) {
            System.out.println(r.getId() + " | " + r.getNombre() + " " + r.getApellido());
        }
    }

    private void enfermeroDeResidente() {
        int id = consola.pedirEntero("Id del residente: ");
        Optional<PersonaDTO> enfermero = controller.enfermeroDeResidente(id);
        if (enfermero.isPresent()) {
            PersonaDTO e = enfermero.get();
            System.out.println("Lo atiende " + e.getNombre() + " " + e.getApellido() + " (id " + e.getId() + ").");
        } else {
            System.out.println("No tiene enfermero asignado.");
        }
    }
}
