package prog2.gestiongeriatrica.dao;

/**
 * Guarda y lee el último número usado de una secuencia (ids de personas, códigos de habitación).
 * @author Emanuel
 */
public interface SecuenciaDAO {
    int leerUltimo();
    void guardarUltimo(int numero);
}
