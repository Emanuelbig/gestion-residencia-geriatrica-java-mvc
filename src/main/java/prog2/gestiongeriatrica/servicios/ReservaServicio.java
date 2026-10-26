package prog2.gestiongeriatrica.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;
import prog2.gestiongeriatrica.excepciones.EntidadNoEncontradaException;
import prog2.gestiongeriatrica.excepciones.OperacionNoPermitidaException;
import prog2.gestiongeriatrica.modelos.Habitacion;
import prog2.gestiongeriatrica.modelos.Residente;

/**
 * Reglas de las reservas: un residente ocupa como máximo una habitación,
 * y una habitación tiene como máximo un residente. La reserva se guarda en la habitación (idResidente).
 * @author Emanuel
 */
public class ReservaServicio {

    private final GenericDAO<Habitacion, String> habitacionDAO;
    private final GenericDAO<Residente, Integer> residenteDAO;

    public ReservaServicio(GenericDAO<Habitacion, String> habitacionDAO,
                       GenericDAO<Residente, Integer> residenteDAO) {
        this.habitacionDAO = habitacionDAO;
        this.residenteDAO = residenteDAO;
    }

    public void reservar(int idResidente, String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (residenteDAO.buscarPorId(idResidente).isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el residente " + idResidente + ".");
        }
        Habitacion habitacion = buscarHabitacion(normalizado);
        if (tieneHabitacion(idResidente)) {
            throw new OperacionNoPermitidaException("El residente " + idResidente + " ya tiene una habitación.");
        }
        if (habitacion.isOcupada()) {
            throw new OperacionNoPermitidaException("La habitación " + normalizado + " ya está ocupada.");
        }
        habitacionDAO.actualizar(habitacion.conResidente(idResidente));
    }

    public void liberar(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        Habitacion habitacion = buscarHabitacion(normalizado);
        if (!habitacion.isOcupada()) {
            throw new EntidadNoEncontradaException("La habitación " + normalizado + " no está ocupada.");
        }
        habitacionDAO.actualizar(habitacion.conResidente(null));
    }

    /** Habitaciones ocupadas, cada una con su residente. */
    public List<PersonaHabitacionDTO> listar() {
        List<PersonaHabitacionDTO> resultado = new ArrayList<>();
        for (Habitacion h : habitacionDAO.listarTodos()) {
            if (h.isOcupada()) {
                Optional<Residente> residente = residenteDAO.buscarPorId(h.getIdResidente());
                if (residente.isPresent()) {
                    resultado.add(new PersonaHabitacionDTO(PersonaDTO.desde(residente.get()), h.getCodigo()));
                }
            }
        }
        return resultado;
    }

    /** Residentes que todavía no tienen habitación (los únicos que se pueden reservar). */
    public List<PersonaDTO> residentesSinHabitacion() {
        List<PersonaDTO> resultado = new ArrayList<>();
        for (Residente r : residenteDAO.listarTodos()) {
            if (!tieneHabitacion(r.getId())) {
                resultado.add(PersonaDTO.desde(r));
            }
        }
        return resultado;
    }

    /** Códigos de las habitaciones libres. */
    public List<String> habitacionesLibres() {
        List<String> resultado = new ArrayList<>();
        for (Habitacion h : habitacionDAO.listarTodos()) {
            if (!h.isOcupada()) {
                resultado.add(h.getCodigo());
            }
        }
        return resultado;
    }

    private Habitacion buscarHabitacion(String codigo) {
        Optional<Habitacion> habitacion = habitacionDAO.buscarPorId(codigo);
        if (habitacion.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe la habitación " + codigo + ".");
        }
        return habitacion.get();
    }

    private boolean tieneHabitacion(int idResidente) {
        for (Habitacion h : habitacionDAO.listarTodos()) {
            if (h.estaOcupadaPor(idResidente)) {
                return true;
            }
        }
        return false;
    }
}
