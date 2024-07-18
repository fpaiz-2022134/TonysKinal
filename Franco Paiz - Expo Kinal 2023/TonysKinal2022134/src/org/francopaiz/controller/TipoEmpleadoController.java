
package org.francopaiz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.francopaiz.bean.TipoEmpleado;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;
import org.francopaiz.report.GenerarReporte;

/**
 *
 * @author fpaiz
 */
public class TipoEmpleadoController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    
    private final String fondoTipoEmpleado = "/org/francopaiz/report/FondoReportes.png";
    
    
    
    //Text
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcionTipoEmpleado;
    
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
    
    // Table
    
    @FXML private TableView tblTiposEmpleados;
    
    // Table Column
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos(){
        tblTiposEmpleados.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTiposEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            
            // While que valida registros
            while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                           resultado.getString("descripcion")));
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
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
                if(txtDescripcionTipoEmpleado.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Debe rellenar los espacios.");
                } else{
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                tipoDeOperacion = TipoEmpleadoController.operaciones.NINGUNO;
                cargarDatos();
                }
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
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblTiposEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                            procedimiento.setInt(1, ((TipoEmpleado) tblTiposEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                            procedimiento.execute();
                            listaTipoEmpleado.remove(tblTiposEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                             tblTiposEmpleados.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            //e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "No puedes eliminar un registro que esta siendo usado en Empleado.\n" + 
                                    "Por favor eliminar el registro en Empleado previamente.\n" + "- Puede estar siendo usada en una tabla has, por favor revisar -");
                        }
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        desactivarControles();
                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                        imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                        tipoDeOperacion = operaciones.NINGUNO;
                        tblTiposEmpleados.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
       }
    }
    
    
    
    
    public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){
            if(tblTiposEmpleados.getSelectionModel().getSelectedItem() != null){
          txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado) tblTiposEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
          txtDescripcionTipoEmpleado.setText(String.valueOf(((TipoEmpleado) tblTiposEmpleados.getSelectionModel().getSelectedItem()).getDescripcion()));
            
        }else {
            JOptionPane.showMessageDialog(null, "No hay datos en este campo\n" + "Seleccione otro campo.");
            
        }
        }else{
            JOptionPane.showMessageDialog(null, "No puedes guardar los datos de un registro ya existente.");
            limpiarControles();
            tblTiposEmpleados.getSelectionModel().clearSelection();
        }
        
    }
    
    public void guardar(){
        
        
        TipoEmpleado registro = new TipoEmpleado();
        registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoEmpleado(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTipoEmpleado.add(registro);
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTiposEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/francopaiz/image/refreshh.png"));
                    imgReporte.setImage(new Image("/org/francopaiz/image/cancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                if (txtDescripcionTipoEmpleado.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe rellenar los espacios.");
                } else {
                    actualizar();
                    limpiarControles();
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
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTipoEmpleado(?,?)}");
            TipoEmpleado registro = (TipoEmpleado) tblTiposEmpleados.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
     public void reporte() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                imprimirReporte();
               /* JasperReport report;
                JasperPrint re;
                try {

                    URL in = this.getClass().getResource("/org/francopaiz/report/ReporteTiposEmpleados.jasper");
                    report = (JasperReport) JRLoader.loadObject(in);
                    Map parametros = new HashMap();
                    parametros.clear();
                    parametros.put("fondoTipoEmpleado", this.getClass().getResourceAsStream(fondoTipoEmpleado));
                    //re = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
                    
                   
                    re = JasperFillManager.fillReport(report, parametros, Conexion.getInstance().getConexion());
                    JasperViewer.viewReport(re, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;*/
            
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
                tblTiposEmpleados.getSelectionModel().clearSelection();

        }
    }
     
     public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoTipoEmpleado", null);
        parametros.put("fondoTipoEmpleado", this.getClass().getResourceAsStream(fondoTipoEmpleado));
        GenerarReporte.mostrarReporte("ReporteTiposEmpleados.jasper", "Reporte de Tipos Empleados", parametros);
    }
    public void ventanaEmpleado() {
        escenarioPrincipal.ventanaEmpleado();
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public void desactivarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoTipoEmpleado.clear();
        txtDescripcionTipoEmpleado.clear();
        tblTiposEmpleados.getSelectionModel().clearSelection();
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
}
