package prog2.gestiongeriatrica.modelos;

/**
 * Datos comunes de toda persona del sistema. Cada subclase define su rol.
 *
 * @author Emanuel Morano Sole
 */
public abstract class Persona {
    //atributos

    //se asigna una sola vez al constructor y no se puede cambiar nunca
    //no cambia por que las reservas siempre apuntan a el 
    private final int id;
    private String dni;
    private String nombre;
    private String apellido;

    //constructores 
    //solo pueden usar este constructor las clases hijas
    protected Persona(int id, String dni, String nombre, String apellido) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    //getters
    public int getId() {
        return id;
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

    //Otros metodos
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Saca espacios de más y pone mayúscula inicial: " morano sole" → "Morano
     * Sole".
     */
    public static String normalizarNombre(String texto) {
        if (texto == null) {
            return null;
        }
        StringBuilder resultado = new StringBuilder();
        for (String palabra : texto.trim().toLowerCase().split(" ")) {
            if (!palabra.isEmpty()) {
                if (resultado.length() > 0) {
                    resultado.append(" ");
                }
                resultado.append(Character.toUpperCase(palabra.charAt(0)));
                resultado.append(palabra.substring(1));
            }
        }
        return resultado.toString();
    }

    //abstracto
    public abstract String getRol();

    @Override
    public String toString() {
        return getRol() + " con id: " + id + ", dni: " + dni + ", nombre: " + nombre + ", apellido: " + apellido + '.';
    }

    // equals y hashCode definen cuándo dos objetos son iguales; las colecciones usan eso 
    //para buscar, borrar o evitar duplicados
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.id;
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
        final Persona other = (Persona) obj;
        return this.id == other.id;
    }

}
