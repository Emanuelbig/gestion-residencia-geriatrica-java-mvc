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
import prog2.gestiongeriatrica.modelos.Reserva;
import prog2.gestiongeriatrica.modelos.Residente;

/**
 * Reglas de las reservas: un residente ocupa como máximo una habitación,
 * y una habitación tiene como máximo un residente.
 * @author Emanuel
 */
public class ReservaServicio {
    
    private final GenericDAO<Reserva, String> reservaDAO;
    private final GenericDAO<Residente, Integer> residenteDAO;
    private final GenericDAO<Habitacion, String> habitacionDAO;

    public ReservaServicio(GenericDAO<Reserva, String> reservaDAO,
                       GenericDAO<Residente, Integer> residenteDAO,
                       GenericDAO<Habitacion, String> habitacionDAO) {
        this.reservaDAO = reservaDAO;
        this.residenteDAO = residenteDAO;
        this.habitacionDAO = habitacionDAO;
    }
    
     public void reservar(int idResidente, String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (residenteDAO.buscarPorId(idResidente).isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el residente " + idResidente + ".");
        }
        if (habitacionDAO.buscarPorId(normalizado).isEmpty()) {
            throw new EntidadNoEncontradaException("No existe la habitación " + normalizado + ".");
        }
        if (tieneReserva(idResidente)) {
            throw new OperacionNoPermitidaException("El residente " + idResidente + " ya tiene una habitación.");
        }
        if (reservaDAO.buscarPorId(normalizado).isPresent()) {
            throw new OperacionNoPermitidaException("La habitación " + normalizado + " ya está ocupada.");
        }
        reservaDAO.crear(new Reserva(idResidente, normalizado));
    }

    public void liberar(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (reservaDAO.buscarPorId(normalizado).isEmpty()) {
            throw new EntidadNoEncontradaException("La habitación " + normalizado + " no está ocupada.");
        }
        reservaDAO.eliminar(normalizado);
    }

    /** Habitaciones ocupadas, cada una con su residente. */
    public List<PersonaHabitacionDTO> listar() {
        List<PersonaHabitacionDTO> resultado = new ArrayList<>();
        for (Reserva r : reservaDAO.listarTodos()) {
            Optional<Residente> residente = residenteDAO.buscarPorId(r.getIdResidente());
            if (residente.isPresent()) {
                resultado.add(new PersonaHabitacionDTO(PersonaDTO.desde(residente.get()), r.getCodigoHabitacion()));
            }
        }
        return resultado;
    }

    /** Residentes que todavía no tienen habitación (los únicos que se pueden reservar). */
    public List<PersonaDTO> residentesSinHabitacion() {
        List<PersonaDTO> resultado = new ArrayList<>();
        for (Residente r : residenteDAO.listarTodos()) {
            if (!tieneReserva(r.getId())) {
                resultado.add(PersonaDTO.desde(r));
            }
        }
        return resultado;
    }

    /** Códigos de las habitaciones libres. */
    public List<String> habitacionesLibres() {
        List<String> resultado = new ArrayList<>();
        for (Habitacion h : habitacionDAO.listarTodos()) {
            if (reservaDAO.buscarPorId(h.getCodigo()).isEmpty()) {
                resultado.add(h.getCodigo());
            }
        }
        return resultado;
    }

    private boolean tieneReserva(int idResidente) {
        for (Reserva r : reservaDAO.listarTodos()) {
            if (r.getIdResidente() == idResidente) {
                return true;
            }
        }
        return false;
    }
}
