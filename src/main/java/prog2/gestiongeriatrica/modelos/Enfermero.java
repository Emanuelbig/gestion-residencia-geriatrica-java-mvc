package prog2.gestiongeriatrica.modelos;

/**
 * Persona del personal que atiende una o varias habitaciones.
 * @author Emanuel
 */
public class Enfermero extends Persona {

    public Enfermero(int id, String dni, String nombre, String apellido) {
        super(id, dni, nombre, apellido);
    }
    
    @Override
    public String getRol(){
        return "Enfermero";
    } 
}
