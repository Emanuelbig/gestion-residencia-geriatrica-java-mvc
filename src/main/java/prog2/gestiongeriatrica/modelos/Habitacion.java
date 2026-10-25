package prog2.gestiongeriatrica.modelos;

import java.util.Objects;

/**
 * Habitación de la residencia, identificada por su código. Si está ocupada no se guarda acá: se calcula a partir de las reservas.
 * @author Emanuel
 */
public class Habitacion {
    
    private final String codigo;

    public Habitacion(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
    
    public static String normalizarCodigo(String codigo) {
        return codigo == null ? "" : codigo.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return "Habitacion con codigo: " + codigo;
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
