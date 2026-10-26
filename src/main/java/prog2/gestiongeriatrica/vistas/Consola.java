package prog2.gestiongeriatrica.vistas;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Lee datos del teclado. Solo controla que la entrada se pueda leer (que no
 * esté vacía, que sea un número); las reglas las valida el service.
 *
 * @author Emanuel
 */
public class Consola {

    private final Scanner scanner = new Scanner(System.in, codificacion());

    /**
     * Codificación de los acentos. En cmd se usa la de la terminal (si no, "Geriátrica" sale mal);
     * en NetBeans no hay terminal (System.console() es null) y se usa UTF-8.
     */
    public static Charset codificacion() {
        return System.console() != null ? System.console().charset() : StandardCharsets.UTF_8;
    }

    public String pedirTexto(String mensaje) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("El campo no puede estar vacío.");
            }
        }
        return texto;
    }

    public int pedirEntero(String mensaje) {
        String entrada = pedirTexto(mensaje);
        while (!entrada.matches("-?\\d{1,9}")) {
            System.out.println("Ingresá un número entero.");
            entrada = pedirTexto(mensaje);
        }
        return Integer.parseInt(entrada);
    }

    public String pedirConFormato(String mensaje, String patron, String error) {
        String texto = pedirTexto(mensaje);
        while (!texto.matches(patron)) {
            System.out.println(error);
            texto = pedirTexto(mensaje);
        }
        return texto;
    }
}
