package prog2.gestiongeriatrica.vistas;

import java.util.Optional;
import prog2.gestiongeriatrica.controllers.PersonaController;
import prog2.gestiongeriatrica.controllers.ReporteController;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;

/**
 * Pantalla de residentes. La ficha muestra además su habitación y el enfermero que lo atiende.
 * @author Emanuel
 */
public class VistaResidentes extends VistaPersonas {

    private final ReporteController reportes;     // para la ficha: habitación y enfermero del residente

    public VistaResidentes(PersonaController controller, ReporteController reportes, Consola consola) {
        super("Residentes", controller, consola);
        this.reportes = reportes;
    }

    @Override
    protected void mostrarFicha(PersonaDTO r) {
        System.out.println("Residente " + r.getNombreCompleto() + " (id " + r.getId() + ")");
        System.out.println("  DNI: " + r.getDni());
        PersonaHabitacionDTO habitacion = reportes.habitacionDeResidente(r.getId());
        System.out.println("  Habitación: " + (habitacion.tieneHabitacion() ? habitacion.getCodigoHabitacion() : "sin habitación"));
        Optional<PersonaDTO> enfermero = reportes.enfermeroDeResidente(r.getId());
        if (enfermero.isPresent()) {
            System.out.println("  Enfermero: " + enfermero.get().getNombreCompleto() + " (id " + enfermero.get().getId() + ")");
        } else {
            System.out.println("  Enfermero: sin enfermero asignado");
        }
    }
}
