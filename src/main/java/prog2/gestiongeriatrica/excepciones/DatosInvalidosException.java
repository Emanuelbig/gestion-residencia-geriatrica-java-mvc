package prog2.gestiongeriatrica.excepciones;

/**
 * Se lanza cuando un dato ingresado no tiene el formato correcto (por ejemplo, un DNI con letras).
 * @author Emanuel
 */
public class DatosInvalidosException extends GeriatricoException {

    public DatosInvalidosException(String message) {
        super(message);
    }
    
}
