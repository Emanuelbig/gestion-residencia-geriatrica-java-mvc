package prog2.gestiongeriatrica.dao;

import java.nio.file.Path;
import prog2.gestiongeriatrica.modelos.Habitacion;

/**
 * Guarda las habitaciones en un archivo de texto, una línea por habitación con su código.
 * @author Emanuel
 */
public class HabitacionDAOArchivo extends ArchivoDAO<Habitacion, String> {

    public HabitacionDAOArchivo(Path archivo) {
        super(archivo);
    }
    
    @Override
    protected String aLinea(Habitacion habitacion) {
        return habitacion.getCodigo();
    }

    @Override
    protected Habitacion desdeLinea(String linea) {
        if (linea.contains(",")) {
            throw new IllegalArgumentException("Una habitacion tiene un solo campo");
        }
        return new Habitacion(linea.trim());
    }

    @Override
    protected String idDe(Habitacion habitacion) {
        return habitacion.getCodigo();
    }
}
