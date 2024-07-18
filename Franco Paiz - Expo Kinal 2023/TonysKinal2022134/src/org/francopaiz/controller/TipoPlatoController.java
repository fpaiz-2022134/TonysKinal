
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
import org.francopaiz.bean.TipoPlato;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;
import org.francopaiz.report.GenerarReporte;

/**
 *
 * @author fpaiz
 */
public class TipoPlatoController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;

    private ObservableList<TipoPlato> listaTipoPlato;
    
    
    private final String fondoTipoPlato = "/org/francopaiz/report/FondoReportes.png";
    //Text
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionTipoPlato;
    
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
    @FXML private TableView tblTiposPlatos;
    //Table column
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionTipoPlato;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos(){
        tblTiposPlatos.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcionTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionTipo"));
    }
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTiposPlatos()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            // While que valida registros
            while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                           resultado.getString("descripcionTipo")));
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTipoPlato = FXCollections.observableArrayList(lista);
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
                tipoDeOperacion = TipoPlatoController.operaciones.GUARDAR;
            break;
            case GUARDAR:
                if(txtDescripcionTipoPlato.getText().isEmpty()){
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
                    tipoDeOperacion = TipoPlatoController.operaciones.NINGUNO;
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
                tipoDeOperacion = TipoPlatoController.operaciones.NINGUNO;
                break;
            default:
                if (tblTiposPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                            procedimiento.setInt(1, ((TipoPlato) tblTiposPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            procedimiento.execute();
                            listaTipoPlato.remove(tblTiposPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblTiposPlatos.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            // e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "No puedes eliminar un registro que esta siendo usado en Plato.\n" + 
                                    "Por favor eliminar el registro en Plato previamente\n" + "- Puede estar siendo usada en una tabla has, por favor revisar -");
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
                        tipoDeOperacion = TipoPlatoController.operaciones.NINGUNO;
                        tblTiposPlatos.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
       }
    }
    
          
          public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTiposPlatos.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/francopaiz/image/refreshh.png"));
                    imgReporte.setImage(new Image("/org/francopaiz/image/cancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                if(txtDescripcionTipoPlato.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Debe rellenar los espacios.");
                } else{
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTipoPlato(?,?)}");
            TipoPlato registro = (TipoPlato) tblTiposPlatos.getSelectionModel().getSelectedItem();
            registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcionTipo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    public void seleccionarElemento(){
        if(tipoDeOperacion != operaciones.GUARDAR){
          if(tblTiposPlatos.getSelectionModel().getSelectedItem() != null){
          txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato) tblTiposPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
          txtDescripcionTipoPlato.setText(String.valueOf(((TipoPlato) tblTiposPlatos.getSelectionModel().getSelectedItem()).getDescripcionTipo()));
            
        }else {
            JOptionPane.showMessageDialog(null, "No hay datos en este campo\n" + "Seleccione otro campo.");
            
        }  
        }else{
            JOptionPane.showMessageDialog(null, "No puedes guardar los datos de un registro ya existente.");
            limpiarControles();
            tblTiposPlatos.getSelectionModel().clearSelection();
        }
        
    }
    
    public void guardar(){
        
            TipoPlato registro = new TipoPlato();
            registro.setDescripcionTipo(txtDescripcionTipoPlato.getText());

            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
                procedimiento.setString(1, registro.getDescripcionTipo());
                procedimiento.execute();
                listaTipoPlato.add(registro);
            } catch(Exception e){
                e.printStackTrace();
            }
        
    }
        
     public void reporte() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                imprimirReporte();
                /*JasperReport report;
                JasperPrint re;
                try {

                    URL in = this.getClass().getResource("/org/francopaiz/report/ReporteTiposPlatos.jasper");
                    report = (JasperReport) JRLoader.loadObject(in);
                    Map parametros = new HashMap();
                    parametros.clear();
                    parametros.put("fondoTipoPlato", this.getClass().getResourceAsStream(fondoTipoPlato));
                    //re = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
                    
                   
                    re = JasperFillManager.fillReport(report, parametros, Conexion.getInstance().getConexion());
                    JasperViewer.viewReport(re, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

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
                tblTiposPlatos.getSelectionModel().clearSelection();

        }
    }
     
     public void imprimirReporte(){
         Map parametros = new HashMap();
         parametros.put("codigoTipoPlato", null);
         parametros.put("fondoTipoPlato", this.getClass().getResourceAsStream(fondoTipoPlato));
         GenerarReporte.mostrarReporte("ReporteTiposPlatos.jasper", "Reporte de Tipo Plato", parametros);
     }
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }    
    
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoTipoPlato.clear();
        txtDescripcionTipoPlato.clear();
        tblTiposPlatos.getSelectionModel().clearSelection();
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
