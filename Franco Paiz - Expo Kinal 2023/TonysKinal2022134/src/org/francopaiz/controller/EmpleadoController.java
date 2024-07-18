
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
import org.francopaiz.bean.Empleado;
import org.francopaiz.bean.TipoEmpleado;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;

/**
 *
 * @author fpaiz
 * 
 */
public class EmpleadoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empleado> listaEmpleados;
    private ObservableList<TipoEmpleado> listaTiposEmpleados;
    
    //Text
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtApellidosEmpleado;
    @FXML private TextField txtNombresEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtGradoCocinero;
    
    //Combo Box
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    // Table View and Table  Columns
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colApellidosEmpleado;
    @FXML private TableColumn colNombresEmpleado;
    @FXML private TableColumn colDireccionEmpleado;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
    
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
        desactivarControles();
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
    }
    
    
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
        
    }
    
     public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){
            if (tblEmpleados.getSelectionModel().getSelectedItem()!=null) {
                txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
                txtApellidosEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
                txtNombresEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
                txtDireccionEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
                txtTelefonoContacto.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
                txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
                cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            } else {
                JOptionPane.showMessageDialog(null, "No hay datos en este campo.\n" + "Seleccione otro campo.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No puedes guardar los datos de un registro ya existente.");
            //limpiarControles();
            tblEmpleados.getSelectionModel().clearSelection();
        }
        
        
    }
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                        registro.getString("descripcion")         
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleados= FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTiposEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTiposEmpleados= FXCollections.observableArrayList(lista);
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
                if (txtNumeroEmpleado.getText().isEmpty() || txtApellidosEmpleado.getText().isEmpty() || txtNombresEmpleado.getText().isEmpty()
                        ||txtDireccionEmpleado.getText().isEmpty()||txtTelefonoContacto.getText().isEmpty() || txtGradoCocinero.getText().isEmpty()|| cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Debe rellenar los espacios.");
                } else {
                    if (txtTelefonoContacto.getText().length() > 8) {
                        JOptionPane.showMessageDialog(null, "No puede ingresar más de 8 dígitos en el teléfono.");
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
                        cmbCodigoTipoEmpleado.setValue("");
                        cargarDatos();

                    }
                }
        }
    }

    public void guardar() {
        Empleado registro = new Empleado();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
        registro.setNombresEmpleado(txtNombresEmpleado.getText());
        registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setCodigoTipoEmpleado(((TipoEmpleado) cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getApellidosEmpleado());
            procedimiento.setString(3, registro.getNombresEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleados.add(registro);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                tipoDeOperacion=operaciones.NINGUNO;
                break;
            default:
                if (tblEmpleados.getSelectionModel().getSelectedItem()!= null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empleado",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                            procedimiento.setInt(1, ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cmbCodigoTipoEmpleado.setValue("");
                            tblEmpleados.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            //e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Este registro no puede ser eliminado mientras sea usado en una tabla has.\n" + "- Por favor, revisar -");
                        }
                    } else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        cmbCodigoTipoEmpleado.setValue("");
                        desactivarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                        imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                        tipoDeOperacion = operaciones.NINGUNO;
                        tblEmpleados.getSelectionModel().clearSelection();
                    }
                    
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
        }
    }
    
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/francopaiz/image/refreshh.png"));
                    imgReporte.setImage(new Image("/org/francopaiz/image/cancelar.png"));
                    activarControles();
                    cmbCodigoTipoEmpleado.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                if (txtNumeroEmpleado.getText().isEmpty() || txtApellidosEmpleado.getText().isEmpty() || txtNombresEmpleado.getText().isEmpty()
                        ||txtDireccionEmpleado.getText().isEmpty()||txtTelefonoContacto.getText().isEmpty() || txtGradoCocinero.getText().isEmpty()|| cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem() == null) {
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmpleado(?,?,?,?,?,?,?)}");
            Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
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
                tblEmpleados.getSelectionModel().clearSelection();
        }
    }
    
    
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoEmpleado.setEditable(true);
        txtNumeroEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbCodigoTipoEmpleado.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtApellidosEmpleado.clear();
        txtNombresEmpleado.clear();
        txtDireccionEmpleado.clear();
        txtTelefonoContacto.clear();
        txtGradoCocinero.clear();
        cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
        cmbCodigoTipoEmpleado.setValue("");
        tblEmpleados.getSelectionModel().clearSelection();
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
}
