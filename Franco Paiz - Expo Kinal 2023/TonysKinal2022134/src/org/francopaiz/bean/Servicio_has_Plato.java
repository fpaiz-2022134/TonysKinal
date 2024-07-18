package org.francopaiz.bean;

import org.francopaiz.main.Principal;

/**
 *
 * @author fpaiz
 */
public class Servicio_has_Plato {
    private int Servicios_codigoServicio;
    private int codigoPlato;
    private int codigoServicio;
    private Principal escenarioPrincipal;

    public Servicio_has_Plato() {
    }

    public Servicio_has_Plato(int Servicios_codigoServicio, int codigoPlato, int codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
        this.codigoPlato = codigoPlato;
        this.codigoServicio = codigoServicio;
    }

    public int getServicios_codigoServicio() {
        return Servicios_codigoServicio;
    }

    public void setServicios_codigoServicio(int Servicios_codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    
}
