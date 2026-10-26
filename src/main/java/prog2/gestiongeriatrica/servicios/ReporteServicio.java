package prog2.gestiongeriatrica.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;
import prog2.gestiongeriatrica.excepciones.EntidadNoEncontradaException;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Habitacion;
import prog2.gestiongeriatrica.modelos.Residente;

/**
 * Consultas que cruzan datos de varias entidades. Todas pasan por la habitación,
 * que es la que sabe quién la ocupa y quién la atiende.
 * @author Emanuel
 */
public class ReporteServicio {

    private final GenericDAO<Residente, Integer> residenteDAO;
    private final GenericDAO<Enfermero, Integer> enfermeroDAO;
    private final GenericDAO<Habitacion, String> habitacionDAO;

    public ReporteServicio(GenericDAO<Residente, Integer> residenteDAO,
                           GenericDAO<Enfermero, Integer> enfermeroDAO,
                           GenericDAO<Habitacion, String> habitacionDAO) {
        this.residenteDAO = residenteDAO;
        this.enfermeroDAO = enfermeroDAO;
        this.habitacionDAO = habitacionDAO;
    }

    /** 1. El residente con la habitación que ocupa (sin código si no tiene). */
    public PersonaHabitacionDTO habitacionDeResidente(int idResidente) {
        Residente residente = buscarResidente(idResidente);
        Optional<Habitacion> habitacion = buscarHabitacionDe(idResidente);
        String codigo = null;                                   // queda null si no tiene habitación
        if (habitacion.isPresent()) {
            codigo = habitacion.get().getCodigo();
        }
        return new PersonaHabitacionDTO(PersonaDTO.desde(residente), codigo);
    }

    /** 2. ¿Qué residentes atiende el enfermero? Los de las habitaciones ocupadas que tiene asignadas. */
    public List<PersonaDTO> residentesDeEnfermero(int idEnfermero) {
        if (enfermeroDAO.buscarPorId(idEnfermero).isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el enfermero " + idEnfermero + ".");
        }
        List<PersonaDTO> resultado = new ArrayList<>();
        for (Habitacion h : habitacionDAO.listarTodos()) {
            if (h.esAtendidaPor(idEnfermero) && h.isOcupada()) {
                Optional<Residente> r = residenteDAO.buscarPorId(h.getIdResidente());
                if (r.isPresent()) {
                    resultado.add(PersonaDTO.desde(r.get()));
                }
            }
        }
        return resultado;
    }

    /** 3. ¿Qué enfermero atiende al residente? Vacío si no tiene. */
    public Optional<PersonaDTO> enfermeroDeResidente(int idResidente) {
        buscarResidente(idResidente);                           // si no existe, lanza la excepción
        Optional<Habitacion> habitacion = buscarHabitacionDe(idResidente);
        if (habitacion.isEmpty() || !habitacion.get().tieneEnfermero()) {
            return Optional.empty();
        }
        Optional<Enfermero> enfermero = enfermeroDAO.buscarPorId(habitacion.get().getIdEnfermero());
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

    private Optional<Habitacion> buscarHabitacionDe(int idResidente) {
        for (Habitacion h : habitacionDAO.listarTodos()) {
            if (h.estaOcupadaPor(idResidente)) {
                return Optional.of(h);
            }
        }
        return Optional.empty();
    }
}
