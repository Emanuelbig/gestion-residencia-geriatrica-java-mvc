package prog2.gestiongeriatrica.controllers;

import prog2.gestiongeriatrica.servicios.ReservaServicio;

/**
 * Recibe los pedidos de la vista de reservas y se los pasa al service.
 * @author Emanuel
 */
public class ReservaController {

    private final ReservaServicio servicio;

    public ReservaController(ReservaServicio servicio) {
        this.servicio = servicio;
    }

    public void reservar(int idResidente, String codigo) {
        servicio.reservar(idResidente, codigo);
    }

    public void liberar(String codigo) {
        servicio.liberar(codigo);
    }
}
