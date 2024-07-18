
package org.francopaiz.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.francopaiz.bean.Empresa;
import org.francopaiz.bean.Presupuesto;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;
import org.francopaiz.report.GenerarReporte;

/**
 *
 * @author fpaiz
 * 
 */
public class PresupuestoController implements Initializable {
    
    private Principal escenarioPrincipal;
    
    private enum operaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Presupuesto> listaPresupuesto;   
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    private final String fondoPresupuesto = "/org/francopaiz/report/FondoReportes.png";
    
    
    //Text
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    
    //GridPane
    @FXML private GridPane grpFecha;
    // Combo box
    @FXML private ComboBox cmbCodigoEmpresa;
    
    //Table View y columns
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
    
    //Button
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    
    // Images
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/francopaiz/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        desactivarControles();
        
        //cmbCodigoEmpresa.setItems(getPresupuesto());
    }
    
     public void cargarDatos(){
         tblPresupuestos.setItems(getPresupuesto());
         colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
         colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
         colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
         colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
         
            
    }
     
    public void seleccionarElemento() {
        if (tipoDeOperacion != operaciones.GUARDAR) {
            if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
                txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
                fecha.selectedDateProperty().set(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
                txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
                cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            } else {
                JOptionPane.showMessageDialog(null, "No hay datos en este campo.\n" + "Seleccione otro campo.");
            }
        }else{
            JOptionPane.showMessageDialog(null, "No puedes guardar los datos de un registro ya existente.");
             limpiarControles();
             tblPresupuestos.getSelectionModel().clearSelection();
             
        }

    }

    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            
            
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                       registro.getString("nombreEmpresa"),
                                       registro.getString("direccion"),
                                       registro.getString("telefono")
                );
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    public ObservableList<Presupuesto> getPresupuesto() {
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPresupuestos");
            ResultSet resultado = procedimiento.executeQuery();

            // El método Next hace que valide las tuplas o registros.
            while (resultado.next()) {
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                                        resultado.getDate("fechaSolicitud"),
                                        resultado.getDouble("cantidadPresupuesto"),
                                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas()}");
            ResultSet resultado = procedimiento.executeQuery();

            // El método Next hace que valide las tuplas o registros.
            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }

    
    public void nuevo(){
        switch(tipoDeOperacion){
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
                
                if(fecha.getSelectedDate() == null ||  txtCantidadPresupuesto.getText().isEmpty() || cmbCodigoEmpresa.getSelectionModel().getSelectedItem()== null ){
                     JOptionPane.showMessageDialog(null, "Debe rellenar los espacios.");
                }else{
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
                    cmbCodigoEmpresa.setValue("");
                    cargarDatos();
                }
                break;
        }
    }
    
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No puedes ingresar más de 10 dígitos enteros.\n" +  "Y solamente puedes agregar 2 decimales\n");
        }
    }
    
    
    public void generarReporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (cmbCodigoEmpresa.getSelectionModel().getSelectedItem() != null) {
                  imprimirReporte();  
                }else{
                    JOptionPane.showMessageDialog(null, "Debes seleccionar alguna casilla para generar el reporte :)");
                }
                
                break;
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/francopaiz/image/refresh_208px.png"));
                imgReporte.setImage(new Image("/org/francopaiz/image/document_500px.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                tblPresupuestos.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void imprimirReporte(){
        
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        parametros.put("fondoPresupuesto", this.getClass().getResourceAsStream(fondoPresupuesto));
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuesto", parametros);
        
        
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
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblPresupuestos.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                            procedimiento.setInt(1, ((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cmbCodigoEmpresa.setValue("");
                            tblPresupuestos.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        cmbCodigoEmpresa.setValue("");
                        desactivarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                        imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                        tipoDeOperacion = operaciones.NINGUNO;
                        tblPresupuestos.getSelectionModel().clearSelection();
                    }
            } else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                    if(tblPresupuestos.getSelectionModel().getSelectedItem()!= null){
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        imgEditar.setImage(new Image("/org/francopaiz/image/refreshh.png"));
                        imgReporte.setImage(new Image("/org/francopaiz/image/cancelar.png"));
                        activarControles();
                        cmbCodigoEmpresa.setDisable(true);
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                    } else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                    break;
            case ACTUALIZAR:
                if(fecha.getSelectedDate() == null ||  txtCantidadPresupuesto.getText().isEmpty() || cmbCodigoEmpresa.getSelectionModel().getSelectedItem()== null){
                    JOptionPane.showMessageDialog(null, "Debe rellenar los espacios");
                }else{
                    actualizar();
                    limpiarControles();
                    cmbCodigoEmpresa.setValue("");
                    desactivarControles();
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarPresupuesto(?,?,?)}");
            Presupuesto registro = (Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setCodigoPresupuesto(Integer.parseInt(txtCodigoPresupuesto.getText()));
            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            //registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            //procedimiento.setInt(4, registro.getCodigoEmpresa());
            procedimiento.execute();
        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No puedes ingresar más de 10 dígitos enteros.\n" +  "Y solamente puedes agregar 2 decimales\n" + "Tampoco puedes editar el codigo empresa y codigo presupuesto.");
        }
    }
    
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                
                
                break;
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/francopaiz/image/refresh_208px.png"));
                imgReporte.setImage(new Image("/org/francopaiz/image/document_500px.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                tblPresupuestos.getSelectionModel().clearSelection();
        }
    }
 
    public void desactivarControles() {
        txtCantidadPresupuesto.setEditable(false);
        fecha.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);

    }
    
    public void activarControles(){
        txtCantidadPresupuesto.setEditable(true);
        fecha.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
        
    }
    
    public void limpiarControles(){
        txtCodigoPresupuesto.clear();
        txtCantidadPresupuesto.clear();
        fecha.setSelectedDate(null);
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
        cmbCodigoEmpresa.setValue("");
        tblPresupuestos.getSelectionModel().clearSelection();
        
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
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
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
