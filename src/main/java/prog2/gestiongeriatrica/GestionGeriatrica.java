package prog2.gestiongeriatrica;

import java.io.PrintStream;
import java.nio.file.Path;
import prog2.gestiongeriatrica.controllers.AsignacionController;
import prog2.gestiongeriatrica.controllers.EnfermeroController;
import prog2.gestiongeriatrica.controllers.HabitacionController;
import prog2.gestiongeriatrica.controllers.PersonaController;
import prog2.gestiongeriatrica.controllers.ReporteController;
import prog2.gestiongeriatrica.controllers.ReservaController;
import prog2.gestiongeriatrica.controllers.ResidenteController;
import prog2.gestiongeriatrica.dao.EnfermeroDAOArchivo;
import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dao.HabitacionDAOArchivo;
import prog2.gestiongeriatrica.dao.ResidenteDAOArchivo;
import prog2.gestiongeriatrica.dao.SecuenciaArchivoDAO;
import prog2.gestiongeriatrica.dao.SecuenciaDAO;
import prog2.gestiongeriatrica.excepciones.PersistenciaException;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Habitacion;
import prog2.gestiongeriatrica.modelos.Residente;
import prog2.gestiongeriatrica.servicios.AsignacionServicio;
import prog2.gestiongeriatrica.servicios.EnfermeroServicio;
import prog2.gestiongeriatrica.servicios.HabitacionServicio;
import prog2.gestiongeriatrica.servicios.ReporteServicio;
import prog2.gestiongeriatrica.servicios.ReservaServicio;
import prog2.gestiongeriatrica.servicios.ResidenteServicio;
import prog2.gestiongeriatrica.vistas.Consola;
import prog2.gestiongeriatrica.vistas.MenuPrincipal;
import prog2.gestiongeriatrica.vistas.VistaAsignaciones;
import prog2.gestiongeriatrica.vistas.VistaEnfermeros;
import prog2.gestiongeriatrica.vistas.VistaHabitaciones;
import prog2.gestiongeriatrica.vistas.VistaReportes;
import prog2.gestiongeriatrica.vistas.VistaReservas;
import prog2.gestiongeriatrica.vistas.VistaResidentes;

/**
 * Arma el sistema y lo arranca. Es el único lugar donde se eligen las
 * implementaciones concretas (por ejemplo, archivos en vez de MySQL).
 *
 * @author Emanuel
 */
public class GestionGeriatrica {

    private static final Path CARPETA_DATOS = Path.of("data");

    public static void main(String[] args) {

        // los acentos se escriben con la misma codificación que usa la pantalla (cmd o NetBeans)
        System.setOut(new PrintStream(System.out, true, Consola.codificacion()));

        try {
            armarSistema().iniciar();
        } catch (PersistenciaException e) {
            // por ejemplo, un archivo de data/ que no se puede leer al arrancar
            System.out.println("No se pudo iniciar el sistema: " + e.getMessage());
        }
    }

    /** Crea todas las piezas y las conecta. Devuelve el menú listo para arrancar. */
    private static MenuPrincipal armarSistema() {

        // 1. Persistencia: lo único que sabe que hay archivos de texto
        GenericDAO<Residente, Integer> residenteDAO = new ResidenteDAOArchivo(CARPETA_DATOS.resolve("residentes.txt"));
        GenericDAO<Enfermero, Integer> enfermeroDAO = new EnfermeroDAOArchivo(CARPETA_DATOS.resolve("enfermeros.txt"));
        // las reservas y asignaciones se guardan dentro de cada habitación (idResidente, idEnfermero)
        GenericDAO<Habitacion, String> habitacionDAO = new HabitacionDAOArchivo(CARPETA_DATOS.resolve("habitaciones.txt"));

        // Últimos números usados: así no se repiten ids ni códigos aunque se borre el registro
        SecuenciaDAO secuenciaResidentes = new SecuenciaArchivoDAO(CARPETA_DATOS.resolve("ultimo-residente.txt"));
        SecuenciaDAO secuenciaEnfermeros = new SecuenciaArchivoDAO(CARPETA_DATOS.resolve("ultimo-enfermero.txt"));
        SecuenciaDAO secuenciaHabitaciones = new SecuenciaArchivoDAO(CARPETA_DATOS.resolve("ultima-habitacion.txt"));

        // 2. Reglas de negocio: reciben los DAO, no los crean
        ResidenteServicio residenteServicio = new ResidenteServicio(residenteDAO, enfermeroDAO, habitacionDAO, secuenciaResidentes);
        EnfermeroServicio enfermeroServicio = new EnfermeroServicio(enfermeroDAO, residenteDAO, habitacionDAO, secuenciaEnfermeros);
        HabitacionServicio habitacionServicio = new HabitacionServicio(habitacionDAO, residenteDAO, enfermeroDAO,
                secuenciaHabitaciones);
        ReservaServicio reservaServicio = new ReservaServicio(habitacionDAO, residenteDAO);
        AsignacionServicio asignacionServicio = new AsignacionServicio(habitacionDAO, enfermeroDAO);
        ReporteServicio reporteServicio = new ReporteServicio(residenteDAO, enfermeroDAO, habitacionDAO);

        // 3. Vista (los controllers de residentes y enfermeros también los usan otras pantallas para mostrar listas)
        PersonaController residenteController = new ResidenteController(residenteServicio);
        PersonaController enfermeroController = new EnfermeroController(enfermeroServicio);

        // estos dos los usan varias pantallas (las fichas de residentes y enfermeros, las consultas)
        AsignacionController asignacionController = new AsignacionController(asignacionServicio);
        ReporteController reporteController = new ReporteController(reporteServicio);

        Consola consola = new Consola();
        MenuPrincipal menu = new MenuPrincipal(consola);
        menu.registrar(new VistaResidentes(residenteController, reporteController, consola));
        menu.registrar(new VistaEnfermeros(enfermeroController, asignacionController, reporteController, consola));
        menu.registrar(new VistaHabitaciones(new HabitacionController(habitacionServicio), consola));
        menu.registrar(new VistaReservas(new ReservaController(reservaServicio), consola));
        menu.registrar(new VistaAsignaciones(asignacionController, enfermeroController, consola));
        menu.registrar(new VistaReportes(reporteController, residenteController, enfermeroController, consola));
        return menu;
    }
}
