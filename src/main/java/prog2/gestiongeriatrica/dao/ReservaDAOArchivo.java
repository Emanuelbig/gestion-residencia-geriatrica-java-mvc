package prog2.gestiongeriatrica.dao;

import java.nio.file.Path;
import prog2.gestiongeriatrica.modelos.Reserva;

/**
 * Guarda las reservas en un archivo de texto, una línea por reserva: idResidente,codigoHabitacion.
 * @author Emanuel
 */
public class ReservaDAOArchivo extends ArchivoDAO<Reserva, String>{

    public ReservaDAOArchivo(Path archivo) {
        super(archivo);
    }
    
        @Override
    protected String aLinea(Reserva reserva) {
        return reserva.getIdResidente() + "," + reserva.getCodigoHabitacion();
    }

    @Override
    protected Reserva desdeLinea(String linea) {
        String[] partes = linea.split(",");
        if (partes.length != 2) {
            throw new IllegalArgumentException("Se esperaban 2 campos");
        }
        return new Reserva(Integer.parseInt(partes[0]), partes[1]);
    }

    @Override
    protected String idDe(Reserva reserva) {
        return reserva.getCodigoHabitacion();
    }
}
