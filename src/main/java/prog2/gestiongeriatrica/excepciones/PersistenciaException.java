package prog2.gestiongeriatrica.excepciones;

/**
 * Se lanza cuando falla la lectura o escritura de los archivos de datos.
 * @author Emanuel
 */
public class PersistenciaException extends GeriatricoException {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
