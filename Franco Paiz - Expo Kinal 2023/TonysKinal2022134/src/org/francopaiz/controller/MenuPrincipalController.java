package org.francopaiz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.francopaiz.main.Principal;

/**
 *
 * @author fpaiz
 */
public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
   

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
        
    }
    
    public void ventanaProgramador(){
        // Para que la venta del programador pueda abrirse...
        // Debemos indicar en la vista este método dentro de la acción del botón.
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
     public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
     
     public void ventanaTipoPlato(){
         escenarioPrincipal.ventanaTipoPlato();
     }
     
     public void ventanaProducto(){
         escenarioPrincipal.ventanaProducto();
     }
     
     public void ventanaPresupuesto(){
         escenarioPrincipal.ventanaPresupuesto();
     }
     
     public void ventanaServicio(){
         escenarioPrincipal.ventanaServicio();
     }
     
     public void ventanaEmpleado(){
         escenarioPrincipal.ventanaEmpleado();
     }
     
     public void ventanaPlato(){
         escenarioPrincipal.ventanaPlato();
     }
     
     public void ventanaLogin(){
         escenarioPrincipal.ventanaLogin();
     }
     public void ventanaUsuario(){
         escenarioPrincipal.ventanaUsuario();
     }
     
     public void ventanaProducto_has_Plato(){
         escenarioPrincipal.ventanaProducto_has_Plato();
     }
     
     public void ventanaServicio_has_Plato(){
         escenarioPrincipal.ventanaServicio_has_Plato();
     }
     public void ventanaServicio_has_Empleado(){
         escenarioPrincipal.ventanaServicio_has_Empleado();
     }
             
             
}
