
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
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.francopaiz.bean.Producto;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;

/**
 *
 * @author fpaiz
 */
public class ProductoController implements Initializable{
    
    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;

    private ObservableList<Producto> listaProducto;
    private final String fondoProducto = "/org/francopaiz/report/FondoReportes.png";
    
    //Text
        @FXML private TextField txtCodigoProducto;
        @FXML private TextField txtNombreProducto;
        @FXML private TextField txtCantidadProducto;
        
    //Button
        @FXML private Button btnNuevo;
        @FXML private Button btnEditar;
        @FXML private Button btnReporte;
        @FXML private Button btnEliminar;
    //Image
        @FXML private ImageView imgNuevo;
        @FXML private ImageView imgEditar;
        @FXML private ImageView imgReporte;
        @FXML private ImageView imgEliminar;
    //Table
        @FXML private TableView tblProductos;
    //Table Column
        @FXML private TableColumn colCodigoProducto;
        @FXML private TableColumn colNombreProducto;
        @FXML private TableColumn colCantidadProducto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
   public void cargarDatos(){
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidadProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidadProducto"));
    }
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            // While que valida registros
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                                           resultado.getString("nombreProducto"),
                                           resultado.getInt("cantidadProducto")));
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaProducto = FXCollections.observableArrayList(lista);
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
                tipoDeOperacion = ProductoController.operaciones.GUARDAR;
            break;
            case GUARDAR:
                if(txtNombreProducto.getText().isEmpty()||txtCantidadProducto.getText().isEmpty()){
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
                tipoDeOperacion = ProductoController.operaciones.NINGUNO;
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
                tipoDeOperacion = ProductoController.operaciones.NINGUNO;
                break;
            default:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                            procedimiento.setInt(1, ((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblProductos.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            e.printStackTrace();
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
                        tipoDeOperacion = ProductoController.operaciones.NINGUNO;
                         tblProductos.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
       }
    }
      
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
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
                if (txtNombreProducto.getText().isEmpty() || txtCantidadProducto.getText().isEmpty()) {
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProducto(?,?,?)}");
            Producto registro = (Producto) tblProductos.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidadProducto(Integer.parseInt(txtCantidadProducto.getText()));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidadProducto());
            
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
   
    public void seleccionarElemento() {
        if (tipoDeOperacion != operaciones.GUARDAR) {
            if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                txtCodigoProducto.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
                txtNombreProducto.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto()));
                txtCantidadProducto.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCantidadProducto()));

            } else {
                JOptionPane.showMessageDialog(null, "No hay datos en este campo\n" + "Seleccione otro campo.");

            }
        } else {
            JOptionPane.showMessageDialog(null, "No puedes guardar los datos de un registro ya existente.");
            limpiarControles();
            tblProductos.getSelectionModel().clearSelection();
        }

    }

     
     public void guardar(){
        
            Producto registro = new Producto();
            //registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidadProducto(Integer.parseInt(txtCantidadProducto.getText()));


            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto(?,?)}");
                procedimiento.setString(1, registro.getNombreProducto());
                procedimiento.setInt(2, registro.getCantidadProducto());
                procedimiento.execute();
                listaProducto.add(registro);
            } catch(Exception e){
                e.printStackTrace();
            }
        
    }
     
      public void reporte() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                JasperReport report;
                JasperPrint re;
                try {

                    URL in = this.getClass().getResource("/org/francopaiz/report/ReporteProductos.jasper");
                    report = (JasperReport) JRLoader.loadObject(in);
                    Map parametros = new HashMap();
                    parametros.clear();
                    parametros.put("fondoProducto", this.getClass().getResourceAsStream(fondoProducto));
                    //re = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
                    
                   
                    re = JasperFillManager.fillReport(report, parametros, Conexion.getInstance().getConexion());
                    JasperViewer.viewReport(re, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                tblProductos.getSelectionModel().clearSelection();

        }
    }

    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidadProducto.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtCantidadProducto.clear();
        tblProductos.getSelectionModel().clearSelection();
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
     
      @FXML
    private void numeros(KeyEvent event){
        String numeros = event.getCharacter();
        
        if(!numeros.matches("[0-9]")){
            event.consume();
        }
    }
    
}
