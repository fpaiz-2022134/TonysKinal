package org.francopaiz.bean;

/**
 *
 * @author fpaiz
 */
public class Producto {
    private int codigoProducto;
    private String nombreProducto;
    private int cantidadProducto;

    public Producto() {
    }

    public Producto(int codigoProducto, String nombreProducto, int cantidadProducto) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadProducto = cantidadProducto;
    }

   

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }
    
     @Override
    public String toString() {
        return codigoProducto + " | " + nombreProducto;
    }
    
    
  

    

    
    
    
    
}
