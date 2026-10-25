package prog2.gestiongeriatrica.dao;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos: qué operaciones se pueden hacer
 * sobre una entidad, sin decir dónde se guarda (archivo o base de datos).
 * Los services dependen de esta interfaz y no de una clase concreta.
 * @author Emanuel Morano Sole
 * @param <T> tipo de entidad que se guarda (Residente, Habitacion etc)
 * @param <ID> tipo de identificador , int para personas, string para habitaciones
 */
public interface GenericDAO <T, ID>{
    
    //guarda una nueva entidad
    void crear(T entity);
    
    //devuelve todas las entidades guardadas
    List<T> listarTodos();
    
    //busca una entidad por su identificador
    Optional<T> buscarPorId(ID id);
    
    //reemplaza la entidad guardada por una nueva con mismo identificador
    void actualizar(T entity);
    
    //elimina identidad que tenga el identificador pasado.
    void eliminar (ID id);
}
