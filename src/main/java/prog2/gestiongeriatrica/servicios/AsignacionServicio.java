package prog2.gestiongeriatrica.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;
import prog2.gestiongeriatrica.excepciones.EntidadNoEncontradaException;
import prog2.gestiongeriatrica.excepciones.OperacionNoPermitidaException;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Habitacion;

/**
 * Reglas de las asignaciones: un enfermero puede atender varias habitaciones,
 * pero cada habitación tiene como máximo un enfermero. La asignación se guarda en la habitación (idEnfermero).
 * @author Emanuel
 */
public class AsignacionServicio {

    private final GenericDAO<Habitacion, String> habitacionDAO;
    private final GenericDAO<Enfermero, Integer> enfermeroDAO;

    public AsignacionServicio(GenericDAO<Habitacion, String> habitacionDAO,
                          GenericDAO<Enfermero, Integer> enfermeroDAO) {
        this.habitacionDAO = habitacionDAO;
        this.enfermeroDAO = enfermeroDAO;
    }

    public void asignar(int idEnfermero, String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (enfermeroDAO.buscarPorId(idEnfermero).isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el enfermero " + idEnfermero + ".");
        }
        Habitacion habitacion = buscarHabitacion(normalizado);
        if (habitacion.tieneEnfermero()) {
            throw new OperacionNoPermitidaException("La habitación " + normalizado + " ya tiene un enfermero asignado.");
        }
        habitacionDAO.actualizar(habitacion.conEnfermero(idEnfermero));
    }

    public void quitar(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        Habitacion habitacion = buscarHabitacion(normalizado);
        if (!habitacion.tieneEnfermero()) {
            throw new EntidadNoEncontradaException("La habitación " + normalizado + " no tiene enfermero asignado.");
        }
        habitacionDAO.actualizar(habitacion.conEnfermero(null));
    }

    /** Habitaciones que tienen enfermero, cada una con el suyo. */
    public List<PersonaHabitacionDTO> listar() {
        List<PersonaHabitacionDTO> resultado = new ArrayList<>();
        for (Habitacion h : habitacionDAO.listarTodos()) {
            if (h.tieneEnfermero()) {
                Optional<Enfermero> enfermero = enfermeroDAO.buscarPorId(h.getIdEnfermero());
                if (enfermero.isPresent()) {
                    resultado.add(new PersonaHabitacionDTO(PersonaDTO.desde(enfermero.get()), h.getCodigo()));
                }
            }
        }
        return resultado;
    }

    /** Códigos de las habitaciones que todavía no tienen enfermero. */
    public List<String> habitacionesSinEnfermero() {
        List<String> resultado = new ArrayList<>();
        for (Habitacion h : habitacionDAO.listarTodos()) {
            if (!h.tieneEnfermero()) {
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
}
