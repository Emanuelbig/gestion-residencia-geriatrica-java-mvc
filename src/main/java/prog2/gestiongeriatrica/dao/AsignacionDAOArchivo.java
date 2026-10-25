package prog2.gestiongeriatrica.dao;

import java.nio.file.Path;
import prog2.gestiongeriatrica.modelos.Asignacion;

/**
 * Guarda las asignaciones en un archivo de texto, una línea por asignación: idEnfermero,codigoHabitacion.
 * @author Emanuel
 */
public class AsignacionDAOArchivo extends ArchivoDAO<Asignacion, String>{

    public AsignacionDAOArchivo(Path archivo) {
        super(archivo);
    }
    
    @Override
    protected String aLinea(Asignacion asignacion) {
        return asignacion.getIdEnfermero() + "," + asignacion.getCodigoHabitacion();
    }

    @Override
    protected Asignacion desdeLinea(String linea) {
        String[] partes = linea.split(",");
        if (partes.length != 2) {
            throw new IllegalArgumentException("Se esperaban 2 campos");
        }
        return new Asignacion(Integer.parseInt(partes[0]), partes[1]);
    }

    @Override
    protected String idDe(Asignacion asignacion) {
        return asignacion.getCodigoHabitacion();
    }
}
