package prog2.gestiongeriatrica.controllers;

import java.util.List;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.servicios.ResidenteServicio;

/**
 * Recibe los pedidos de la vista de residentes y se los pasa al service.
 *
 * @author Emanuel
 */
public class ResidenteController implements PersonaController {

    private final ResidenteServicio servicio;

    public ResidenteController(ResidenteServicio servicio) {
        this.servicio = servicio;
    }

    @Override
    public void alta(PersonaDTO datos) {
        servicio.crear(datos);
    }

    @Override
    public List<PersonaDTO> listar() {
        return servicio.listar();
    }

    @Override
    public void modificar(int id, PersonaDTO datos) {
        servicio.modificar(id, datos);
    }

    @Override
    public void baja(int id) {
        servicio.eliminar(id);
    }

    @Override
    public PersonaDTO buscar(int id) {
        return servicio.obtener(id);
    }
}
