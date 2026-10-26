package prog2.gestiongeriatrica.modelos;

import java.util.Objects;

/**
 * Habitación de la residencia. Guarda qué residente la ocupa y qué enfermero la atiende (null si no hay),
 * así las reservas y las asignaciones no necesitan un archivo propio.
 * @author Emanuel
 */
public class Habitacion {

    private final String codigo;
    private final Integer idResidente;   // null = libre
    private final Integer idEnfermero;   // null = sin enfermero

    public Habitacion(String codigo) {
        this(codigo, null, null);          // una habitación nueva está libre y sin enfermero
    }

    public Habitacion(String codigo, Integer idResidente, Integer idEnfermero) {
        this.codigo = codigo;
        this.idResidente = idResidente;
        this.idEnfermero = idEnfermero;
    }

    public String getCodigo() {
        return codigo;
    }

    public Integer getIdResidente() {
        return idResidente;
    }

    public Integer getIdEnfermero() {
        return idEnfermero;
    }

    public boolean isOcupada() {
        return idResidente != null;
    }

    public boolean tieneEnfermero() {
        return idEnfermero != null;
    }

    public boolean estaOcupadaPor(int idResidente) {
        return this.idResidente != null && this.idResidente == idResidente;
    }

    public boolean esAtendidaPor(int idEnfermero) {
        return this.idEnfermero != null && this.idEnfermero == idEnfermero;
    }

    // La habitación no cambia: reservar o asignar devuelve una copia con el dato nuevo, y el DAO la reemplaza.
    /** Copia de esta habitación con otro residente (null para liberarla). */
    public Habitacion conResidente(Integer idResidente) {
        return new Habitacion(codigo, idResidente, idEnfermero);
    }

    /** Copia de esta habitación con otro enfermero (null para quitarlo). */
    public Habitacion conEnfermero(Integer idEnfermero) {
        return new Habitacion(codigo, idResidente, idEnfermero);
    }

    public static String normalizarCodigo(String codigo) {
        return codigo == null ? "" : codigo.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return "Habitacion con codigo: " + codigo + ", residente: " + idResidente + ", enfermero: " + idEnfermero;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.codigo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Habitacion other = (Habitacion) obj;
        return Objects.equals(this.codigo, other.codigo);
    }
}
