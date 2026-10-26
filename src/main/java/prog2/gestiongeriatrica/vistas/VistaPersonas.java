package prog2.gestiongeriatrica.vistas;

import java.util.List;
import prog2.gestiongeriatrica.controllers.PersonaController;
import prog2.gestiongeriatrica.dto.PersonaDTO;
import prog2.gestiongeriatrica.excepciones.GeriatricoException;

/**
 * Pantalla de alta, listado, consulta, modificación y baja de personas. El menú es el mismo
 * para residentes y enfermeros; cada hija dice qué muestra su ficha (opción Consultar).
 *
 * @author Emanuel
 */
public abstract class VistaPersonas implements Vista {

    private final String titulo;
    private final PersonaController controller;
    private final Consola consola;

    protected VistaPersonas(String titulo, PersonaController controller, Consola consola) {
        this.titulo = titulo;
        this.controller = controller;
        this.consola = consola;
    }

    /** La ficha completa de una persona (opción Consultar): sus datos y sus relaciones. */
    protected abstract void mostrarFicha(PersonaDTO persona);

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
            System.out.println("1. Alta | 2. Listar | 3. Consultar | 4. Modificar | 5. Baja | 6. Volver");
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
                        consultar();
                        break;
                    case 4:
                        modificar();
                        break;
                    case 5:
                        baja();
                        break;
                    case 6:
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

    /** Muestra el listado. Devuelve false si no hay nadie, así no se pide un id que no se puede elegir. */
    private boolean listar() {
        List<PersonaDTO> personas = controller.listar();
        if (personas.isEmpty()) {
            System.out.println("No hay registros.");
            return false;
        }
        for (PersonaDTO p : personas) {
            System.out.println(p.getId() + " | DNI " + p.getDni() + " | " + p.getNombre() + " " + p.getApellido());
        }
        return true;
    }

    // consultar, modificar y baja primero muestran la lista: el usuario elige el id mirándola.
    // Listar muestra un resumen de todos; Consultar, la ficha completa de uno (con sus relaciones)
    private void consultar() {
        if (listar()) {
            int id = consola.pedirEntero("Id a consultar: ");
            mostrarFicha(controller.buscar(id));
        }
    }

    private void modificar() {
        if (listar()) {
            int id = consola.pedirEntero("Id a modificar: ");
            PersonaDTO actual = controller.buscar(id);
            System.out.println("Modificando a " + actual.getNombre() + " " + actual.getApellido() + ". Ingresá los datos nuevos:");
            controller.modificar(id, pedirDatos());
            System.out.println("Modificado.");
        }
    }

    private void baja() {
        if (listar()) {
            int id = consola.pedirEntero("Id a dar de baja: ");
            controller.baja(id);
            System.out.println("Eliminado.");
        }
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
