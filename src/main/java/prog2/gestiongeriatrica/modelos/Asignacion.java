package prog2.gestiongeriatrica.modelos;

import java.util.Objects;

/**
 * Indica qué enfermero atiende una habitación. Cada habitación tiene como máximo un enfermero.
 * @author Emanuel
 */
public class Asignacion {
    
    private final int idEnfermero;
    private final String codigoHabitacion;

    public Asignacion(int idEnfermero, String codigoHabitacion) {
        this.idEnfermero = idEnfermero;
        this.codigoHabitacion = codigoHabitacion;
    }

    public int getIdEnfermero() {
        return idEnfermero;
    }

    public String getCodigoHabitacion() {
        return codigoHabitacion;
    }

    @Override
    public String toString() {
        return "Asignacion con id del enfermero: " + idEnfermero + " y codigo de habitacion: " + codigoHabitacion;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.codigoHabitacion);
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
        final Asignacion other = (Asignacion) obj;
        return Objects.equals(this.codigoHabitacion, other.codigoHabitacion);
    }
        
}
