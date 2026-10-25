package prog2.gestiongeriatrica.excepciones;

/**
 * Base de todos los errores del sistema. La vista la atrapa para mostrar el mensaje al usuario.
 * @author Emanuel
 */
public abstract class GeriatricoException extends RuntimeException {

    protected GeriatricoException(String message) {
        super(message);
    }

    protected GeriatricoException(String message, Throwable cause) {
        super(message, cause);
    }

    
}
