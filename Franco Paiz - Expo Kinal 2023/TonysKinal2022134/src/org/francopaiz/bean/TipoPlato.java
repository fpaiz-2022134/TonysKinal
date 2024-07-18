
package org.francopaiz.bean;

/**
 *
 * @author fpaiz
 */
public class TipoPlato {
    
    public int codigoTipoPlato;
    public String descripcionTipo;

    public TipoPlato() {
    }

    public TipoPlato(int codigoTipoPlato, String descripcionTipo) {
        this.codigoTipoPlato = codigoTipoPlato;
        this.descripcionTipo = descripcionTipo;
    }
    
    

    public int getCodigoTipoPlato() {
        return codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public String getDescripcionTipo() {
        return descripcionTipo;
    }

    public void setDescripcionTipo(String descripcionTipo) {
        this.descripcionTipo = descripcionTipo;
    }
    
         @Override
    public String toString() {
        return codigoTipoPlato + " | " + descripcionTipo;
    }
    
    
    
    
}
