package prog2.gestiongeriatrica.vistas;

import java.util.ArrayList;
import java.util.List;

/**
 * Menú principal: lista las vistas registradas y abre la elegida.
 * Agregar una pantalla nueva no requiere modificar esta clase.
 * @author Emanuel
 */
public class MenuPrincipal {

    private final Consola consola;
    private final List<Vista> vistas = new ArrayList<>();

    public MenuPrincipal(Consola consola) {
        this.consola = consola;
    }

    public void registrar(Vista vista) {
        vistas.add(vista);
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("=== RESIDENCIA GERIÁTRICA ===");
            for (int i = 0; i < vistas.size(); i++) {
                System.out.println((i + 1) + ". " + vistas.get(i).getTitulo());
            }
            int opcionSalir = vistas.size() + 1;
            System.out.println(opcionSalir + ". Salir");

            int elegida = consola.pedirEntero("Opción: ");
            if (elegida == opcionSalir) {
                salir = true;
            } else if (elegida >= 1 && elegida <= vistas.size()) {
                vistas.get(elegida - 1).mostrar();
            } else {
                System.out.println("Opción inválida.");
            }
        }
        System.out.println("Hasta luego.");
    }
}
