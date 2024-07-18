package org.francopaiz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.francopaiz.main.Principal;

/**
 *
 * @author fpaiz
 */
public class ProgramadorController  implements Initializable{
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
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    
}
