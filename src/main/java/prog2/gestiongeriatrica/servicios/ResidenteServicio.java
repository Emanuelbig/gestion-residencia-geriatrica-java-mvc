package prog2.gestiongeriatrica.servicios;

import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dao.SecuenciaDAO;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Habitacion;
import prog2.gestiongeriatrica.modelos.Residente;

/**
 * Reglas propias de los residentes. Lo común a toda persona está en PersonaServicio.
 * @author Emanuel
 */
public class ResidenteServicio extends PersonaServicio<Residente> {

    private static final int PRIMER_ID = 1001;

    private final GenericDAO<Habitacion, String> habitacionDAO;   // para no borrar un residente con habitación

    public ResidenteServicio(GenericDAO<Residente, Integer> residenteDAO,
                             GenericDAO<Enfermero, Integer> enfermeroDAO,
                             GenericDAO<Habitacion, String> habitacionDAO,
                             SecuenciaDAO secuencia) {
        super(residenteDAO, enfermeroDAO, secuencia, PRIMER_ID);
        this.habitacionDAO = habitacionDAO;
    }

    @Override
    protected Residente crearPersona(int id, String dni, String nombre, String apellido) {
        return new Residente(id, dni, nombre, apellido);
    }

    @Override
    protected boolean estaEnUso(int id) {
        for (Habitacion h : habitacionDAO.listarTodos()) {
            if (h.estaOcupadaPor(id)) {
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
