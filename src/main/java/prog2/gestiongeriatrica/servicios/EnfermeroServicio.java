package prog2.gestiongeriatrica.servicios;

import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.modelos.Asignacion;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Residente;

/**
 * Reglas propias de los enfermeros. Lo común a toda persona está en PersonaServicio.
 * @author Emanuel
 */
public class EnfermeroServicio extends PersonaServicio<Enfermero> {

    private static final int PRIMER_ID = 2001;

    private final GenericDAO<Asignacion, String> asignacionDAO;   // para no borrar un enfermero con habitaciones

    public EnfermeroServicio(GenericDAO<Enfermero, Integer> enfermeroDAO,
                             GenericDAO<Residente, Integer> residenteDAO,
                             GenericDAO<Asignacion, String> asignacionDAO) {
        super(enfermeroDAO, residenteDAO, PRIMER_ID);
        this.asignacionDAO = asignacionDAO;
    }

    @Override
    protected Enfermero crearPersona(int id, String dni, String nombre, String apellido) {
        return new Enfermero(id, dni, nombre, apellido);
    }

    @Override
    protected boolean estaEnUso(int id) {
        for (Asignacion a : asignacionDAO.listarTodos()) {
            if (a.getIdEnfermero() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String mensajeEnUso(int id) {
        return "El enfermero " + id + " atiende habitaciones. Quitale las asignaciones primero.";
    }

    @Override
    protected String nombreEntidad() {
        return "enfermero";
    }
}