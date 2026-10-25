package prog2.gestiongeriatrica.servicios;

import java.util.ArrayList;
import java.util.List;
import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dto.HabitacionDTO;
import prog2.gestiongeriatrica.excepciones.DatosInvalidosException;
import prog2.gestiongeriatrica.excepciones.EntidadDuplicadaException;
import prog2.gestiongeriatrica.excepciones.EntidadNoEncontradaException;
import prog2.gestiongeriatrica.excepciones.OperacionNoPermitidaException;
import prog2.gestiongeriatrica.modelos.Asignacion;
import prog2.gestiongeriatrica.modelos.Habitacion;
import prog2.gestiongeriatrica.modelos.Reserva;

/**
 * Reglas de negocio de las habitaciones: alta, listado y baja. Si una
 * habitación está ocupada no se guarda: se calcula a partir de las reservas.
 *
 * @author Emanuel
 */
public class HabitacionServicio {

    private final GenericDAO<Habitacion, String> habitacionDAO;
    private final GenericDAO<Reserva, String> reservaDAO;
    private final GenericDAO<Asignacion, String> asignacionDAO;

    public HabitacionServicio(GenericDAO<Habitacion, String> habitacionDAO, GenericDAO<Reserva, String> reservaDAO, GenericDAO<Asignacion, String> asignacionDAO) {
        this.habitacionDAO = habitacionDAO;
        this.reservaDAO = reservaDAO;
        this.asignacionDAO = asignacionDAO;
    }

    public void crear(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (!normalizado.matches(HabitacionDTO.PATRON_CODIGO)) {
            throw new DatosInvalidosException("El código debe tener el formato HAB-101.");
        }
        if (habitacionDAO.buscarPorId(normalizado).isPresent()) {
            throw new EntidadDuplicadaException("Ya existe la habitación " + normalizado + ".");
        }
        habitacionDAO.crear(new Habitacion(normalizado));
    }

    public List<HabitacionDTO> listar() {
        List<HabitacionDTO> resultado = new ArrayList<>();
        for (Habitacion h : habitacionDAO.listarTodos()) {
            boolean ocupada = reservaDAO.buscarPorId(h.getCodigo()).isPresent();
            resultado.add(new HabitacionDTO(h.getCodigo(), ocupada));
        }
        return resultado;
    }

    public void eliminar(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (habitacionDAO.buscarPorId(normalizado).isEmpty()) {
            throw new EntidadNoEncontradaException("No existe la habitación " + normalizado + ".");
        }
        if (reservaDAO.buscarPorId(normalizado).isPresent()) {
            throw new OperacionNoPermitidaException("La habitación está ocupada. Liberala primero.");
        }
        if (asignacionDAO.buscarPorId(normalizado).isPresent()) {
            throw new OperacionNoPermitidaException("La habitación tiene un enfermero asignado. Quitalo primero.");
        }
        habitacionDAO.eliminar(normalizado);
    }

}
