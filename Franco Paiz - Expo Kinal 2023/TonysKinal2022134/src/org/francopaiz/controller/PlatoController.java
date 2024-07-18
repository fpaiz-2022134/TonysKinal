/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.francopaiz.bean.Plato;
import org.francopaiz.bean.TipoPlato;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;

/**
 *
 * @author fpaiz
 */
public class PlatoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Plato> listaPlatos;
    private ObservableList<TipoPlato> listaTiposPlatos;
    
    //Text
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcionPlato;
    @FXML private TextField txtPrecioPlato;
    
    //Combobox
    @FXML private ComboBox cmbCodigoTipoPlato;
    
    // Table view and columns
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private TableColumn colPrecioPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    
    //Button
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
        cmbCodigoTipoPlato.setItems(getTipoPlato());
        desactivarControles();
    }
    
    
    public void cargarDatos(){
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));
        
    }
    public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){
            if (tblPlatos.getSelectionModel().getSelectedItem()!=null) {
                txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
                txtNombrePlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
                txtDescripcionPlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
                txtPrecioPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
                cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            } else {
                JOptionPane.showMessageDialog(null, "No hay datos en este campo.\n" + "Seleccione otro campo.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No puedes guardar los datos de un registro ya existente.");
           // limpiarControles();
            tblPlatos.getSelectionModel().clearSelection();
        }
        
        
    }
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),  
                        registro.getString("descripcionTipo")
                
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")
                
                ));
            }
        } catch (Exception e) {
        }
        
        return listaPlatos = FXCollections.observableArrayList(lista);
    }
    
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTiposPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),  
                        resultado.getString("descripcionTipo")
                        
                
                ));
            }
        } catch (Exception e) {
        }
        
        return listaTiposPlatos = FXCollections.observableArrayList(lista);
    }
    
    
    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/francopaiz/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/francopaiz/image/cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                if (txtCantidad.getText().isEmpty()|| txtNombrePlato.getText().isEmpty() || txtDescripcionPlato.getText().isEmpty() || 
                         txtPrecioPlato.getText().isEmpty()|| cmbCodigoTipoPlato.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Debe rellenar los espacios.");
                } else {
                        guardar();
                        limpiarControles();
                        desactivarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                        imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                        tipoDeOperacion = operaciones.NINGUNO;
                        cmbCodigoTipoPlato.setValue("");
                        cargarDatos();

                    
                }
        }

   }

    public void guardar() {
        Plato registro = new Plato();
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato) cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlato(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getCodigoTipoPlato());
            procedimiento.execute();
            listaPlatos.add(registro);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Crear");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                tipoDeOperacion=operaciones.NINGUNO;
                break;
            default:
                if (tblPlatos.getSelectionModel().getSelectedItem()!= null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Plato",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlato(?)}");
                            procedimiento.setInt(1, ((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            listaPlatos.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cmbCodigoTipoPlato.setValue("");
                            tblPlatos.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            //e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Este registro no puede ser eliminado mientras sea usado en una tabla has.\n" + "- Por favor, revisar -");
                        }
                    } else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        cmbCodigoTipoPlato.setValue("");
                        desactivarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                        imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                        tipoDeOperacion = operaciones.NINGUNO;
                        tblPlatos.getSelectionModel().clearSelection();
                    }
                    
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
        }
    }
    
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/francopaiz/image/refreshh.png"));
                    imgReporte.setImage(new Image("/org/francopaiz/image/cancelar.png"));
                    activarControles();
                    cmbCodigoTipoPlato.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                if (txtCantidad.getText().isEmpty()|| txtNombrePlato.getText().isEmpty() || txtDescripcionPlato.getText().isEmpty() || 
                         txtPrecioPlato.getText().isEmpty()|| cmbCodigoTipoPlato.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Debe rellenar los espacios");
                } else {
                    actualizar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    imgEditar.setImage(new Image("/org/francopaiz/image/refresh_208px.png"));
                    imgReporte.setImage(new Image("/org/francopaiz/image/document_500px.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    break;
                }
                

                
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarPlato(?,?,?,?,?)}");
            Plato registro = (Plato)tblPlatos.getSelectionModel().getSelectedItem();
            registro.setCodigoPlato(Integer.parseInt(txtCodigoPlato.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcionPlato(txtDescripcionPlato.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setString(4, registro.getDescripcionPlato());
            procedimiento.setDouble(5, registro.getPrecioPlato());
            procedimiento.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            default:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/francopaiz/image/refresh_208px.png"));
                imgReporte.setImage(new Image("/org/francopaiz/image/document_500px.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                tblPlatos.getSelectionModel().clearSelection();
        }
    }
    
    public void desactivarControles(){

        txtCantidad.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
    }
    
     public void activarControles(){
        txtCantidad.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }
     
     
     public void limpiarControles(){
        txtCodigoPlato.clear();
        txtCantidad.clear();
        txtNombrePlato.clear();
        txtDescripcionPlato.clear();
        txtPrecioPlato.clear();
        cmbCodigoTipoPlato.getSelectionModel().clearSelection();
        cmbCodigoTipoPlato.setValue("");
        tblPlatos.getSelectionModel().clearSelection();
    }
     
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    @FXML
    private void numeros(KeyEvent event) {

        String numeros = event.getCharacter();
        String punto = event.getCharacter();
        if (!numeros.matches("[0-9]")) {
            if (!punto.matches("[.]")) {
                event.consume();
            }
        }

    }
    
    
}
