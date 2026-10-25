package prog2.gestiongeriatrica.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.dto.PersonaHabitacionDTO;
import prog2.gestiongeriatrica.excepciones.EntidadNoEncontradaException;
import prog2.gestiongeriatrica.excepciones.OperacionNoPermitidaException;
import prog2.gestiongeriatrica.modelos.Asignacion;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Habitacion;

/**
 * Reglas de las asignaciones: un enfermero puede atender varias habitaciones,
 * pero cada habitación tiene como máximo un enfermero.
 * @author Emanuel
 */
public class AsignacionServicio {
    
    private final GenericDAO<Asignacion, String> asignacionDAO;
    private final GenericDAO<Enfermero, Integer> enfermeroDAO;
    private final GenericDAO<Habitacion, String> habitacionDAO;

    public AsignacionServicio(GenericDAO<Asignacion, String> asignacionDAO,
                          GenericDAO<Enfermero, Integer> enfermeroDAO,
                          GenericDAO<Habitacion, String> habitacionDAO) {
        this.asignacionDAO = asignacionDAO;
        this.enfermeroDAO = enfermeroDAO;
        this.habitacionDAO = habitacionDAO;
    }
    
     public void asignar(int idEnfermero, String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (enfermeroDAO.buscarPorId(idEnfermero).isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el enfermero " + idEnfermero + ".");
        }
        if (habitacionDAO.buscarPorId(normalizado).isEmpty()) {
            throw new EntidadNoEncontradaException("No existe la habitación " + normalizado + ".");
        }
        if (asignacionDAO.buscarPorId(normalizado).isPresent()) {
            throw new OperacionNoPermitidaException("La habitación " + normalizado + " ya tiene un enfermero asignado.");
        }
        asignacionDAO.crear(new Asignacion(idEnfermero, normalizado));
    }

    public void quitar(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (asignacionDAO.buscarPorId(normalizado).isEmpty()) {
            throw new EntidadNoEncontradaException("La habitación " + normalizado + " no tiene enfermero asignado.");
        }
        asignacionDAO.eliminar(normalizado);
    }

    /** Habitaciones que tienen enfermero, cada una con el suyo. */
    public List<PersonaHabitacionDTO> listar() {
        List<PersonaHabitacionDTO> resultado = new ArrayList<>();
        for (Asignacion a : asignacionDAO.listarTodos()) {
            Optional<Enfermero> enfermero = enfermeroDAO.buscarPorId(a.getIdEnfermero());
            if (enfermero.isPresent()) {
                resultado.add(new PersonaHabitacionDTO(PersonaDTO.desde(enfermero.get()), a.getCodigoHabitacion()));
            }
        }
        return resultado;
    }

    /** Códigos de las habitaciones que todavía no tienen enfermero. */
    public List<String> habitacionesSinEnfermero() {
        List<String> resultado = new ArrayList<>();
        for (Habitacion h : habitacionDAO.listarTodos()) {
            if (asignacionDAO.buscarPorId(h.getCodigo()).isEmpty()) {
                resultado.add(h.getCodigo());
            }
        }
        return resultado;
    }
}
