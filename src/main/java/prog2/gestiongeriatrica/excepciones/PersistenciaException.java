package prog2.gestiongeriatrica.excepciones;

/**
 * Se lanza cuando falla la lectura o escritura de los archivos de datos.
 * @author Emanuel
 */
public class PersistenciaException extends GeriatricoException {

    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
