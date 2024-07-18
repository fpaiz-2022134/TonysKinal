
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
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.francopaiz.bean.Empresa;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;
import org.francopaiz.report.GenerarReporte;

/**
 *
 * @author fpaiz
 */
public class EmpresaController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    //Se necesita una variable de tipo observable list para ver los datos.
    private ObservableList<Empresa> listaEmpresa;
    private Principal escenarioPrincipal;
    

    
    
    private final String fondoEmpresa = "/org/francopaiz/report/FondoReportes.png";
    private final String fondoGeneral = "/org/francopaiz/report/FondoReportes.png";
    
   // String letras ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    //Text
    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefonoEmpresa;
    //Table
    @FXML private TableView tblEmpresas;
    //Table Column
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccionEmpresa;
    @FXML private TableColumn colTelefonoEmpresa;
    //Button
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnReporteGeneral;
    
    //Image
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos() {
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
        
    }
    
    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
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
                
                /*if((txtNombreEmpresa.getText()==null)||(txtDireccionEmpresa.getText() == null)||(txtTelefonoEmpresa.getText()== null) ){
                    JOptionPane.showMessageDialog(null, "Debe rellenar los textos");
                    tipoDeOperacion = operaciones.NINGUNO;
                    
                }else{
                    tipoDeOperacion = operaciones.GUARDAR;  
                }*/
                
                tipoDeOperacion = operaciones.GUARDAR;

                
                break;
            case GUARDAR:
                if(txtNombreEmpresa.getText().isEmpty()||txtDireccionEmpresa.getText().isEmpty()||txtTelefonoEmpresa.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Debe rellenar los espacios.");
                    
                } else{
                    if(txtTelefonoEmpresa.getText().length() > 8 ){
                        JOptionPane.showMessageDialog(null, "No puede ingresar más de 8 digitos en el telefono.\n " + "Tampoco puede ingresar letras");
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
                    cargarDatos();  
                    }
                    
                }

        }
    }
    
    public void eliminar() {
        
        switch (tipoDeOperacion) {
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
                // Se realiza la evaluación y el proceso de eliminación de una tupla.
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresa(?)}");
                            procedimiento.setInt(1, ((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                            procedimiento.execute();
                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblEmpresas.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                           // e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "No puedes eliminar un registro que esta siendo usado en presupuesto o servicio.\n" + 
                                    "Por favor eliminar el registro en presupuesto o servicio previamente.\n" + "- Puede estar siendo usada en una tabla has, por favor revisar -");
                            limpiarControles();
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
                        tblEmpresas.getSelectionModel().clearSelection();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
            
        }
    }
    
    public void seleccionarElemento() {
        if (tipoDeOperacion != operaciones.GUARDAR) {
            if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                txtCodigoEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
                txtNombreEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa()));
                txtDireccionEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion()));
                txtTelefonoEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono()));
            } else {
                JOptionPane.showMessageDialog(null, "No hay datos en este campo.\n" + "Seleccione otro campo.");
            }
        } else {
             JOptionPane.showMessageDialog(null, "No puedes guardar los datos de un registro ya existente.");
             limpiarControles();
             tblEmpresas.getSelectionModel().clearSelection();
        }

    }

    public void guardar() {
        
           Empresa registro = new Empresa();
        // Asignamos el texto al modelo que tenemos en bean.
        //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpres.getText()));
        /*if((txtNombreEmpresa.getText()==null)&&(txtDireccionEmpresa.getText() == null)&&(txtTelefonoEmpresa.getText()== null) ){
            JOptionPane.showMessageDialog(null, "Debe rellenar los textos");
        }*/
        
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefonoEmpresa.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresa(?,?,?)}");
            procedimiento.setString(1, registro.getNombreEmpresa());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getTelefono());
            procedimiento.execute();
            listaEmpresa.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        
        
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
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
                if (txtNombreEmpresa.getText().isEmpty() || txtDireccionEmpresa.getText().isEmpty() || txtTelefonoEmpresa.getText().isEmpty()) {
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

    
    public void actualizar() {

        
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmpresa(?,?,?,?)}");
                Empresa registro = (Empresa) tblEmpresas.getSelectionModel().getSelectedItem();
                registro.setNombreEmpresa(txtNombreEmpresa.getText());
                registro.setDireccion(txtDireccionEmpresa.getText());
                registro.setTelefono(txtTelefonoEmpresa.getText());
                procedimiento.setInt(1, registro.getCodigoEmpresa());
                procedimiento.setString(2, registro.getNombreEmpresa());
                procedimiento.setString(3, registro.getDireccion());
                procedimiento.setString(4, registro.getTelefono());
                procedimiento.execute();
            } catch (Exception e) {
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

                    URL in = this.getClass().getResource("/org/francopaiz/report/ReporteEmpresas.jasper");
                    report = (JasperReport) JRLoader.loadObject(in);
                    Map parametros = new HashMap();
                    parametros.clear();
                    parametros.put("fondoEmpresa", this.getClass().getResourceAsStream(fondoEmpresa));
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
                tblEmpresas.getSelectionModel().clearSelection();

        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        parametros.put("fondoEmpresa", this.getClass().getResourceAsStream(fondoEmpresa));
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
    }
    
    public void reporteGeneral(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (txtCodigoEmpresa.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debes seleccionar una casilla para generar el reporte :)");
                } else {
                    imprimirReporteGeneral();
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
                tblEmpresas.getSelectionModel().clearSelection();
                
        }
    }
    
    public void imprimirReporteGeneral(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(txtCodigoEmpresa.getText());
        parametros.put("codEmpresa", codEmpresa);
        parametros.put("fondoGeneral", this.getClass().getResourceAsStream(fondoEmpresa));
        GenerarReporte.mostrarReporte("ReporteGeneral.jasper", "Reporte General Empresarial", parametros);
    }

    // Métodos para hacer funcionar los botones.
    //DESACTIVACIÓN DE BOTONES
    public void desactivarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }

    //ACTIVACIÓN DE BOTONES
    public void activarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }
    
    public void limpiarControles() {
        txtCodigoEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccionEmpresa.clear();
        txtTelefonoEmpresa.clear();
        tblEmpresas.getSelectionModel().clearSelection();
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
    
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
    
    //Validaciones
    
    @FXML
    private void numeros(KeyEvent event){
        String numeros = event.getCharacter();
        
        if(!numeros.matches("[0-9]")){
            event.consume();
        }
    }
    
    /*@FXML
    private void letras(KeyEvent event){
        String caracteres = event.getCharacter();
        
        if(!caracteres.matches("[a-z\\sA-Z]")){
            event.consume();
        }
    }*/
    
    /*public void reporteEmpresa(){
        
        JasperReport report;
        JasperPrint re;
        try {
            
            URL in = this.getClass().getResource("/org/francopaiz/report/ReporteEmpresas.jasper");
            report = (JasperReport)JRLoader.loadObject(in);
            Map parametros = new HashMap();
            parametros.clear();
            parametros.put("Membrete tonys kinal fp", this.getClass().getResourceAsStream(fondoEmpresa));
            re = JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());

            JasperViewer.viewReport(re,false);
        } catch (Exception e) {
        }
    }*/
   
}
