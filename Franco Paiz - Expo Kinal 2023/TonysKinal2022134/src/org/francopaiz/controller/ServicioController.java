
package org.francopaiz.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
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
import org.francopaiz.bean.Servicio;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;
import org.francopaiz.report.GenerarReporte;

/**
 *
 * @author fpaiz
 */
public class ServicioController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicios;
    private ObservableList<Empresa> listaEmpresas;
    private DatePicker fechaServicio;
    
    private final String fondoServicio = "/org/francopaiz/report/FondoReportes.png";
    
    //Text
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtHoraServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    
    // GridPane
    @FXML private GridPane grpFechaServicio;
    // Combo Box
    @FXML private ComboBox cmbCodigoEmpresa;
    // Table View y Columns
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa;
    // Button
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
        fechaServicio = new DatePicker(Locale.ENGLISH);
        fechaServicio.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaServicio.getCalendarView().todayButtonTextProperty().set("Today");
        fechaServicio.getCalendarView().setShowWeeks(false);
        fechaServicio.getStylesheets().add("/org/francopaiz/resource/TonysKinal.css");
        grpFechaServicio.add(fechaServicio, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        desactivarControles();

    }

    public void cargarDatos() {
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Time>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));

    }
    
    public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){
            if (tblServicios.getSelectionModel().getSelectedItem()!=null) {
                txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                fechaServicio.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
                txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
                txtHoraServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()));
                txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
                txtTelefonoContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
                cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            } else {
                JOptionPane.showMessageDialog(null, "No hay datos en este campo.\n" + "Seleccione otro campo.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No puedes guardar los datos de un registro ya existente.");
            limpiarControles();
            tblServicios.getSelectionModel().clearSelection();
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

    public ObservableList<Servicio> getServicio() {
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
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

    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEmpresas = FXCollections.observableArrayList(lista);
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
                if (fechaServicio.getSelectedDate() == null || txtTipoServicio.getText().isEmpty() || txtHoraServicio.getText().isEmpty()
                        || txtLugarServicio.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty() || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null) {
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
                        cmbCodigoEmpresa.setValue("");
                        cargarDatos();

                    }
                }
        }

    }

    public void guardar() {
        try {
        Servicio registro = new Servicio();
        registro.setFechaServicio(fechaServicio.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(Time.valueOf(txtHoraServicio.getText()));
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setTime(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicios.add(registro);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "La hora debes escribirlo con el formato *Hora:Minuto:Segundo*\n" +
                        "Por favor escribir la hora correctamente o una hora valida en el formato de 24h.\n" +
                        "Importante: Debe incluir los segundos siempre que escriba la hora. Ejemplo: 12:30:45");
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
                if (tblServicios.getSelectionModel().getSelectedItem()!= null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Servicio",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                            procedimiento.setInt(1, ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicios.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cmbCodigoEmpresa.setValue("");
                            tblServicios.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            //e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Este registro no puede ser eliminado mientras sea usado en una tabla has.\n" + "- Por favor, revisar -");
                        }
                    } else if(respuesta == JOptionPane.NO_OPTION){
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
                        tblServicios.getSelectionModel().clearSelection();
                    }
                    
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/francopaiz/image/refreshh.png"));
                    imgReporte.setImage(new Image("/org/francopaiz/image/cancelar.png"));
                    activarControles();
                    cmbCodigoEmpresa.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                if (fechaServicio.getSelectedDate() == null || txtTipoServicio.getText().isEmpty() || txtHoraServicio.getText().isEmpty()
                        || txtLugarServicio.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty() || cmbCodigoEmpresa.getSelectionModel().getSelectedItem() == null) {
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarServicio(?,?,?,?,?,?)}");
            Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
            registro.setCodigoServicio(Integer.parseInt(txtCodigoServicio.getText()));
            registro.setFechaServicio(fechaServicio.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(Time.valueOf(txtHoraServicio.getText()));
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3, registro.getTipoServicio());
            procedimiento.setTime(4, registro.getHoraServicio());
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "La hora debes escribirlo con el formato *Hora:Minuto:Segundo*\n" +
                        "Por favor escribir la hora correctamente o una hora valida en el formato de 24h.\n" +
                        "Importante: Debe incluir los segundos siempre que escriba la hora. Ejemplo: 12:30:45");
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                
                break;
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
                tblServicios.getSelectionModel().clearSelection();
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoServicio", null);
        parametros.put("fondoServicio", this.getClass().getResourceAsStream(fondoServicio));
        GenerarReporte.mostrarReporte("ReporteServicios.jasper", "Reporte de Servicios", parametros);
    }
    public void activarControles() {
        fechaServicio.setDisable(false);
        txtTipoServicio.setEditable(true);
        txtHoraServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigoServicio.clear();
        fechaServicio.setSelectedDate(null);
        txtTipoServicio.clear();
        txtHoraServicio.clear();
        txtLugarServicio.clear();
        txtTelefonoContacto.clear();
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
        cmbCodigoEmpresa.setValue("");
        tblServicios.getSelectionModel().clearSelection();
    }
    
        public void desactivarControles() {
        fechaServicio.setDisable(true);
        txtTipoServicio.setEditable(false);
        txtHoraServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
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
    private void numeros(KeyEvent event){
        String numeros = event.getCharacter();
        if (!numeros.matches("[0-9]")) {
            event.consume();
        }
    }
    
    @FXML 
    private void hora(KeyEvent event){
        String numeros = event.getCharacter();
        String dosPuntos = event.getCharacter();
        if (!numeros.matches("[0-9]")) {
            if (!dosPuntos.matches("[:]")) {
                event.consume();
            }
        }
    }

}
