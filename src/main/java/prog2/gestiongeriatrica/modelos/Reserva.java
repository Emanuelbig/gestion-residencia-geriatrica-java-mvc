package prog2.gestiongeriatrica.modelos;

import java.util.Objects;

/**
 * Indica qué residente ocupa una habitación. Cada habitación tiene como máximo una.
 * @author Emanuel
 */
public class Reserva {
    
    private final int idResidente;
    private final String codigoHabitacion;

    public Reserva(int idResidente, String codigoHabitacion) {
        this.idResidente = idResidente;
        this.codigoHabitacion = codigoHabitacion;
    }

    public int getIdResidente() {
        return idResidente;
    }

    public String getCodigoHabitacion() {
        return codigoHabitacion;
    }

    @Override
    public String toString() {
        return "Reserva con id del Residente: " + idResidente + " y codigo de habitacion: " + codigoHabitacion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Reserva other = (Reserva) obj;
        return Objects.equals(this.codigoHabitacion, other.codigoHabitacion);
    }

    
    
    
}
