package prog2.gestiongeriatrica.vistas;

/**
 * Una pantalla del sistema. El menú principal las muestra sin saber cuál es cada una.
 * @author Emanuel
 */
public interface Vista {

    String getTitulo();

    void mostrar();
}