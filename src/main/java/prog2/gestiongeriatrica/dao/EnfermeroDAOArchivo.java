package prog2.gestiongeriatrica.dao;

import java.nio.file.Path;
import prog2.gestiongeriatrica.modelos.Enfermero;

/**
 * Guarda los enfermeros en un archivo de texto, una línea por residente:
 * id,dni,nombre,apellido
 * @author Emanuel
 */
public class EnfermeroDAOArchivo extends ArchivoDAO<Enfermero, Integer> {

    public EnfermeroDAOArchivo(Path archivo) {
        super(archivo);
    }
    
    @Override
    protected String aLinea(Enfermero enfermero) {
        return enfermero.getId() + "," + enfermero.getDni() + ","
                + enfermero.getNombre() + "," + enfermero.getApellido();
    }

    @Override
    protected Enfermero desdeLinea(String linea) {
        String[] partes = linea.split(",");
        if (partes.length != 4) {
            throw new IllegalArgumentException("Se esperaban 4 campos");
        }
        return new Enfermero(Integer.parseInt(partes[0]), partes[1], partes[2], partes[3]);
    }

    @Override
    protected Integer idDe(Enfermero enfermero) {
        return enfermero.getId();
    }
}
