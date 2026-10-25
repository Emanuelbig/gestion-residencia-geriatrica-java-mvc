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
import prog2.gestiongeriatrica.modelos.Asignacion;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Habitacion;
import prog2.gestiongeriatrica.modelos.Reserva;
import prog2.gestiongeriatrica.modelos.Residente;

/**
 * Reglas de negocio de las habitaciones: alta, listado, consulta y baja. Si una
 * habitación está ocupada no se guarda: se calcula a partir de las reservas.
 *
 * @author Emanuel
 */
public class HabitacionServicio {

    private static final String PREFIJO = "HAB-";
    private static final int PRIMER_NUMERO = 101;

    private final GenericDAO<Habitacion, String> habitacionDAO;
    private final GenericDAO<Reserva, String> reservaDAO;
    private final GenericDAO<Asignacion, String> asignacionDAO;
    private final GenericDAO<Residente, Integer> residenteDAO;   // para mostrar quién la ocupa
    private final GenericDAO<Enfermero, Integer> enfermeroDAO;   // para mostrar quién la atiende
    private final SecuenciaDAO secuencia;   // último número usado, para no repetir códigos

    public HabitacionServicio(GenericDAO<Habitacion, String> habitacionDAO,
                              GenericDAO<Reserva, String> reservaDAO,
                              GenericDAO<Asignacion, String> asignacionDAO,
                              GenericDAO<Residente, Integer> residenteDAO,
                              GenericDAO<Enfermero, Integer> enfermeroDAO,
                              SecuenciaDAO secuencia) {
        this.habitacionDAO = habitacionDAO;
        this.reservaDAO = reservaDAO;
        this.asignacionDAO = asignacionDAO;
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
            boolean ocupada = reservaDAO.buscarPorId(h.getCodigo()).isPresent();
            resultado.add(new HabitacionDTO(h.getCodigo(), ocupada));
        }
        return resultado;
    }

    /** Detalle de una habitación: quién la ocupa y qué enfermero la atiende. */
    public HabitacionDetalleDTO obtener(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        verificarQueExiste(normalizado);
        PersonaDTO residente = null;                   // quedan null si no hay
        PersonaDTO enfermero = null;
        Optional<Reserva> reserva = reservaDAO.buscarPorId(normalizado);
        if (reserva.isPresent()) {
            Optional<Residente> r = residenteDAO.buscarPorId(reserva.get().getIdResidente());
            if (r.isPresent()) {
                residente = PersonaDTO.desde(r.get());
            }
        }
        Optional<Asignacion> asignacion = asignacionDAO.buscarPorId(normalizado);
        if (asignacion.isPresent()) {
            Optional<Enfermero> e = enfermeroDAO.buscarPorId(asignacion.get().getIdEnfermero());
            if (e.isPresent()) {
                enfermero = PersonaDTO.desde(e.get());
            }
        }
        return new HabitacionDetalleDTO(normalizado, residente, enfermero);
    }

    public void eliminar(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        verificarQueExiste(normalizado);
        if (reservaDAO.buscarPorId(normalizado).isPresent()) {
            throw new OperacionNoPermitidaException("La habitación está ocupada. Liberala primero.");
        }
        if (asignacionDAO.buscarPorId(normalizado).isPresent()) {
            throw new OperacionNoPermitidaException("La habitación tiene un enfermero asignado. Quitalo primero.");
        }
        habitacionDAO.eliminar(normalizado);
    }

    private void verificarQueExiste(String codigo) {
        if (habitacionDAO.buscarPorId(codigo).isEmpty()) {
            throw new EntidadNoEncontradaException("No existe la habitación " + codigo + ".");
        }
    }

}
