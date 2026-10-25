package prog2.gestiongeriatrica.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;
import prog2.gestiongeriatrica.excepciones.EntidadNoEncontradaException;
import prog2.gestiongeriatrica.modelos.Asignacion;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Reserva;
import prog2.gestiongeriatrica.modelos.Residente;

/**
 * Consultas que cruzan datos de varias entidades.
 * @author Emanuel
 */
public class ReporteServicio {
    
    private final GenericDAO<Residente, Integer> residenteDAO;
    private final GenericDAO<Enfermero, Integer> enfermeroDAO;
    private final GenericDAO<Reserva, String> reservaDAO;
    private final GenericDAO<Asignacion, String> asignacionDAO;

    public ReporteServicio(GenericDAO<Residente, Integer> residenteDAO, GenericDAO<Enfermero, Integer> enfermeroDAO, GenericDAO<Reserva, String> reservaDAO, GenericDAO<Asignacion, String> asignacionDAO) {
        this.residenteDAO = residenteDAO;
        this.enfermeroDAO = enfermeroDAO;
        this.reservaDAO = reservaDAO;
        this.asignacionDAO = asignacionDAO;
    }


    /** 1. El residente con la habitación que ocupa (sin código si no tiene). */
    public PersonaHabitacionDTO habitacionDeResidente(int idResidente) {
        Residente residente = buscarResidente(idResidente);
        Optional<Reserva> reserva = buscarReservaDe(idResidente);
        String codigo = null;                                   // queda null si no tiene habitación
        if (reserva.isPresent()) {
            codigo = reserva.get().getCodigoHabitacion();
        }
        return new PersonaHabitacionDTO(PersonaDTO.desde(residente), codigo);
    }

    /** 2. ¿Qué residentes atiende el enfermero? */
    public List<PersonaDTO> residentesDeEnfermero(int idEnfermero) {
        if (enfermeroDAO.buscarPorId(idEnfermero).isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el enfermero " + idEnfermero + ".");
        }
        List<PersonaDTO> resultado = new ArrayList<>();
        for (Asignacion a : asignacionDAO.listarTodos()) {
            if (a.getIdEnfermero() == idEnfermero) {
                Optional<Reserva> reserva = reservaDAO.buscarPorId(a.getCodigoHabitacion());
                if (reserva.isPresent()) {
                    Optional<Residente> r = residenteDAO.buscarPorId(reserva.get().getIdResidente());
                    if (r.isPresent()) {
                        resultado.add(PersonaDTO.desde(r.get()));
                    }
                }
            }
        }
        return resultado;
    }

    /** 3. ¿Qué enfermero atiende al residente? Vacío si no tiene. */
    public Optional<PersonaDTO> enfermeroDeResidente(int idResidente) {
        buscarResidente(idResidente);                           // si no existe, lanza la excepción
        Optional<Reserva> reserva = buscarReservaDe(idResidente);
        if (reserva.isEmpty()) {
            return Optional.empty();
        }
        Optional<Asignacion> asignacion = asignacionDAO.buscarPorId(reserva.get().getCodigoHabitacion());
        if (asignacion.isEmpty()) {
            return Optional.empty();
        }
        Optional<Enfermero> enfermero = enfermeroDAO.buscarPorId(asignacion.get().getIdEnfermero());
        if (enfermero.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(PersonaDTO.desde(enfermero.get()));
    }

    private Residente buscarResidente(int idResidente) {
        Optional<Residente> residente = residenteDAO.buscarPorId(idResidente);
        if (residente.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el residente " + idResidente + ".");
        }
        return residente.get();
    }

    private Optional<Reserva> buscarReservaDe(int idResidente) {
        for (Reserva r : reservaDAO.listarTodos()) {
            if (r.getIdResidente() == idResidente) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

}
