package prog2.gestiongeriatrica.controllers;

import java.util.List;
import prog2.gestiongeriatrica.dto.PersonaDTO;

/**
 * Lo que se puede hacer con cualquier persona del sistema (residente o enfermero).
 * Permite tener una sola vista para las dos.
 * @author Emanuel
 */
public interface PersonaController {

    void alta(PersonaDTO datos);

    List<PersonaDTO> listar();

    void modificar(int id, PersonaDTO datos);

    void baja(int id);
    
    PersonaDTO buscar(int id);
}
