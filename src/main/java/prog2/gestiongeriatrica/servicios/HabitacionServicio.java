package prog2.gestiongeriatrica.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dao.SecuenciaDAO;
import prog2.gestiongeriatrica.dto.HabitacionDTO;
import prog2.gestiongeriatrica.dto.HabitacionDetalleDTO;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.excepciones.EntidadNoEncontradaException;
import prog2.gestiongeriatrica.excepciones.OperacionNoPermitidaException;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Habitacion;
import prog2.gestiongeriatrica.modelos.Residente;

/**
 * Reglas de negocio de las habitaciones: alta, listado, consulta y baja.
 * La habitación está ocupada cuando tiene un residente guardado (idResidente).
 *
 * @author Emanuel
 */
public class HabitacionServicio {

    private static final String PREFIJO = "HAB-";
    private static final int PRIMER_NUMERO = 101;

    private final GenericDAO<Habitacion, String> habitacionDAO;
    private final GenericDAO<Residente, Integer> residenteDAO;   // para mostrar quién la ocupa
    private final GenericDAO<Enfermero, Integer> enfermeroDAO;   // para mostrar quién la atiende
    private final SecuenciaDAO secuencia;   // último número usado, para no repetir códigos

    public HabitacionServicio(GenericDAO<Habitacion, String> habitacionDAO,
                              GenericDAO<Residente, Integer> residenteDAO,
                              GenericDAO<Enfermero, Integer> enfermeroDAO,
                              SecuenciaDAO secuencia) {
        this.habitacionDAO = habitacionDAO;
        this.residenteDAO = residenteDAO;
        this.enfermeroDAO = enfermeroDAO;
        this.secuencia = secuencia;
    }

    /** Da de alta una habitación con el siguiente código libre y lo devuelve. */
    public String crear() {
        int numero = generarNumero();
        String codigo = PREFIJO + numero;
        habitacionDAO.crear(new Habitacion(codigo));
        secuencia.guardarUltimo(numero);         // recién ahora el número queda "gastado"
        return codigo;
    }

    // Busca el número más alto usado (aunque se haya borrado) y le suma 1: HAB-105 → 106
    private int generarNumero() {
        int mayor = Math.max(PRIMER_NUMERO - 1, secuencia.leerUltimo());
        for (Habitacion h : habitacionDAO.listarTodos()) {
            int numero = Integer.parseInt(h.getCodigo().substring(PREFIJO.length()));
            if (numero > mayor) {
                mayor = numero;
            }
        }
        return mayor + 1;
    }

    public List<HabitacionDTO> listar() {
        List<HabitacionDTO> resultado = new ArrayList<>();
        for (Habitacion h : habitacionDAO.listarTodos()) {
            resultado.add(new HabitacionDTO(h.getCodigo(), h.isOcupada()));
        }
        return resultado;
    }

    /** Detalle de una habitación: quién la ocupa y qué enfermero la atiende. */
    public HabitacionDetalleDTO obtener(String codigo) {
        Habitacion habitacion = buscar(Habitacion.normalizarCodigo(codigo));
        PersonaDTO residente = null;                   // quedan null si no hay
        PersonaDTO enfermero = null;
        if (habitacion.isOcupada()) {
            Optional<Residente> r = residenteDAO.buscarPorId(habitacion.getIdResidente());
            if (r.isPresent()) {
                residente = PersonaDTO.desde(r.get());
            }
        }
        if (habitacion.tieneEnfermero()) {
            Optional<Enfermero> e = enfermeroDAO.buscarPorId(habitacion.getIdEnfermero());
            if (e.isPresent()) {
                enfermero = PersonaDTO.desde(e.get());
            }
        }
        return new HabitacionDetalleDTO(habitacion.getCodigo(), residente, enfermero);
    }

    public void eliminar(String codigo) {
        Habitacion habitacion = buscar(Habitacion.normalizarCodigo(codigo));
        if (habitacion.isOcupada()) {
            throw new OperacionNoPermitidaException("La habitación está ocupada. Liberala primero.");
        }
        if (habitacion.tieneEnfermero()) {
            throw new OperacionNoPermitidaException("La habitación tiene un enfermero asignado. Quitalo primero.");
        }
        habitacionDAO.eliminar(habitacion.getCodigo());
    }

    private Habitacion buscar(String codigo) {
        Optional<Habitacion> habitacion = habitacionDAO.buscarPorId(codigo);
        if (habitacion.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe la habitación " + codigo + ".");
        }
        return habitacion.get();
    }
}
