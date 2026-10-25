package prog2.gestiongeriatrica.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import prog2.gestiongeriatrica.excepciones.PersistenciaException;

/**
 * Guarda en un archivo el último número usado, para no repetirlo aunque se borre el registro.
 * @author Emanuel
 */
public class SecuenciaArchivoDAO implements SecuenciaDAO{
    private final Path archivo;

    public SecuenciaArchivoDAO(Path archivo) {
        this.archivo = archivo;
    }

    @Override
    public int leerUltimo() {
        if (!Files.exists(archivo)) {
            return 0;                                   // todavía no se usó ninguno
        }
        try {
            return Integer.parseInt(Files.readString(archivo).trim());
        } catch (IOException | NumberFormatException e) {
            throw new PersistenciaException("No se pudo leer " + archivo, e);
        }
    }

    @Override
    public void guardarUltimo(int numero) {
        try {
            Path carpeta = archivo.getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            Files.writeString(archivo, String.valueOf(numero));
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo guardar " + archivo, e);
        }
    }
}
