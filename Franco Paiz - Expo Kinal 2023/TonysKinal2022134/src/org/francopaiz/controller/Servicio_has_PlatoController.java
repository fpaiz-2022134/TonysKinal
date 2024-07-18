package org.francopaiz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.francopaiz.bean.Plato;
import org.francopaiz.bean.Servicio;
import org.francopaiz.bean.Servicio_has_Plato;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;

/**
 *
 * @author fpaiz
 */
public class Servicio_has_PlatoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO, GUARDAR, ELIMINAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio_has_Plato> listaServicios_has_Platos;
    private ObservableList <Plato> listaPlatos;
    private ObservableList <Servicio> listaServicios;
    
    // Text Field 

    @FXML private TextField txtCodigoServicioPlato;
    
    // Combo box 
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoServicio;
    
    // Table View 
    @FXML private TableView tblServicios_has_Platos;
    @FXML private TableColumn colCodigoServicioPlato;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoServicio;
    
    // Button
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    
    // Image 
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoServicio.setItems(getServicio());
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblServicios_has_Platos.setItems(getServicio_has_Plato());
        colCodigoServicioPlato.setCellValueFactory(new PropertyValueFactory<Servicio_has_Plato, Integer>("Servicios_codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Servicio_has_Plato, Integer>("codigoPlato"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio_has_Plato, Integer>("codigoServicio"));
    }
    
    public void seleccionarElemento(){
        if (tipoDeOperacion != operaciones.GUARDAR) {
             if (tblServicios_has_Platos.getSelectionModel().getSelectedItem()!= null) {
                txtCodigoServicioPlato.setText(String.valueOf(((Servicio_has_Plato) tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
                cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Servicio_has_Plato)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicio_has_Plato)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            } else {
                 JOptionPane.showMessageDialog(null, "No hay datos en este campo.\n" + "Seleccione otro campo.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No puedes guardar los datos de un registro ya existente.");
        }
    }
    
    public Plato buscarPlato(int codigoPlato){
        Plato  resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Plato(registro.getInt("codigoPlato"),  
                        registro.getInt("cantidad"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcionPlato"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato")
                
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Servicio(registro.getInt("codigoServicio"),
                                        registro.getDate("fechaServicio"),
                                        registro.getString("tipoServicio"),
                                        registro.getTime("horaServicio"),
                                        registro.getString("lugarServicio"),
                                        registro.getString("telefonoContacto"),
                                        registro.getInt("codigoEmpresa")
                );
            }
                   
            
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Servicio_has_Plato> getServicio_has_Plato(){
        ArrayList <Servicio_has_Plato> lista = new ArrayList<Servicio_has_Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio_has_Plato(resultado.getInt("Servicios_codigoServicio"),
                                                 resultado.getInt("codigoPlato"),
                                                 resultado.getInt("codigoServicio")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
                
        
        return listaServicios_has_Platos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlatos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                                        resultado.getDate("fechaServicio"),
                                        resultado.getString("tipoServicio"),
                                        resultado.getTime("horaServicio"),
                                        resultado.getString("lugarServicio"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaServicios = FXCollections.observableArrayList(lista);
        
    }
    
    
    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnReporte.setDisable(true);
                btnEditar.setDisable(true);
                imgNuevo.setImage(new Image("/org/francopaiz/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/francopaiz/image/cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if (txtCodigoServicioPlato.getText().isEmpty() || cmbCodigoPlato.getSelectionModel().getSelectedItem() == null || cmbCodigoServicio.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Debes rellenar los espacios vacíos.");

                } else {
                    guardar();
                    limpiarControles();
                    desactivarControles ();
                    btnNuevo.setText("Crear");
                    btnEliminar.setText("Eliminar");
                    btnReporte.setDisable(false);
                    btnEditar.setDisable(false);
                    imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                    imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                }

        }
    }
    
    public void guardar(){
      // registro.setCodigoTipoPlato(((TipoPlato) cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        Servicio_has_Plato registro = new Servicio_has_Plato();
        registro.setServicios_codigoServicio(Integer.parseInt(txtCodigoServicioPlato.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio_has_Plato(?,?,?)}");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoServicio());
            procedimiento.execute();
            listaServicios_has_Platos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Crear");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case NINGUNO:
                JOptionPane.showMessageDialog(null, "No pueden eliminarse los datos");
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                JOptionPane.showMessageDialog(null, "No pueden editarse los datos");
                break;
            default:
                JOptionPane.showMessageDialog(null, "No pueden editarse los datos");
        }
    }
    public void limpiarControles(){
        txtCodigoServicioPlato.clear();
        cmbCodigoPlato.getSelectionModel().clearSelection();
        cmbCodigoServicio.getSelectionModel().clearSelection();
    }
    
    public void desactivarControles(){
        txtCodigoServicioPlato.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoServicio.setDisable(true);
        
    }
    
    public void activarControles(){
        txtCodigoServicioPlato.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
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
