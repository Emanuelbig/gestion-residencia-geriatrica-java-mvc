package prog2.gestiongeriatrica.controllers;

import java.util.List;
import prog2.gestiongeriatrica.dto.HabitacionDTO;
import prog2.gestiongeriatrica.servicios.HabitacionServicio;

/**
 * Recibe los pedidos de la vista de habitaciones y se los pasa al service.
 * @author Emanuel
 */
public class HabitacionController {

    private final HabitacionServicio servicio;

    public HabitacionController(HabitacionServicio servicio) {
        this.servicio = servicio;
    }

    public void alta(String codigo) {
        servicio.crear(codigo);
    }

    public List<HabitacionDTO> listar() {
        return servicio.listar();
    }

    public void baja(String codigo) {
        servicio.eliminar(codigo);
    }
}
