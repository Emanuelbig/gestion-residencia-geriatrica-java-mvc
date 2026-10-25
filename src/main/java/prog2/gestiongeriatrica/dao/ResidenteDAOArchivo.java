package prog2.gestiongeriatrica.dao;

import java.nio.file.Path;
import prog2.gestiongeriatrica.modelos.Residente;

/**
 * Guarda los residentes en un archivo de texto, una línea por residente:
 * id,dni,nombre,apellido
 * @author Emanuel
 */
public class ResidenteDAOArchivo extends ArchivoDAO<Residente, Integer> {

    public ResidenteDAOArchivo(Path archivo) {
        super(archivo);
    }
    
    @Override
    protected String aLinea(Residente residente) {
        return residente.getId() + "," + residente.getDni() + ","
                + residente.getNombre() + "," + residente.getApellido();
    }

    @Override
    protected Residente desdeLinea(String linea) {
        String[] partes = linea.split(",");
        if (partes.length != 4) {
            throw new IllegalArgumentException("Se esperaban 4 campos");
        }
        return new Residente(Integer.parseInt(partes[0]), partes[1], partes[2], partes[3]);
    }

    @Override
    protected Integer idDe(Residente residente) {
        return residente.getId();
    }
}
