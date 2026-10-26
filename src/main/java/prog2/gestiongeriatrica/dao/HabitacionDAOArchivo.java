package prog2.gestiongeriatrica.dao;

import java.nio.file.Path;
import prog2.gestiongeriatrica.modelos.Habitacion;

/**
 * Guarda las habitaciones en un archivo de texto, una línea por habitación: codigo,idResidente,idEnfermero.
 * Un id vacío significa que no hay: "HAB-104,," es una habitación libre y sin enfermero.
 * @author Emanuel
 */
public class HabitacionDAOArchivo extends ArchivoDAO<Habitacion, String> {

    public HabitacionDAOArchivo(Path archivo) {
        super(archivo);
    }

    @Override
    protected String aLinea(Habitacion habitacion) {
        return habitacion.getCodigo() + "," + aTexto(habitacion.getIdResidente()) + "," + aTexto(habitacion.getIdEnfermero());
    }

    @Override
    protected Habitacion desdeLinea(String linea) {
        String[] partes = linea.split(",", -1);        // el -1 conserva los campos vacíos del final
        if (partes.length != 3) {
            throw new IllegalArgumentException("Se esperaban 3 campos");
        }
        return new Habitacion(partes[0].trim(), aNumero(partes[1]), aNumero(partes[2]));
    }

    @Override
    protected String idDe(Habitacion habitacion) {
        return habitacion.getCodigo();
    }

    private String aTexto(Integer id) {
        return id == null ? "" : String.valueOf(id);
    }

    private Integer aNumero(String texto) {
        return texto.isBlank() ? null : Integer.valueOf(texto.trim());
    }
}
