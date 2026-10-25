package prog2.gestiongeriatrica.dto;

/**
 * Datos de una persona (residente o enfermero) que viajan entre la vista y el service. El id es null en el alta, porque lo genera el sistema.
 * @author Emanuel
 */
public class PersonaDTO {
    
    public static final String PATRON_DNI = "\\d{7,8}";
    public static final String PATRON_NOMBRE = "[\\p{L} ]{2,40}";

    private final String dni;
    private final String nombre;
    private final String apellido;
    
    private final Integer id;

    public PersonaDTO(String dni, String nombre, String apellido, Integer id) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
    }

    

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Integer getId() {
        return id;
    }
    
    
    
    
}
