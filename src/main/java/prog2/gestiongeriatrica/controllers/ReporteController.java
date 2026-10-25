package prog2.gestiongeriatrica.controllers;

import java.util.List;
import java.util.Optional;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.servicios.ReporteServicio;

/**
 * Recibe los pedidos de la vista de consultas y se los pasa al service.
 * @author Emanuel
 */
public class ReporteController {

    private final ReporteServicio servicio;

    public ReporteController(ReporteServicio servicio) {
        this.servicio = servicio;
    }

    public Optional<String> habitacionDeResidente(int idResidente) {
        return servicio.habitacionDeResidente(idResidente);
    }

    public List<PersonaDTO> residentesDeEnfermero(int idEnfermero) {
        return servicio.residentesDeEnfermero(idEnfermero);
    }

    public Optional<PersonaDTO> enfermeroDeResidente(int idResidente) {
        return servicio.enfermeroDeResidente(idResidente);
    }
}
