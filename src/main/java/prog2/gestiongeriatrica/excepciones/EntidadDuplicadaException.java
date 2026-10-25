package prog2.gestiongeriatrica.excepciones;

/**
 * Se lanza cuando se intenta registrar algo que ya existe (por ejemplo, un DNI repetido).
 * @author Emanuel
 */
public class EntidadDuplicadaException extends GeriatricoException {

    public EntidadDuplicadaException(String message) {
        super(message);
    }
    
}
