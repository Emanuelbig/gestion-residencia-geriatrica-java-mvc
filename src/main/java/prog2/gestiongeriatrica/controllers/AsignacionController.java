package prog2.gestiongeriatrica.controllers;

import java.util.List;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;
import prog2.gestiongeriatrica.servicios.AsignacionServicio;

/**
 * Recibe los pedidos de la vista de asignaciones y se los pasa al service.
 * @author Emanuel
 */
public class AsignacionController {

    private final AsignacionServicio servicio;

    public AsignacionController(AsignacionServicio servicio) {
        this.servicio = servicio;
    }

    public void asignar(int idEnfermero, String codigo) {
        servicio.asignar(idEnfermero, codigo);
    }

    public void quitar(String codigo) {
        servicio.quitar(codigo);
    }

    public List<PersonaHabitacionDTO> listar() {
        return servicio.listar();
    }

    public List<String> habitacionesSinEnfermero() {
        return servicio.habitacionesSinEnfermero();
    }
}
