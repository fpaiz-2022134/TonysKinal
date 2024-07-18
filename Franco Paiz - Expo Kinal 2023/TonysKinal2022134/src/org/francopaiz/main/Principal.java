/*
    Programador: Franco Alejandro Paiz González
    Código Técnico: IN5AV
    Carné: 2022134
    Creación: 28/03/2023
    Fechas de modificación: 28/03/2023 5h
        11/04/2023- 10 h
        12/04/2023- 10h
        17/04/2023- 10h
        18/04/2023- 10h
        23/04/2023 3 h
        24/04/2023- 10h
        25/04/2023- 10h
        26/04/2023- 10h
        8/05/2023 - 10h
        9/05/2023- 10h
        10/05/2023- 10h
        22/05/2023- 10h
        23/05/2023- 10h
        24/05/2023- 10h
        28/05/2023 5 h
        29/05/2023- 10h
        30/05/2023- 10h
 */
package org.francopaiz.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.francopaiz.bean.Producto_has_Plato;
import org.francopaiz.controller.EmpleadoController;
import org.francopaiz.controller.EmpresaController;
import org.francopaiz.controller.LoginController;
import org.francopaiz.controller.MenuPrincipalController;
import org.francopaiz.controller.PlatoController;
import org.francopaiz.controller.PresupuestoController;
import org.francopaiz.controller.ProductoController;
import org.francopaiz.controller.Producto_has_PlatoController;
import org.francopaiz.controller.ProgramadorController;
import org.francopaiz.controller.ServicioController;
import org.francopaiz.controller.Servicio_has_EmpleadoController;
import org.francopaiz.controller.Servicio_has_PlatoController;
import org.francopaiz.controller.TipoEmpleadoController;
import org.francopaiz.controller.TipoPlatoController;
import org.francopaiz.controller.UsuarioController;

/**
 *
 * @author fpaiz
 */
public class Principal extends Application {

    private final String PAQUETE_VISTA = "/org/francopaiz/view/";
    private Stage escenarioPrincipal;
    private Scene escena;

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Tony's Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/francopaiz/image/MenuFP.jpg"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/francopaiz/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        escenarioPrincipal.setResizable(false);
        
        ventanaLogin();
        //menuPrincipal();
        escenarioPrincipal.show();

    }

    public void menuPrincipal() {
        try {
            // Aquí utilizamos el método de cambiarEscena e indicamos que queremos abrir el menú+ ptincipal.
            MenuPrincipalController menu = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 605, 510);
            menu.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
            // En esta porción de código indicamos que deseamos ver la ventana del Programador.
            // Casteamos el valor a la clase del controlador
            ProgramadorController progra = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 661, 413);
            progra.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    // Método que permite ver la ventana de la Empresa.
    public void ventanaEmpresa(){
        try{
            EmpresaController empresaController = (EmpresaController)cambiarEscena("EmpresaView.fxml",933,539);
            empresaController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaTipoEmpleado(){
        
        try{
            TipoEmpleadoController tipoEmpleadoController = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml",897,539); 
            tipoEmpleadoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
       
 
        
    }
    
    public void ventanaTipoPlato(){
        
        try{
            TipoPlatoController tipoPlatoController = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml",897,473); 
            tipoPlatoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
       
 
        
    }
    
    public void ventanaProducto(){
        
        try{
            ProductoController productoController = (ProductoController) cambiarEscena("ProductoView.fxml",897,473); 
            productoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
       
 
        
    }
    
    public void ventanaPresupuesto(){
        try {
            PresupuestoController presupuestoController = (PresupuestoController) cambiarEscena("PresupuestoView.fxml",933,539);
            presupuestoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicio(){
        try {
            ServicioController servicioController = (ServicioController) cambiarEscena("ServicioView.fxml",1100,539);
            servicioController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpleado(){
        try {
            
            EmpleadoController empleadoController = (EmpleadoController) cambiarEscena("EmpleadoView.fxml",1100,539);
            empleadoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaPlato(){
        try {
            
            PlatoController empleadoController = (PlatoController) cambiarEscena("PlatoView.fxml",1020,473);
            empleadoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        try {
            LoginController loginController = (LoginController)cambiarEscena("LoginView.fxml", 700, 500);
            loginController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try {
            UsuarioController usuarioController = (UsuarioController) cambiarEscena("UsuarioView.fxml", 808,473);
            usuarioController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProducto_has_Plato(){
        try {
            Producto_has_PlatoController producto_has_PlatoController = (Producto_has_PlatoController)cambiarEscena("Producto_has_PlatoView.fxml", 808, 473);
            producto_has_PlatoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicio_has_Plato(){
        try {
            Servicio_has_PlatoController servicio_has_PlatoController = (Servicio_has_PlatoController) cambiarEscena("Servicio_has_PlatoView.fxml", 808, 473);
            servicio_has_PlatoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicio_has_Empleado(){
        try {
            Servicio_has_EmpleadoController servicio_has_EmpleadoController = (Servicio_has_EmpleadoController) cambiarEscena("Servicio_has_ EmpleadoView.fxml", 962, 473);
            servicio_has_EmpleadoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        // EL InputStream permite saber donde se encuentra el archivo.
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        // El BuilderFactory permite manipular los elementos de JavaFX en Java.
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        //Casteamos para que sea compatible al tipo Initializable que será returnado.
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;

    }

}
