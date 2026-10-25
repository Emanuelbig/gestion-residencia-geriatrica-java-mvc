package prog2.gestiongeriatrica.excepciones;

/**
 * Se lanza cuando se busca un id o código que no existe.
 * @author Emanuel
 */
public class EntidadNoEncontradaException extends GeriatricoException {

    public EntidadNoEncontradaException(String message) {
        super(message);
    }
    
}
