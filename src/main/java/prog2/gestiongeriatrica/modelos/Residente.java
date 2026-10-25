package prog2.gestiongeriatrica.modelos;

/**
 * Persona que vive en la residencia y puede ocupar una habitación.
 * @author Emanuel
 */
public class Residente extends Persona {

    public Residente(int id, String dni, String nombre, String apellido) {
        super(id, dni, nombre, apellido);
    }
    
    @Override
    public String getRol(){
        return "Residente";
    } 
    
}
