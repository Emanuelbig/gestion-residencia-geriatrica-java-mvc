package prog2.gestiongeriatrica.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.excepciones.DatosInvalidosException;
import prog2.gestiongeriatrica.excepciones.EntidadDuplicadaException;
import prog2.gestiongeriatrica.excepciones.EntidadNoEncontradaException;
import prog2.gestiongeriatrica.excepciones.OperacionNoPermitidaException;
import prog2.gestiongeriatrica.modelos.Persona;

/**
 * Reglas comunes a todas las personas del sistema: alta, listado, modificación y baja.
 * Cada subclase indica cómo se crea su tipo de persona y cuándo está en uso.
 *
 * @param <T> tipo de persona (Residente, Enfermero)
 * @author Emanuel
 */
public abstract class PersonaServicio<T extends Persona> {

    private final GenericDAO<T, Integer> dao;
    private final GenericDAO<? extends Persona, Integer> otrasPersonasDAO;   // el DNI no se repite entre tipos
    private final int primerId;

    protected PersonaServicio(GenericDAO<T, Integer> dao,
                              GenericDAO<? extends Persona, Integer> otrasPersonasDAO,
                              int primerId) {
        this.dao = dao;
        this.otrasPersonasDAO = otrasPersonasDAO;
        this.primerId = primerId;
    }

    /** Crea el objeto concreto (new Residente, new Enfermero...). */
    protected abstract T crearPersona(int id, String dni, String nombre, String apellido);

    /** Indica si la persona está siendo usada (reserva, asignaciones) y no se puede borrar. */
    protected abstract boolean estaEnUso(int id);

    /** Mensaje para cuando no se puede borrar porque está en uso. */
    protected abstract String mensajeEnUso(int id);

    /** Nombre para los mensajes: "residente", "enfermero". */
    protected abstract String nombreEntidad();

    public void crear(PersonaDTO datos) {
        datos = limpiar(datos);
        validar(datos);
        if (existeDni(datos.getDni())) {
            throw new EntidadDuplicadaException("Ya existe una persona con el DNI " + datos.getDni() + ".");
        }
        int id = generarId();
        dao.crear(crearPersona(id, datos.getDni(), datos.getNombre(), datos.getApellido()));
    }

    public List<PersonaDTO> listar() {
        List<PersonaDTO> resultado = new ArrayList<>();
        for (T p : dao.listarTodos()) {
            resultado.add(aDTO(p));
        }
        return resultado;
    }

    public PersonaDTO obtener(int id) {
        return aDTO(buscar(id));
    }

    public void modificar(int id, PersonaDTO datos) {
        datos = limpiar(datos);
        T actual = buscar(id);
        validar(datos);
        boolean cambioDni = !actual.getDni().equals(datos.getDni());
        if (cambioDni && existeDni(datos.getDni())) {
            throw new EntidadDuplicadaException("Ya existe una persona con el DNI " + datos.getDni() + ".");
        }
        dao.actualizar(crearPersona(id, datos.getDni(), datos.getNombre(), datos.getApellido()));
    }

    public void eliminar(int id) {
        buscar(id);
        if (estaEnUso(id)) {
            throw new OperacionNoPermitidaException(mensajeEnUso(id));
        }
        dao.eliminar(id);
    }

    private T buscar(int id) {
        Optional<T> encontrado = dao.buscarPorId(id);
        if (encontrado.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el " + nombreEntidad() + " " + id + ".");
        }
        return encontrado.get();
    }

    private PersonaDTO aDTO(T p) {
        return new PersonaDTO(p.getDni(), p.getNombre(), p.getApellido(), p.getId());
    }

    private PersonaDTO limpiar(PersonaDTO datos) {
        String dni = datos.getDni() == null ? null : datos.getDni().trim();
        return new PersonaDTO(dni, Persona.normalizarNombre(datos.getNombre()),
                Persona.normalizarNombre(datos.getApellido()), datos.getId());
    }

    private void validar(PersonaDTO datos) {
        if (datos.getDni() == null || !datos.getDni().matches(PersonaDTO.PATRON_DNI)) {
            throw new DatosInvalidosException("El DNI debe tener 7 u 8 números.");
        }
        if (!esNombreValido(datos.getNombre())) {
            throw new DatosInvalidosException("El nombre solo puede tener letras y espacios.");
        }
        if (!esNombreValido(datos.getApellido())) {
            throw new DatosInvalidosException("El apellido solo puede tener letras y espacios.");
        }
    }

    private boolean esNombreValido(String texto) {
        return texto != null && texto.matches(PersonaDTO.PATRON_NOMBRE);
    }

    private boolean existeDni(String dni) {
        for (Persona p : dao.listarTodos()) {
            if (p.getDni().equals(dni)) {
                return true;
            }
        }
        for (Persona p : otrasPersonasDAO.listarTodos()) {
            if (p.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }

    private int generarId() {
        int mayor = primerId - 1;
        for (T p : dao.listarTodos()) {
            if (p.getId() > mayor) {
                mayor = p.getId();
            }
        }
        return mayor + 1;
    }
}
