package prog2.gestiongeriatrica.servicios;

import prog2.gestiongeriatrica.dao.GenericDAO;
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
}
