package prog2.gestiongeriatrica.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import prog2.gestiongeriatrica.excepciones.EntidadNoEncontradaException;
import prog2.gestiongeriatrica.excepciones.PersistenciaException;

/**
 * Base de todos los DAO que guardan en archivos de texto.
 * Tiene escrito una sola vez el código común (leer, guardar, buscar, borrar);
 * cada DAO concreto solo dice cómo convertir su entidad a una línea y al revés.
 * 
 * Esta clase tiene atributos, cosntructor y los metodos abstractos
 * Lee el archivo
 * Guarda el archivo
 * Los 5 metodos de la interfaz
 * 
 * @author Emanuel
 * @param <T> tipo de entidad que guarda
 * @param <ID> tipo de identificador
 */
public abstract class ArchivoDAO<T, ID> implements GenericDAO<T, ID> {
    
    //ruta del .txt
    private final Path archivo; 
    
    //copia en memoria de lo que hay en el archivo
    private final List<T> entidades;

    protected ArchivoDAO(Path archivo) {
        this.archivo = archivo;
        this.entidades = leerArchivo();
    }
    
    protected abstract String aLinea(T entity);
    
    protected abstract T desdeLinea(String linea);
    
    protected abstract ID idDe(T entity);
    
    private List<T> leerArchivo(){
        
        List<T> resultado = new ArrayList<>();
        
        if(!Files.exists(archivo)){
            return resultado;
        }
        
        try{
            for (String linea : Files.readAllLines(archivo)) {
                if(!linea.isBlank()){
                    try{
                        resultado.add(desdeLinea(linea));
                    } catch (RuntimeException e){
                        System.err.println("Linea ignorada en " + archivo + ": " + linea);
                    }
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer " + archivo,e);
        }
        return resultado;
    }
    
    private void guardarArchivo() {
        List<String> lineas = new ArrayList<>();
        
        for (T entity : entidades) {
            lineas.add(aLinea(entity));
        }
        
        try{
            Path carpeta = archivo.getParent();
            
            if(carpeta != null){
                Files.createDirectories(carpeta);
            }
            
            Files.write(archivo, lineas);
        } catch (IOException e){
            throw new PersistenciaException("No se pudo guardar " + archivo, e);
        }
    }
    
    @Override
    public void crear(T entidad) {
        entidades.add(entidad);      // la agrego a la memoria
        guardarArchivo();            // y actualizo el disco
    }
    
    @Override
    public List<T> listarTodos() {
        return List.copyOf(entidades);   // una copia: nadie de afuera puede tocar la lista real
    }
    
    @Override
    public Optional<T> buscarPorId(ID id) {
        for (T entidad : entidades) {
            if (idDe(entidad).equals(id)) {      // ¿el id de esta entidad es el que busco?
                return Optional.of(entidad);     // la encontré: la devuelvo adentro de la cajita
            }
        }
        return Optional.empty();                 // recorrí todo y no estaba: cajita vacía
    }
    
    @Override
    public void actualizar(T entidad) {
        for (int i = 0; i < entidades.size(); i++) {
            if (idDe(entidades.get(i)).equals(idDe(entidad))) {   // ¿misma id?
                entidades.set(i, entidad);                         // reemplazo la vieja por la nueva
                guardarArchivo();
                return;                                            // listo, salgo del método
            }
        }
        throw new EntidadNoEncontradaException("No existe el registro con id " + idDe(entidad));
    }
    
    @Override
    public void eliminar(ID id) {
        for (int i = 0; i < entidades.size(); i++) {
            if (idDe(entidades.get(i)).equals(id)) {
                entidades.remove(i);                  // la saco de la memoria
                guardarArchivo();
                return;
            }
        }
        throw new EntidadNoEncontradaException("No existe el registro con id " + id);
    }
}
