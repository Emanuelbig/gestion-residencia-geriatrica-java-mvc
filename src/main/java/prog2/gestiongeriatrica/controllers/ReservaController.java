package prog2.gestiongeriatrica.controllers;

import java.util.List;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;
import prog2.gestiongeriatrica.servicios.ReservaServicio;

/**
 * Recibe los pedidos de la vista de reservas y se los pasa al service.
 * @author Emanuel
 */
public class ReservaController {

    private final ReservaServicio servicio;

    public ReservaController(ReservaServicio servicio) {
        this.servicio = servicio;
    }

    public void reservar(int idResidente, String codigo) {
        servicio.reservar(idResidente, codigo);
    }

    public void liberar(String codigo) {
        servicio.liberar(codigo);
    }

    public List<PersonaHabitacionDTO> listar() {
        return servicio.listar();
    }

    public List<PersonaDTO> residentesSinHabitacion() {
        return servicio.residentesSinHabitacion();
    }

    public List<String> habitacionesLibres() {
        return servicio.habitacionesLibres();
    }
}
