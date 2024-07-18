
package org.francopaiz.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.francopaiz.bean.Empleado;
import org.francopaiz.bean.Servicio;
import org.francopaiz.bean.Servicio_has_Empleado;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;

/**
 *
 * @author fpaiz
 */
public class Servicio_has_EmpleadoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO, GUARDAR, ELIMINAR, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio_has_Empleado> listaServicios_has_Empleados;
    private ObservableList<Servicio> listaServicios;
    private ObservableList<Empleado> listaEmpleados;
    private DatePicker fechaServicioEmpleado;
    
    // TEXTFIELD
    //@FXML private TextField txtCodigoServicioEmpleado;
    @FXML private TextField txtCodigoServicioEmpleado;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    
    //COMBOBOX
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    
    //TABLE VIEW
    @FXML private TableView tblServicios_has_Empleados;
    @FXML private TableColumn colCodigoServicioEmpleado;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colLugarEvento;
    
    //BUTTON
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
    
    // IMAGE
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private ImageView imgEliminar;
    
    // Datepicke
    @FXML private GridPane grpFechaServicioEmpleado;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fechaServicioEmpleado = new DatePicker(Locale.ENGLISH);
        fechaServicioEmpleado.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaServicioEmpleado.getCalendarView().todayButtonTextProperty().set("Today");
        fechaServicioEmpleado.getCalendarView().setShowWeeks(false);
        fechaServicioEmpleado.getStylesheets().add("/org/francopaiz/resource/TonysKinal.css");
        grpFechaServicioEmpleado.add(fechaServicioEmpleado, 3, 1);
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
        desactivarControles();
        
    }
    
    
    public void cargarDatos(){
        tblServicios_has_Empleados.setItems(getServicio_has_Empleado());
        colCodigoServicioEmpleado.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Integer>("Servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, Time>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado, String>("lugarEvento"));
    }
    
    public void seleccionarElemento(){
        if (tipoDeOperacion != operaciones.GUARDAR) {
            if (tblServicios_has_Empleados.getSelectionModel().getSelectedItem() != null) {
                txtCodigoServicioEmpleado.setText(String.valueOf(((Servicio_has_Empleado)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
                // cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Servicio_has_Plato)tblServicios_has_Platos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicio_has_Empleado)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicio_has_Empleado)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                fechaServicioEmpleado.selectedDateProperty().set(((Servicio_has_Empleado)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getFechaEvento());
                txtHoraEvento.setText(String.valueOf(((Servicio_has_Empleado)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getHoraEvento()));
                txtLugarEvento.setText(((Servicio_has_Empleado)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getLugarEvento());
            
            } else {
                JOptionPane.showMessageDialog(null, "No hay datos en este campo.\n" + "Seleccione otro campo.");

            }
        } else {
            JOptionPane.showMessageDialog(null, "No puedes guardar los datos de un registro ya existente.");

        }
    }
    
    public ObservableList<Servicio_has_Empleado> getServicio_has_Empleado(){
        ArrayList <Servicio_has_Empleado> lista = new ArrayList<Servicio_has_Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio_has_Empleado(resultado.getInt("Servicios_codigoServicio"),
                                                    resultado.getInt("codigoServicio"),
                                                    resultado.getInt("codigoEmpleado"),
                                                    resultado.getDate("fechaEvento"),
                                                    resultado.getTime("horaEvento"),
                                                    resultado.getString("lugarEvento")
                                        
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicios_has_Empleados = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Servicio> getServicio() {
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
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
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList <Empleado> lista = new ArrayList <Empleado>();
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
                                        resultado.getInt("codigoTipoEmpleado")
                                        
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaEmpleados = FXCollections.observableArrayList(lista);
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
    
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleado(registro.getInt("codigoEmpleado"),
                                        registro.getInt("numeroEmpleado"),
                                        registro.getString("apellidosEmpleado"),
                                        registro.getString("nombresEmpleado"),
                                        registro.getString("direccionEmpleado"),
                                        registro.getString("telefonoContacto"),
                                        registro.getString("gradoCocinero"),
                                        registro.getInt("codigoTipoEmpleado")
                                            
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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
                if (txtCodigoServicioEmpleado.getText().isEmpty() || cmbCodigoServicio.getSelectionModel().getSelectedItem() == null
                        || cmbCodigoEmpleado.getSelectionModel().getSelectedItem() == null || fechaServicioEmpleado.getSelectedDate() == null || txtHoraEvento.getText().isEmpty() || txtLugarEvento.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe rellenar los espacios.");
                } else {
                    guardar();
                    limpiarControles();
                    desactivarControles();
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
        try {
        Servicio_has_Empleado registro = new Servicio_has_Empleado();
        registro.setServicios_codigoServicio(Integer.parseInt(txtCodigoServicioEmpleado.getText()));
        registro.setCodigoServicio(((Servicio) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fechaServicioEmpleado.getSelectedDate());
        registro.setHoraEvento(Time.valueOf(txtHoraEvento.getText()));
        registro.setLugarEvento(txtLugarEvento.getText());
        
        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio_has_Empleado(?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.setInt(3, registro.getCodigoEmpleado());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setTime(5, registro.getHoraEvento());
            procedimiento.setString(6, registro.getLugarEvento());
            procedimiento.execute();
            listaServicios_has_Empleados.add(registro);
            
        } catch (Exception e) {
            //e.printStackTrace();
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
                btnNuevo.setText("Crear");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
             default:
                 if (tblServicios_has_Empleados.getSelectionModel().getSelectedItem()!=null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Servicio_has_Empleado", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
 
                     if (respuesta == JOptionPane.YES_OPTION) {
                         try {
                             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio_has_Empleado(?)}");
                             procedimiento.setInt(1, ((Servicio_has_Empleado)tblServicios_has_Empleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                             procedimiento.execute();
                             listaServicios_has_Empleados.remove(tblServicios_has_Empleados.getSelectionModel().getSelectedItem());
                             limpiarControles();
                             tblServicios_has_Empleados.getSelectionModel().clearSelection();
                             
                             
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     } else if(respuesta == JOptionPane.NO_OPTION){
                            limpiarControles();
                            desactivarControles();
                            btnNuevo.setText("Nuevo");
                            btnEliminar.setText("Eliminar");
                            btnEditar.setDisable(false);
                            btnReporte.setDisable(false);
                            imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                            imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                            tipoDeOperacion = operaciones.NINGUNO;
                            tblServicios_has_Empleados.getSelectionModel().clearSelection();                        
                     }
                 } else {
                     JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                 }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblServicios_has_Empleados.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/francopaiz/image/refreshh.png"));
                    imgReporte.setImage(new Image("/org/francopaiz/image/cancelar.png"));
                    activarControles();
                    txtCodigoServicioEmpleado.setEditable(false);
                    cmbCodigoServicio.setDisable(true);
                    cmbCodigoEmpleado.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                if (txtCodigoServicioEmpleado.getText().isEmpty() || cmbCodigoServicio.getSelectionModel().getSelectedItem() == null
                        || cmbCodigoEmpleado.getSelectionModel().getSelectedItem() == null || fechaServicioEmpleado.getSelectedDate() == null || txtHoraEvento.getText().isEmpty() || txtLugarEvento.getText().isEmpty()) {
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
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarServicio_has_Empleado(?,?,?,?)}");
            Servicio_has_Empleado registro = (Servicio_has_Empleado) tblServicios_has_Empleados.getSelectionModel().getSelectedItem();

            registro.setServicios_codigoServicio(Integer.parseInt(txtCodigoServicioEmpleado.getText()));
            registro.setFechaEvento(fechaServicioEmpleado.getSelectedDate());
            registro.setHoraEvento(Time.valueOf(txtHoraEvento.getText()));
            registro.setLugarEvento(txtLugarEvento.getText());

            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setTime(3, registro.getHoraEvento());
            procedimiento.setString(4, registro.getLugarEvento());
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
                tblServicios_has_Empleados.getSelectionModel().clearSelection();
        }
    }

    public void limpiarControles(){
        txtCodigoServicioEmpleado.clear();
        cmbCodigoServicio.getSelectionModel().clearSelection();
        cmbCodigoEmpleado.getSelectionModel().clearSelection();
        txtHoraEvento.clear();
        txtLugarEvento.clear();
        fechaServicioEmpleado.setSelectedDate(null);
    }
            
    public void desactivarControles(){
        txtCodigoServicioEmpleado.setEditable(false);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
        //
        txtHoraEvento.setEditable(false);
        txtLugarEvento.setEditable(false);
        fechaServicioEmpleado.setDisable(true);
    }
    public void activarControles(){
        txtCodigoServicioEmpleado.setEditable(true);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        //
        txtHoraEvento.setEditable(true);
        txtLugarEvento.setEditable(true);
        fechaServicioEmpleado.setDisable(false);
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
