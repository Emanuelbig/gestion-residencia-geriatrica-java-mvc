package prog2.gestiongeriatrica.vistas;

import java.util.List;
import prog2.gestiongeriatrica.controllers.PersonaController;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.excepciones.GeriatricoException;

/**
 * Pantalla de alta, listado, modificación y baja de personas. La misma clase
 * sirve para residentes y para enfermeros.
 *
 * @author Emanuel
 */
public class VistaPersonas implements Vista {

    private final String titulo;
    private final PersonaController controller;
    private final Consola consola;

    public VistaPersonas(String titulo, PersonaController controller, Consola consola) {
        this.titulo = titulo;
        this.controller = controller;
        this.consola = consola;
    }

    @Override
    public String getTitulo() {
        return titulo;
    }

    @Override
    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("-- " + titulo.toUpperCase() + " --");
            System.out.println("1. Alta | 2. Listar | 3. Modificar | 4. Baja | 5. Volver");
            int opcion = consola.pedirEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1:
                        alta();
                        break;
                    case 2:
                        listar();
                        break;
                    case 3:
                        modificar();
                        break;
                    case 4:
                        baja();
                        break;
                    case 5:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (GeriatricoException e) {
                System.out.println("No se pudo completar: " + e.getMessage());
            }
        }
    }

    private void alta() {
        controller.alta(pedirDatos());
        System.out.println("Registrado.");
    }

    private void listar() {
        List<PersonaDTO> personas = controller.listar();
        if (personas.isEmpty()) {
            System.out.println("No hay registros.");
        }
        for (PersonaDTO p : personas) {
            System.out.println(p.getId() + " | DNI " + p.getDni() + " | " + p.getNombre() + " " + p.getApellido());
        }
    }

    private void modificar() {
        listar();
        int id = consola.pedirEntero("Id a modificar: ");
        PersonaDTO actual = controller.buscar(id);
        System.out.println("Modificando a " + actual.getNombre() + " " + actual.getApellido() + ". Ingresá los datos nuevos:");
        controller.modificar(id, pedirDatos());
        System.out.println("Modificado.");
    }

    private void baja() {
        listar();
        int id = consola.pedirEntero("Id a dar de baja: ");
        controller.baja(id);
        System.out.println("Eliminado.");
    }

    private PersonaDTO pedirDatos() {
        String dni = consola.pedirConFormato("DNI: ", PersonaDTO.PATRON_DNI,
                "El DNI debe tener 7 u 8 números, sin puntos.");
        String nombre = consola.pedirConFormato("Nombre: ", PersonaDTO.PATRON_NOMBRE,
                "Solo letras y espacios (2 a 40).");
        String apellido = consola.pedirConFormato("Apellido: ", PersonaDTO.PATRON_NOMBRE,
                "Solo letras y espacios (2 a 40).");
        return new PersonaDTO(dni, nombre, apellido, null);
    }
}
