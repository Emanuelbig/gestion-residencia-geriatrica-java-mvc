package prog2.gestiongeriatrica.servicios;

import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Reserva;
import prog2.gestiongeriatrica.modelos.Residente;

/**
 * Reglas propias de los residentes. Lo común a toda persona está en PersonaServicio.
 * @author Emanuel
 */
public class ResidenteServicio extends PersonaServicio<Residente> {

    private static final int PRIMER_ID = 1001;

    private final GenericDAO<Reserva, String> reservaDAO;   // para no borrar un residente con habitación

    public ResidenteServicio(GenericDAO<Residente, Integer> residenteDAO,
                             GenericDAO<Enfermero, Integer> enfermeroDAO,
                             GenericDAO<Reserva, String> reservaDAO) {
        super(residenteDAO, enfermeroDAO, PRIMER_ID);
        this.reservaDAO = reservaDAO;
    }

    @Override
    protected Residente crearPersona(int id, String dni, String nombre, String apellido) {
        return new Residente(id, dni, nombre, apellido);
    }

    @Override
    protected boolean estaEnUso(int id) {
        for (Reserva r : reservaDAO.listarTodos()) {
            if (r.getIdResidente() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String mensajeEnUso(int id) {
        return "El residente " + id + " tiene una habitación asignada. Liberala primero.";
    }

    @Override
    protected String nombreEntidad() {
        return "residente";
    }
}
