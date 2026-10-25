package prog2.gestiongeriatrica;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import prog2.gestiongeriatrica.controllers.AsignacionController;
import prog2.gestiongeriatrica.controllers.EnfermeroController;
import prog2.gestiongeriatrica.controllers.HabitacionController;
import prog2.gestiongeriatrica.controllers.ReporteController;
import prog2.gestiongeriatrica.controllers.ReservaController;
import prog2.gestiongeriatrica.controllers.ResidenteController;
import prog2.gestiongeriatrica.dao.AsignacionDAOArchivo;
import prog2.gestiongeriatrica.dao.EnfermeroDAOArchivo;
import prog2.gestiongeriatrica.dao.GenericDAO;
import prog2.gestiongeriatrica.dao.HabitacionDAOArchivo;
import prog2.gestiongeriatrica.dao.ReservaDAOArchivo;
import prog2.gestiongeriatrica.dao.ResidenteDAOArchivo;
import prog2.gestiongeriatrica.modelos.Asignacion;
import prog2.gestiongeriatrica.modelos.Enfermero;
import prog2.gestiongeriatrica.modelos.Habitacion;
import prog2.gestiongeriatrica.modelos.Reserva;
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
import prog2.gestiongeriatrica.vistas.VistaHabitaciones;
import prog2.gestiongeriatrica.vistas.VistaPersonas;
import prog2.gestiongeriatrica.vistas.VistaReportes;
import prog2.gestiongeriatrica.vistas.VistaReservas;

/**
 * Arma el sistema y lo arranca. Es el único lugar donde se eligen las
 * implementaciones concretas (por ejemplo, archivos en vez de MySQL).
 *
 * @author Emanuel
 */
public class GestionGeriatrica {

    private static final Path CARPETA_DATOS = Path.of("data");

    public static void main(String[] args) {
        
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        
        // 1. Persistencia: lo único que sabe que hay archivos de texto
        GenericDAO<Residente, Integer> residenteDAO = new ResidenteDAOArchivo(CARPETA_DATOS.resolve("residentes.txt"));
        GenericDAO<Enfermero, Integer> enfermeroDAO = new EnfermeroDAOArchivo(CARPETA_DATOS.resolve("enfermeros.txt"));
        GenericDAO<Habitacion, String> habitacionDAO = new HabitacionDAOArchivo(CARPETA_DATOS.resolve("habitaciones.txt"));
        GenericDAO<Reserva, String> reservaDAO = new ReservaDAOArchivo(CARPETA_DATOS.resolve("reservas.txt"));
        GenericDAO<Asignacion, String> asignacionDAO = new AsignacionDAOArchivo(CARPETA_DATOS.resolve("asignaciones.txt"));

        // 2. Reglas de negocio: reciben los DAO, no los crean
        ResidenteServicio residenteServicio = new ResidenteServicio(residenteDAO, enfermeroDAO, reservaDAO);
        EnfermeroServicio enfermeroServicio = new EnfermeroServicio(enfermeroDAO, residenteDAO, asignacionDAO);
        HabitacionServicio habitacionServicio = new HabitacionServicio(habitacionDAO, reservaDAO, asignacionDAO);
        ReservaServicio reservaServicio = new ReservaServicio(reservaDAO, residenteDAO, habitacionDAO);
        AsignacionServicio asignacionServicio = new AsignacionServicio(asignacionDAO, enfermeroDAO, habitacionDAO);
        ReporteServicio reporteServicio = new ReporteServicio(residenteDAO, enfermeroDAO, reservaDAO, asignacionDAO);

        // 3. Vista
        Consola consola = new Consola();
        MenuPrincipal menu = new MenuPrincipal(consola);
        menu.registrar(new VistaPersonas("Residentes", new ResidenteController(residenteServicio), consola));
        menu.registrar(new VistaPersonas("Enfermeros", new EnfermeroController(enfermeroServicio), consola));
        menu.registrar(new VistaHabitaciones(new HabitacionController(habitacionServicio), consola));
        menu.registrar(new VistaReservas(new ReservaController(reservaServicio), consola));
        menu.registrar(new VistaAsignaciones(new AsignacionController(asignacionServicio), consola));
        menu.registrar(new VistaReportes(new ReporteController(reporteServicio), consola));

        menu.iniciar();
    }
}
