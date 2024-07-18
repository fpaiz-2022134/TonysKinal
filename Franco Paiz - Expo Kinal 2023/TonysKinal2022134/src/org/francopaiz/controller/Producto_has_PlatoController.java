
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
import org.francopaiz.bean.Producto;
import org.francopaiz.bean.Producto_has_Plato;
import org.francopaiz.db.Conexion;
import org.francopaiz.main.Principal;

/**
 *
 * @author fpaiz
 */
public class Producto_has_PlatoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO, GUARDAR, ELIMINAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Plato> listaPlatos;
    private ObservableList <Producto> listaProductos;
    private ObservableList <Producto_has_Plato> listaProductos_has_Platos;
    
    
    //Text
    @FXML private TextField txtCodigoProductoPlato;
    // CMB
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoProducto;
    
    // Table View y Columns
    
    @FXML private TableView tblProductos_has_Platos;
    @FXML private TableColumn colCodigoProductoPlato;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
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
        cmbCodigoProducto.setItems(getProducto());
        desactivarControles();
    }
        
    public void cargarDatos(){
        tblProductos_has_Platos.setItems(getProducto_has_Plato());
        colCodigoProductoPlato.setCellValueFactory(new PropertyValueFactory<Producto_has_Plato, Integer>("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Producto_has_Plato, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto_has_Plato, Integer>("codigoProducto"));
    }
    
    public void seleccionarElemento(){
        if (tipoDeOperacion != operaciones.GUARDAR) {
            if (tblProductos_has_Platos.getSelectionModel().getSelectedItem()!=null) {
                txtCodigoProductoPlato.setText(String.valueOf(((Producto_has_Plato)tblProductos_has_Platos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
                cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Producto_has_Plato)tblProductos_has_Platos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Producto_has_Plato)tblProductos_has_Platos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
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
    
    public Producto buscarProducto(int codigoProducto){
        Producto  resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            
            while(registro.next()){
                resultado = new Producto(registro.getInt("codigoProducto"),  
                        registro.getString("nombreProducto"),
                        registro.getInt("cantidadProducto")
                
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Producto_has_Plato> getProducto_has_Plato(){
        ArrayList<Producto_has_Plato> lista = new ArrayList<Producto_has_Plato>();
        try {
            PreparedStatement procedimiento =Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_has_Platos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto_has_Plato(resultado.getInt("Productos_codigoProducto"),
                                                resultado.getInt("codigoPlato"),
                                                resultado.getInt("codigoProducto")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        return listaProductos_has_Platos = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                                        resultado.getString("nombreProducto"),
                                        resultado.getInt("cantidadProducto")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaProductos = FXCollections.observableArrayList(lista);
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
                if (txtCodigoProductoPlato.getText().isEmpty() || cmbCodigoPlato.getSelectionModel().getSelectedItem() == null || cmbCodigoProducto.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Debes rellenar los espacios vacíos.");

                } else {
                    guardar();
                    limpiarControles();
                    activarControles();
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
    
    /*
    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Crear");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(true);
                btnEditar.setDisable(true);
                imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblProductos_has_Platos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Producto_has_Plato", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (true) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProducto_has_Plato(?)}");
                            procedimiento.setInt(1, ((Producto_has_Plato) tblProductos_has_Platos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto());
                            procedimiento.execute();
                            listaProductos_has_Platos.remove(tblProductos_has_Platos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            tblProductos_has_Platos.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            limpiarControles();
                            desactivarControles();
                            btnNuevo.setText("Nuevo");
                            btnEliminar.setText("Eliminar");
                            btnEditar.setDisable(false);
                            btnReporte.setDisable(false);
                            imgNuevo.setImage(new Image("/org/francopaiz/image/Agregar.png"));
                            imgEliminar.setImage(new Image("/org/francopaiz/image/delete.png"));
                            tipoDeOperacion = operaciones.NINGUNO;
                            tblProductos_has_Platos.getSelectionModel().clearSelection();
                        }
                    } else {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }

        }
    }*/
    
    public void guardar(){
      // registro.setCodigoTipoPlato(((TipoPlato) cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        Producto_has_Plato registro = new Producto_has_Plato();
        registro.setProductos_codigoProducto(Integer.parseInt(txtCodigoProductoPlato.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoProducto(((Producto)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto_has_Plato(?,?,?)}");
            procedimiento.setInt(1, registro.getProductos_codigoProducto());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.execute();
            listaProductos_has_Platos.add(registro);
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
                JOptionPane.showMessageDialog(null, "No pueden eliminarse los datos.");
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                JOptionPane.showMessageDialog(null, "No pueden editarse los datos.");
                break;
            default:
                JOptionPane.showMessageDialog(null, "No pueden editarse los datos.");
        }
    }
    
    public void limpiarControles(){
        txtCodigoProductoPlato.clear();
        cmbCodigoPlato.getSelectionModel().clearSelection();
        cmbCodigoProducto.getSelectionModel().clearSelection();
        tblProductos_has_Platos.getSelectionModel().clearSelection();
        cmbCodigoPlato.setValue("");
        cmbCodigoProducto.setValue("");
    }
    public void desactivarControles(){
        
        txtCodigoProductoPlato.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
        
        
    }
    
    public void activarControles(){
        txtCodigoProductoPlato.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
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
