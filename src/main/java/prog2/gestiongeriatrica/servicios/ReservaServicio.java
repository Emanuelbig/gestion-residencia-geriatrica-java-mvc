package prog2.gestiongeriatrica.servicios;

import prog2.gestiongeriatrica.dao.GenericDAO;
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

    private boolean tieneReserva(int idResidente) {
        for (Reserva r : reservaDAO.listarTodos()) {
            if (r.getIdResidente() == idResidente) {
                return true;
            }
        }
        return false;
    }
}
