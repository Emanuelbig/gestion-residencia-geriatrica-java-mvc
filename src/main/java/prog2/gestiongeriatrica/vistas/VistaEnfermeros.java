package prog2.gestiongeriatrica.vistas;

import java.util.ArrayList;
import java.util.List;
import prog2.gestiongeriatrica.controllers.AsignacionController;
import prog2.gestiongeriatrica.controllers.PersonaController;
import prog2.gestiongeriatrica.controllers.ReporteController;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;

/**
 * Pantalla de enfermeros. La ficha muestra además las habitaciones que atiende y sus residentes.
 * @author Emanuel
 */
public class VistaEnfermeros extends VistaPersonas {

    private final AsignacionController asignaciones;   // para la ficha: habitaciones que atiende
    private final ReporteController reportes;          // para la ficha: residentes que atiende

    public VistaEnfermeros(PersonaController controller, AsignacionController asignaciones,
                           ReporteController reportes, Consola consola) {
        super("Enfermeros", controller, consola);
        this.asignaciones = asignaciones;
        this.reportes = reportes;
    }

    @Override
    protected void mostrarFicha(PersonaDTO e) {
        System.out.println("Enfermero " + e.getNombreCompleto() + " (id " + e.getId() + ")");
        System.out.println("  DNI: " + e.getDni());
        List<String> habitaciones = new ArrayList<>();
        for (PersonaHabitacionDTO a : asignaciones.listar()) {
            if (a.getPersona().getId().equals(e.getId())) {
                habitaciones.add(a.getCodigoHabitacion());
            }
        }
        System.out.println("  Habitaciones: " + (habitaciones.isEmpty() ? "no atiende ninguna" : String.join(", ", habitaciones)));
        List<PersonaDTO> residentes = reportes.residentesDeEnfermero(e.getId());
        if (residentes.isEmpty()) {
            System.out.println("  Residentes: no atiende a ninguno");
        } else {
            System.out.println("  Residentes que atiende:");
            for (PersonaDTO r : residentes) {
                System.out.println("    " + r.getId() + " | " + r.getNombreCompleto());
            }
        }
    }
}
